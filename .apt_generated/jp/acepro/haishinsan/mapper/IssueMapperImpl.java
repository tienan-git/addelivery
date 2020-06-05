package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.dto.IssueDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.form.DspCampaignCreInputForm;
import jp.acepro.haishinsan.form.IssueInputForm;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-09-12T20:35:01+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class IssueMapperImpl implements IssueMapper {

    @Override
    public IssueDto map(IssueInputForm issueInputForm) {
        if ( issueInputForm == null ) {
            return null;
        }

        IssueDto issueDto = new IssueDto();

        issueDto.setAdType( issueInputForm.getAdType() );
        issueDto.setBudget( issueInputForm.getBudget() );
        issueDto.setCampaignStatus( issueInputForm.getCampaignStatus() );
        issueDto.setCheckStatus( issueInputForm.getCheckStatus() );
        List<DspCampaignCreInputForm> list = issueInputForm.getDspCampaignCreInputFormList();
        if ( list != null ) {
            issueDto.setDspCampaignCreInputFormList(       new ArrayList<DspCampaignCreInputForm>( list )
            );
        }
        issueDto.setDspSelected( issueInputForm.isDspSelected() );
        issueDto.setEndDate( issueInputForm.getEndDate() );
        issueDto.setFacebookImage( issueInputForm.getFacebookImage() );
        issueDto.setFacebookSelected( issueInputForm.isFacebookSelected() );
        issueDto.setGoogleSelected( issueInputForm.isGoogleSelected() );
        List<Integer> list_ = issueInputForm.getIdList();
        if ( list_ != null ) {
            issueDto.setIdList(       new ArrayList<Integer>( list_ )
            );
        }
        List<byte[]> list__ = issueInputForm.getImageAdImageDataList();
        if ( list__ != null ) {
            issueDto.setImageAdImageDataList(       new ArrayList<byte[]>( list__ )
            );
        }
        List<MultipartFile> list___ = issueInputForm.getImageAdImageFileList();
        if ( list___ != null ) {
            issueDto.setImageAdImageFileList(       new ArrayList<MultipartFile>( list___ )
            );
        }
        issueDto.setIssueId( issueInputForm.getIssueId() );
        issueDto.setResAdDescription( issueInputForm.getResAdDescription() );
        List<byte[]> list____ = issueInputForm.getResAdImageDateList();
        if ( list____ != null ) {
            issueDto.setResAdImageDateList(       new ArrayList<byte[]>( list____ )
            );
        }
        List<MultipartFile> list_____ = issueInputForm.getResAdImageFileList();
        if ( list_____ != null ) {
            issueDto.setResAdImageFileList(       new ArrayList<MultipartFile>( list_____ )
            );
        }
        issueDto.setResAdShortTitle( issueInputForm.getResAdShortTitle() );
        issueDto.setSegmentId( issueInputForm.getSegmentId() );
        issueDto.setStartDate( issueInputForm.getStartDate() );
        issueDto.setTextAdDescription( issueInputForm.getTextAdDescription() );
        issueDto.setTextAdTitle1( issueInputForm.getTextAdTitle1() );
        issueDto.setTextAdTitle2( issueInputForm.getTextAdTitle2() );
        List<String> list______ = issueInputForm.getTweetIdList();
        if ( list______ != null ) {
            issueDto.setTweetIdList(       new ArrayList<String>( list______ )
            );
        }
        issueDto.setTwitterSelected( issueInputForm.isTwitterSelected() );
        issueDto.setUrl( issueInputForm.getUrl() );
        List<TwitterTweet> list_______ = issueInputForm.getWebsiteTweetList();
        if ( list_______ != null ) {
            issueDto.setWebsiteTweetList(       new ArrayList<TwitterTweet>( list_______ )
            );
        }

        return issueDto;
    }
}
