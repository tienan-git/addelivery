package jp.acepro.haishinsan.util;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.exception.BusinessException;

/**
 * @author jinzhenghan
 *
 */
@Component
public class ImageUtil {

	@Autowired
	ApplicationProperties applicationProperties;

	/**
	 * @param multipartFile
	 * @param mediaType
	 * @return Base64で暗号化した文字列
	 * @throws IOException
	 */

	private Long maxSize = 0L;

	private List<String> contentTypeList = new ArrayList<String>();

	private List<String> dimensionList = new ArrayList<String>();

	private int gifFrames = 0;

	private int gifPlayTime = 0;

	public String getImageBytes(MultipartFile multipartFile, int mediaType) throws IOException {

		// ファイルの0サイズチェック - 共通
		if (multipartFile.isEmpty()) {
			throw new BusinessException(ErrorCodeConstant.E00002);
		}

		// ファイルの最大サイズチェック
		Long size = multipartFile.getSize();

		if (!validSize(size, mediaType)) {
			throw new BusinessException(ErrorCodeConstant.E00003, String.valueOf(size / 1000),
					String.valueOf(maxSize / 1000));
		}

		// ファイルの拡張子チェック
		String contentType = multipartFile.getContentType();
		if (!validType(contentType, mediaType)) {
			throw new BusinessException(ErrorCodeConstant.E00004, contentType.substring(contentType.indexOf("/") + 1),
					contentTypeList.stream().map(s -> s.substring(s.indexOf("/") + 1))
							.collect(Collectors.joining(", ")));
		}

		// ファイルの幅、高さチェック
		InputStream is = multipartFile.getInputStream();
		Image image = ImageIO.read(is);

		if (!validDimension(image, mediaType)) {
			String code = MediaType.of(mediaType).equals(MediaType.FACEBOOK) ? ErrorCodeConstant.E40004
					: ErrorCodeConstant.E00005;

			throw new BusinessException(code,
					String.valueOf(image.getWidth(null)) + "*" + String.valueOf(image.getHeight(null)));

//			throw new BusinessException(code,
//					String.valueOf(image.getWidth(null)) + "*" + String.valueOf(image.getHeight(null)),
//					String.join(", ", dimensionList));
		}

		// GIFの毎秒フレーム数と長さのチェック
		if ("image/gif".compareToIgnoreCase(contentType) == 0) {

			GifProperty gifProperty = getFramesPerSecond(multipartFile);
			int res = validGif(gifProperty, mediaType);

			if (res == 1) { // 毎秒フレーム数のチェックエラー
				throw new BusinessException(ErrorCodeConstant.E00006, String.valueOf(gifProperty.framesPerSecond),
						String.valueOf(gifFrames));

			} else if (res == 2) { // 長さのチェックエラー
				throw new BusinessException(ErrorCodeConstant.E00007,
						String.valueOf(Math.floorDiv(gifProperty.frameCount, gifProperty.framesPerSecond)),
						String.valueOf(gifPlayTime));
			}
		}

		// Base64の文字列に変換
		byte[] bytes = multipartFile.getBytes();
		String Base64Str = Base64.getEncoder().encodeToString(bytes);

		return Base64Str;
	}

	/**
	 * @param size
	 * @param mediaType
	 * @return
	 */
	private boolean validSize(Long size, int mediaType) {

		switch (MediaType.of(mediaType)) {
		case DSP: // dsp
			maxSize = applicationProperties.getDspImageMaxSize();
			break;
		case GOOGLERES1: // google Responsive & Image
		case GOOGLEIMG1:
			maxSize = applicationProperties.getGoogleImageMaxSize();
			break;
		default:
			return true;
		}

		if (size > maxSize) {
			return false;
		}
		return true;
	}

	/**
	 * @param contentType
	 * @param mediaType
	 * @return
	 */
	private boolean validType(String contentType, int mediaType) {

		switch (MediaType.of(mediaType)) {
		case DSP: // dsp
			contentTypeList = applicationProperties.getDspContentTypes();
			break;
		case GOOGLERES1: // google Responsive
			contentTypeList = applicationProperties.getGoogleResponsiveContentTypes();
			break;
		case GOOGLEIMG1: // google Image
			contentTypeList = applicationProperties.getGoogleImageContentTypes();
			break;
		default:
			return true;
		}

		if (contentTypeList == null) {
			return false;
		} else if (contentTypeList.contains(contentType)) {
			return true;
		}

		return false;
	}

	/**
	 * @param image
	 * @param mediaType
	 * @return
	 */
	private boolean validDimension(Image image, int mediaType) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		String dimension = String.valueOf(width) + "*" + String.valueOf(height);
		String minimumDimension = null;
		String maximumDimension = null;
		int minimumWidth = 0;
		int minimumHeight = 0;
		int maximumWidth = 0;
		int maximumHeight = 0;
				
		switch (MediaType.of(mediaType)) {
		case DSP: // dsp
			dimensionList = applicationProperties.getDspDimenstions();
			break;
		case GOOGLERES1: // google Responsive
			dimensionList = applicationProperties.getGoogleResponsiveDimenstions();
			minimumDimension = dimensionList.get(0);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case GOOGLERES2: // google Responsive
			dimensionList = applicationProperties.getGoogleResponsiveDimenstions();
			minimumDimension = dimensionList.get(1);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case GOOGLEIMG1: // google Image
			dimensionList = applicationProperties.getGoogleImageDimenstions();
			minimumDimension = dimensionList.get(0);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case GOOGLEIMG2: // google Image
			dimensionList = applicationProperties.getGoogleImageDimenstions();
			minimumDimension = dimensionList.get(1);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case GOOGLEIMG3: // google Image
			dimensionList = applicationProperties.getGoogleImageDimenstions();
			minimumDimension = dimensionList.get(2);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case GOOGLEIMG4: // google Image
			dimensionList = applicationProperties.getGoogleImageDimenstions();
			minimumDimension = dimensionList.get(3);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			if (width != minimumWidth || height != minimumHeight) {
				return false;
			}
			break;
		case YAHOORESPONSIVE: // yahoo Responsive
			dimensionList = applicationProperties.getYahooInfeedDimenstions();
			break;
		case YAHOOIMAGE: // yahoo Image
			dimensionList = applicationProperties.getYahooTargetingDimenstions();
			break;
		case FACEBOOK: // facebook Image
			if (width != height) {
				return false;
			}
			dimensionList = applicationProperties.getFacebookDimensions();
			minimumDimension = dimensionList.get(0);
			maximumDimension = dimensionList.get(1);
			minimumWidth = Integer.valueOf(minimumDimension.substring(0, minimumDimension.indexOf("*")).trim());
			minimumHeight = Integer.valueOf(minimumDimension.substring(minimumDimension.indexOf("*") + 1).trim());
			maximumWidth = Integer.valueOf(maximumDimension.substring(0, maximumDimension.indexOf("*")).trim());
			maximumHeight = Integer.valueOf(maximumDimension.substring(maximumDimension.indexOf("*") + 1).trim());
			if (width < minimumWidth || width > maximumWidth) {
				return false;
			}

		default:
			return true;
		}

		if (dimensionList == null) {
			return false;
		} else if (dimensionList.contains(dimension)) {
			return true;
		}

		return false;
	}

	/**
	 * @param gifProperty
	 * @param mediaType
	 * @return 0:正常 1:毎秒のフレーム数チェックエラー 2:Gifの長さをチェックエラー
	 */
	private int validGif(GifProperty gifProperty, int mediaType) {

		int res = 0;

		switch (MediaType.of(mediaType)) {
		case DSP: // dsp
			gifFrames = applicationProperties.getDspGifFrames();
			gifPlayTime = applicationProperties.getDspgifPlayTime();
			break;
		case GOOGLEIMG1: // google Image
			gifFrames = applicationProperties.getGoogleImageGifFrames();
			gifPlayTime = applicationProperties.getGoogleImageGifPlayTime();
			break;
		default:
			break;
		}

		// 毎秒のフレーム数をチェック
		if (gifProperty.framesPerSecond > gifFrames) {
			res = 1;

			// Gifの長さをチェック
		} else if (Math.floorDiv(gifProperty.frameCount, gifProperty.framesPerSecond) > gifPlayTime) {
			res = 2;

		}

		return res;
	}

	/**
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private GifProperty getFramesPerSecond(MultipartFile multipartFile) throws IOException {
		ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
		ByteArrayInputStream imageDataInputStream = new ByteArrayInputStream(multipartFile.getBytes());
		ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageDataInputStream);

		reader.setInput(imageInputStream);

		IIOMetadata imageMetaData = reader.getImageMetadata(0);
		String metaFormatName = imageMetaData.getNativeMetadataFormatName();

		IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
		IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

		// フレーム間の間隔時間(1/100秒）
		int delayTime = Integer.parseInt(graphicsControlExtensionNode.getAttribute("delayTime"));
		int framsPerSecond = 100 / delayTime;

		// フレームの個数を取得
		int frameCount = reader.getNumImages(true);

		return new GifProperty(framsPerSecond, frameCount);
	}

	/**
	 * @param rootNode
	 * @param nodeName
	 * @return
	 */
	private IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++) {
			if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
				return ((IIOMetadataNode) rootNode.item(i));
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return (node);
	}

	private class GifProperty {

		public GifProperty(int framesPerSecond, int frameCount) {
			super();
			this.framesPerSecond = framesPerSecond;
			this.frameCount = frameCount;
		}

		int framesPerSecond;
		int frameCount;

	}

}
