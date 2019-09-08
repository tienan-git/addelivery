package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.CreativeDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.form.CreativeInputForm;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-06T22:22:59+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class CreativeMapperImpl implements CreativeMapper {

    @Override
    public CreativeDto map(CreativeInputForm creativeInputForm) {
        if ( creativeInputForm == null ) {
            return null;
        }

        CreativeDto creativeDto = new CreativeDto();

        creativeDto.setAdType( creativeInputForm.getAdType() );
        creativeDto.setBudget( creativeInputForm.getBudget() );
        creativeDto.setCampaignName( creativeInputForm.getCampaignName() );
        creativeDto.setCampaignStatus( creativeInputForm.getCampaignStatus() );
        creativeDto.setCheckStatus( creativeInputForm.getCheckStatus() );
        creativeDto.setCreativeType( creativeInputForm.getCreativeType() );
        List<DspCampaignCreInputForm> list = creativeInputForm.getDspCampaignCreInputFormList();
        if ( list != null ) {
            creativeDto.setDspCampaignCreInputFormList(       new ArrayList<DspCampaignCreInputForm>( list )
            );
        }
        creativeDto.setDspSelected( creativeInputForm.isDspSelected() );
        creativeDto.setEndDate( creativeInputForm.getEndDate() );
        creativeDto.setFacebookImage( creativeInputForm.getFacebookImage() );
        creativeDto.setFacebookSelected( creativeInputForm.isFacebookSelected() );
        creativeDto.setGoogleSelected( creativeInputForm.isGoogleSelected() );
        List<Integer> list_ = creativeInputForm.getIdList();
        if ( list_ != null ) {
            creativeDto.setIdList(       new ArrayList<Integer>( list_ )
            );
        }
        List<byte[]> list__ = creativeInputForm.getImageAdImageDataList();
        if ( list__ != null ) {
            creativeDto.setImageAdImageDataList(       new ArrayList<byte[]>( list__ )
            );
        }
        List<MultipartFile> list___ = creativeInputForm.getImageAdImageFileList();
        if ( list___ != null ) {
            creativeDto.setImageAdImageFileList(       new ArrayList<MultipartFile>( list___ )
            );
        }
        creativeDto.setIssueId( creativeInputForm.getIssueId() );
        creativeDto.setResAdDescription( creativeInputForm.getResAdDescription() );
        List<byte[]> list____ = creativeInputForm.getResAdImageDateList();
        if ( list____ != null ) {
            creativeDto.setResAdImageDateList(       new ArrayList<byte[]>( list____ )
            );
        }
        List<MultipartFile> list_____ = creativeInputForm.getResAdImageFileList();
        if ( list_____ != null ) {
            creativeDto.setResAdImageFileList(       new ArrayList<MultipartFile>( list_____ )
            );
        }
        creativeDto.setResAdShortTitle( creativeInputForm.getResAdShortTitle() );
        creativeDto.setSegmentId( creativeInputForm.getSegmentId() );
        creativeDto.setStartDate( creativeInputForm.getStartDate() );
        creativeDto.setTextAdDescription( creativeInputForm.getTextAdDescription() );
        creativeDto.setTextAdTitle1( creativeInputForm.getTextAdTitle1() );
        creativeDto.setTextAdTitle2( creativeInputForm.getTextAdTitle2() );
        creativeDto.setTextFacebookDescription( creativeInputForm.getTextFacebookDescription() );
        creativeDto.setTextInstagramDescription( creativeInputForm.getTextInstagramDescription() );
        List<String> list______ = creativeInputForm.getTweetIdList();
        if ( list______ != null ) {
            creativeDto.setTweetIdList(       new ArrayList<String>( list______ )
            );
        }
        creativeDto.setTwitterSelected( creativeInputForm.isTwitterSelected() );
        creativeDto.setUrl( creativeInputForm.getUrl() );
        List<TwitterTweet> list_______ = creativeInputForm.getWebsiteTweetList();
        if ( list_______ != null ) {
            creativeDto.setWebsiteTweetList(       new ArrayList<TwitterTweet>( list_______ )
            );
        }

        return creativeDto;
    }
}
