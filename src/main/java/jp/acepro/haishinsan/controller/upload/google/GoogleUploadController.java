package jp.acepro.haishinsan.controller.upload.google;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.io.ByteSource;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dto.dsp.DspSegmentListDto;
import jp.acepro.haishinsan.dto.google.GoogleCampaignDto;
import jp.acepro.haishinsan.enums.GoogleAdType;
import jp.acepro.haishinsan.enums.GoogleDeviceType;
import jp.acepro.haishinsan.enums.MediaType;
import jp.acepro.haishinsan.enums.UnitPriceType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.form.UploadGoogleBannerAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleBannerTextAdCreateForm;
import jp.acepro.haishinsan.form.UploadGoogleTextAdCreateForm;
import jp.acepro.haishinsan.service.dsp.DspSegmentService;
import jp.acepro.haishinsan.service.google.GoogleCampaignService;
import jp.acepro.haishinsan.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/upload/google")
public class GoogleUploadController {

	@Autowired
	HttpSession session;

	@Autowired
	ImageUtil imageUtil;

	@Autowired
	GoogleCampaignService googleCampaignService;

	@Autowired
	DspSegmentService dspSegmentService;

	// Select Ad Type
	@GetMapping("/adTypeSelection")
	public ModelAndView adTypeSelection(ModelAndView mv) {
		mv.setViewName("upload/google/adTypeSelection");
		return mv;
	}

	// Create Banner Ad
	@GetMapping("/bannerAd/create")
	public ModelAndView bannerAdCreate(ModelAndView mv) {
		UploadGoogleBannerAdCreateForm form = new UploadGoogleBannerAdCreateForm();
		mv.setViewName("upload/google/bannerAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerAd/confirm")
	public ModelAndView bannerAdConfirm(@Validated UploadGoogleBannerAdCreateForm form, BindingResult result)
			throws IOException {
		try {
			if (!form.getImageFile01().isEmpty()) {
				form.setImageFileName01(form.getImageFile01().getOriginalFilename());
				form.setImageData01("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile01(), MediaType.GOOGLEIMG.getValue()));
				form.setImageBytes01(getByteArrayFromStream(form.getImageFile01().getInputStream()));
			}
			if (!form.getImageFile02().isEmpty()) {
				form.setImageFileName02(form.getImageFile02().getOriginalFilename());
				form.setImageData02("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile02(), MediaType.GOOGLEIMG.getValue()));
				form.setImageBytes02(getByteArrayFromStream(form.getImageFile02().getInputStream()));
			}
			if (!form.getImageFile03().isEmpty()) {
				form.setImageFileName03(form.getImageFile03().getOriginalFilename());
				form.setImageData03("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile03(), MediaType.GOOGLEIMG.getValue()));
				form.setImageBytes03(getByteArrayFromStream(form.getImageFile03().getInputStream()));
			}
			if (!form.getImageFile04().isEmpty()) {
				form.setImageFileName04(form.getImageFile04().getOriginalFilename());
				form.setImageData04("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile04(), MediaType.GOOGLEIMG.getValue()));
				form.setImageBytes04(getByteArrayFromStream(form.getImageFile04().getInputStream()));
			}
			if (form.getImageFile01().isEmpty() && form.getImageFile02().isEmpty() && form.getImageFile03().isEmpty()
					&& form.getImageFile04().isEmpty()) {
				// バナーを少なくとも１枚アップロードしてください。
				throw new BusinessException(ErrorCodeConstant.E70009);
			}
		} catch (BusinessException e) {
			// 異常時レスポンスを作成
			result.reject(e.getMessage(), e.getParams(), null);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("upload/google/bannerAd/create");
			modelAndView.addObject("form", form);
			return modelAndView;
		}
		session.setAttribute("bannerAdForm", form);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerAd/complete")
	public ModelAndView bannerAdComplete() throws IOException {
		UploadGoogleBannerAdCreateForm form = (UploadGoogleBannerAdCreateForm) session.getAttribute("bannerAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/complete");
		session.removeAttribute("bannerAdForm");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー）
		googleCampaignDto.setAdType(GoogleAdType.IMAGE.getValue());
		// バナー（画面入力）
		googleCampaignDto.setImageAdImageFileNameList(new ArrayList<String>());
		googleCampaignDto.setImageAdImageBytesList(new ArrayList<byte[]>());
		if (form.getImageBytes01() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName01());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes01());
		}
		if (form.getImageBytes02() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName02());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes02());
		}
		if (form.getImageBytes03() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName03());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes03());
		}
		if (form.getImageBytes04() != null) {
			googleCampaignDto.getImageAdImageFileNameList().add(form.getImageFileName04());
			googleCampaignDto.getImageAdImageBytesList().add(form.getImageBytes04());
		}
		// 地域単価タイプ（クリック重視）
		googleCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// セグメントリスト取得
		List<DspSegmentListDto> dspSegmentDtoList = null;
		LocalDateTime dateTime = LocalDateTime.now();
		dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(dateTime);

		// セグメントURL分の広告作成
		int urlCount = 0;
		if (dspSegmentDtoList != null && dspSegmentDtoList.size() > 0) {
			for (DspSegmentListDto dto : dspSegmentDtoList) {
				if (!dto.getUrl().isEmpty()) {
					//
					urlCount++;
					// 広告名（画面入力＋セグメントID）※キャンペーン名重複不可のため、セグメントIDで区別
					googleCampaignDto.setCampaignName(
							form.getImgAdName().concat(new String("(" + dto.getSegmentId().toString() + ")")));
					// 最終ページURL（セグメントURL）
					googleCampaignDto.setImageAdFinalPageUrl(dto.getUrl());
					// 広告作成
					googleCampaignService.createCampaign(googleCampaignDto, null);
				}
			}
			if (urlCount == 0) {
				// セグメントURLが存在しない（セグメント存在するが、セグメントURL存在しない）
				throw new BusinessException(ErrorCodeConstant.E00012);
			}
		} else {
			// セグメントURLが存在しない（セグメント存在しない）
			throw new BusinessException(ErrorCodeConstant.E00012);
		}
		return mv;
	}

	// Create Banner&Text Ad
	@GetMapping("/bannerTextAd/create")
	public ModelAndView bannerTextAdCreate(ModelAndView mv) {
		UploadGoogleBannerTextAdCreateForm form = new UploadGoogleBannerTextAdCreateForm();
		mv.setViewName("upload/google/bannerTextAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerTextAd/confirm")
	public ModelAndView bannerTextAdConfirm(UploadGoogleBannerTextAdCreateForm form) throws IOException {
		try {
			if (!form.getImageFile01().isEmpty()) {
				form.setImageFileName01(form.getImageFile01().getOriginalFilename());
				form.setImageData01("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile01(), MediaType.GOOGLERES.getValue()));
				form.setImageBytes01(getByteArrayFromStream(form.getImageFile01().getInputStream()));
			}
			if (!form.getImageFile02().isEmpty()) {
				form.setImageFileName02(form.getImageFile02().getOriginalFilename());
				form.setImageData02("data:image/jpeg;base64,"
						+ imageUtil.getImageBytes(form.getImageFile02(), MediaType.GOOGLERES.getValue()));
				form.setImageBytes02(getByteArrayFromStream(form.getImageFile02().getInputStream()));
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		session.setAttribute("bannerTextAdForm", form);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerTextAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/bannerTextAd/complete")
	public ModelAndView bannerTextAdComplete() {
		UploadGoogleBannerTextAdCreateForm form = (UploadGoogleBannerTextAdCreateForm) session
				.getAttribute("bannerTextAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/bannerTextAd/complete");
		session.removeAttribute("bannerTextAdForm");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー＋文字）
		googleCampaignDto.setAdType(GoogleAdType.RESPONSIVE.getValue());
		// 広告名（画面入力）
		googleCampaignDto.setCampaignName(form.getResAdName());
		// 広告短い見出し（画面入力）
		googleCampaignDto.setResAdShortTitle(form.getResAdShortTitle());
		// 広告説明文（画面入力）
		googleCampaignDto.setResAdDescription(form.getResAdDescription());
		// バナー（画面入力）
		googleCampaignDto.setResAdImageFileNameList(new ArrayList<String>());
		googleCampaignDto.setResAdImageBytesList(new ArrayList<byte[]>());
		if (form.getImageBytes01() != null) {
			googleCampaignDto.getResAdImageFileNameList().add(form.getImageFileName01());
			googleCampaignDto.getResAdImageBytesList().add(form.getImageBytes01());
		}
		if (form.getImageBytes02() != null) {
			googleCampaignDto.getResAdImageFileNameList().add(form.getImageFileName02());
			googleCampaignDto.getResAdImageBytesList().add(form.getImageBytes02());
		}
		// 地域単価タイプ（クリック重視）
		googleCampaignDto.setUnitPriceType(UnitPriceType.CLICK.getValue());
		// 最終ページURL（セグメントURL）
		googleCampaignDto.setResAdFinalPageUrl("https://www.google.com");
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// セグメントリスト取得
		List<DspSegmentListDto> dspSegmentDtoList = null;
		LocalDateTime dateTime = LocalDateTime.now();
		dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(dateTime);

		// セグメントURL分の広告作成
		int urlCount = 0;
		if (dspSegmentDtoList != null && dspSegmentDtoList.size() > 0) {
			for (DspSegmentListDto dto : dspSegmentDtoList) {
				if (!dto.getUrl().isEmpty()) {
					//
					urlCount++;
					// 広告名（画面入力＋セグメントID）※キャンペーン名重複不可のため、セグメントIDで区別
					googleCampaignDto.setCampaignName(
							form.getResAdName().concat(new String("(" + dto.getSegmentId().toString() + ")")));
					// 最終ページURL（セグメントURL）
					googleCampaignDto.setResAdFinalPageUrl(dto.getUrl());
					// 広告作成
					googleCampaignService.createCampaign(googleCampaignDto, null);
				}
			}
			if (urlCount == 0) {
				// セグメントURLが存在しない（セグメント存在するが、セグメントURL存在しない）
				throw new BusinessException(ErrorCodeConstant.E00012);
			}
		} else {
			// セグメントURLが存在しない（セグメント存在しない）
			throw new BusinessException(ErrorCodeConstant.E00012);
		}
		return mv;
	}

	// Create Text Ad
	@GetMapping("/textAd/create")
	public ModelAndView textAdCreate(ModelAndView mv) {
		UploadGoogleTextAdCreateForm form = new UploadGoogleTextAdCreateForm();
		mv.setViewName("upload/google/textAd/create");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/textAd/confirm")
	public ModelAndView textAdConfirm(UploadGoogleTextAdCreateForm form) {
		log.debug(form.toString());
		session.setAttribute("textAdForm", form);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/confirm");
		mv.addObject("form", form);
		return mv;
	}

	@PostMapping("/textAd/complete")
	public ModelAndView textAdComplete() {
		UploadGoogleTextAdCreateForm form = (UploadGoogleTextAdCreateForm) session.getAttribute("textAdForm");
		log.debug(form.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("upload/google/textAd/complete");
		session.removeAttribute("textAdForm");

		GoogleCampaignDto googleCampaignDto = new GoogleCampaignDto();
		// 広告種別（バナー＋文字）
		googleCampaignDto.setAdType(GoogleAdType.TEXT.getValue());
		// 広告名（画面入力）
		googleCampaignDto.setCampaignName(form.getTextAdName());
		// 広告見出し１（画面入力）
		googleCampaignDto.setTextAdTitle1(form.getTextAdTitle1());
		// 広告見出し２（画面入力）
		googleCampaignDto.setTextAdTitle2(form.getTextAdTitle2());
		// 広告説明文（画面入力）
		googleCampaignDto.setTextAdDescription(form.getTextAdDescription());
		googleCampaignDto.setTextAdFinalPageUrl("https://www.google.com");
		// 配信開始日（本日）
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = localDate.format(formatter);
		googleCampaignDto.setStartDate(formattedString);
		// 配信終了日（未指定）
		googleCampaignDto.setEndDate("2037-12-30");
		// 配信地域（日本）
		googleCampaignDto.setLocationList(new ArrayList<Long>(Arrays.asList(2392L)));
		// 日次予算（１円）
		googleCampaignDto.setBudget(1L);
		// ディバイスタイプ（パソコン）
		googleCampaignDto.setDeviceType(GoogleDeviceType.PC.getValue());

		// セグメントリスト取得
		List<DspSegmentListDto> dspSegmentDtoList = null;
		LocalDateTime dateTime = LocalDateTime.now();
		dspSegmentDtoList = dspSegmentService.selectUrlByDateTimeWithNoCheck(dateTime);

		// セグメントURL分の広告作成
		int urlCount = 0;
		if (dspSegmentDtoList != null && dspSegmentDtoList.size() > 0) {
			for (DspSegmentListDto dto : dspSegmentDtoList) {
				if (!dto.getUrl().isEmpty()) {
					//
					urlCount++;
					// 広告名（画面入力＋セグメントID）※キャンペーン名重複不可のため、セグメントIDで区別
					googleCampaignDto.setCampaignName(
							form.getTextAdName().concat(new String("(" + dto.getSegmentId().toString() + ")")));
					// 最終ページURL（セグメントURL）
					googleCampaignDto.setTextAdFinalPageUrl(dto.getUrl());
					// 広告作成
					googleCampaignService.createCampaign(googleCampaignDto, null);
				}
			}
			if (urlCount == 0) {
				// セグメントURLが存在しない（セグメント存在するが、セグメントURL存在しない）
				throw new BusinessException(ErrorCodeConstant.E00012);
			}
		} else {
			// セグメントURLが存在しない（セグメント存在しない）
			throw new BusinessException(ErrorCodeConstant.E00012);
		}
		return mv;
	}

	// Utilities
	private byte[] getByteArrayFromStream(final InputStream inputStream) throws IOException {
		return new ByteSource() {
			@Override
			public InputStream openStream() {
				return inputStream;
			}
		}.read();
	}
}
