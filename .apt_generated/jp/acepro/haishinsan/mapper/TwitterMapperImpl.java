package jp.acepro.haishinsan.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jp.acepro.haishinsan.db.entity.TwitterTemplate;
import jp.acepro.haishinsan.dto.twitter.TwitterAdsDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.dto.twitter.TwitterTweet;
import jp.acepro.haishinsan.form.TwitterAdsInputForm;
import jp.acepro.haishinsan.form.TwitterTemplateInputForm;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-08-24T14:36:44+0900",
    comments = "version: 1.1.0.Final, compiler: Eclipse JDT (IDE) 3.13.0.v20170516-1929, environment: Java 1.8.0_144 (Oracle Corporation)"
)
public class TwitterMapperImpl implements TwitterMapper {

    @Override
    public TwitterTemplateDto tempFormToDto(TwitterTemplateInputForm twittertemplateInputForm) {
        if ( twittertemplateInputForm == null ) {
            return null;
        }

        TwitterTemplateDto twitterTemplateDto = new TwitterTemplateDto();

        twitterTemplateDto.setBroadKeyword( twittertemplateInputForm.getBroadKeyword() );
        twitterTemplateDto.setCampaignName( twittertemplateInputForm.getCampaignName() );
        twitterTemplateDto.setDailyBudget( twittertemplateInputForm.getDailyBudget() );
        twitterTemplateDto.setEndTime( twittertemplateInputForm.getEndTime() );
        if ( twittertemplateInputForm.getLocation() != null ) {
            twitterTemplateDto.setLocation( String.valueOf( twittertemplateInputForm.getLocation() ) );
        }
        if ( twittertemplateInputForm.getObjective() != null ) {
            twitterTemplateDto.setObjective( String.valueOf( twittertemplateInputForm.getObjective() ) );
        }
        List<String> list = twittertemplateInputForm.getRegions();
        if ( list != null ) {
            twitterTemplateDto.setRegions(       new ArrayList<String>( list )
            );
        }
        twitterTemplateDto.setSimilarToFollowersOfUser( twittertemplateInputForm.getSimilarToFollowersOfUser() );
        twitterTemplateDto.setStartTime( twittertemplateInputForm.getStartTime() );
        twitterTemplateDto.setTemplateId( twittertemplateInputForm.getTemplateId() );
        twitterTemplateDto.setTemplateName( twittertemplateInputForm.getTemplateName() );
        twitterTemplateDto.setTemplatePriority( twittertemplateInputForm.getTemplatePriority() );
        twitterTemplateDto.setTemplateType( twittertemplateInputForm.getTemplateType() );
        twitterTemplateDto.setTotalBudget( twittertemplateInputForm.getTotalBudget() );

        return twitterTemplateDto;
    }

    @Override
    public TwitterTemplate tempDtoToEntity(TwitterTemplateDto twitterTemplateDto) {
        if ( twitterTemplateDto == null ) {
            return null;
        }

        TwitterTemplate twitterTemplate = new TwitterTemplate();

        twitterTemplate.setTemplateId( twitterTemplateDto.getTemplateId() );
        twitterTemplate.setTemplateName( twitterTemplateDto.getTemplateName() );
        twitterTemplate.setTemplatePriority( twitterTemplateDto.getTemplatePriority() );
        twitterTemplate.setCampaignName( twitterTemplateDto.getCampaignName() );
        twitterTemplate.setDailyBudget( twitterTemplateDto.getDailyBudget() );
        twitterTemplate.setLocation( twitterTemplateDto.getLocation() );
        twitterTemplate.setTotalBudget( twitterTemplateDto.getTotalBudget() );

        twitterTemplate.setStartTime( jp.acepro.haishinsan.util.StringFormatter.dateFormat(twitterTemplateDto.getStartTime()) );
        twitterTemplate.setRegions( jp.acepro.haishinsan.util.TwitterUtil.formatToOneString(twitterTemplateDto.getRegions()) );
        twitterTemplate.setEndTime( jp.acepro.haishinsan.util.StringFormatter.dateFormat(twitterTemplateDto.getEndTime()) );

        return twitterTemplate;
    }

    @Override
    public TwitterTemplateDto tempEntityToDto(TwitterTemplate twitterTemplate) {
        if ( twitterTemplate == null ) {
            return null;
        }

        TwitterTemplateDto twitterTemplateDto = new TwitterTemplateDto();

        twitterTemplateDto.setCampaignName( twitterTemplate.getCampaignName() );
        twitterTemplateDto.setDailyBudget( twitterTemplate.getDailyBudget() );
        twitterTemplateDto.setLocation( twitterTemplate.getLocation() );
        twitterTemplateDto.setTemplateId( twitterTemplate.getTemplateId() );
        twitterTemplateDto.setTemplateName( twitterTemplate.getTemplateName() );
        twitterTemplateDto.setTemplatePriority( twitterTemplate.getTemplatePriority() );
        twitterTemplateDto.setTotalBudget( twitterTemplate.getTotalBudget() );

        twitterTemplateDto.setStartTime( jp.acepro.haishinsan.util.StringFormatter.formatToHyphen(twitterTemplate.getStartTime()) );
        twitterTemplateDto.setRegions( jp.acepro.haishinsan.util.TwitterUtil.formatStringToList(twitterTemplate.getRegions()) );
        twitterTemplateDto.setEndTime( jp.acepro.haishinsan.util.StringFormatter.formatToHyphen(twitterTemplate.getEndTime()) );

        return twitterTemplateDto;
    }

    @Override
    public List<TwitterTemplateDto> tempListEntityToDto(List<TwitterTemplate> twitterTemplateList) {
        if ( twitterTemplateList == null ) {
            return null;
        }

        List<TwitterTemplateDto> list = new ArrayList<TwitterTemplateDto>();
        for ( TwitterTemplate twitterTemplate : twitterTemplateList ) {
            list.add( tempEntityToDto( twitterTemplate ) );
        }

        return list;
    }

    @Override
    public TwitterTemplateInputForm tempDtoToForm(TwitterTemplateDto twitterTemplateDto) {
        if ( twitterTemplateDto == null ) {
            return null;
        }

        TwitterTemplateInputForm twitterTemplateInputForm = new TwitterTemplateInputForm();

        twitterTemplateInputForm.setBroadKeyword( twitterTemplateDto.getBroadKeyword() );
        twitterTemplateInputForm.setCampaignName( twitterTemplateDto.getCampaignName() );
        twitterTemplateInputForm.setDailyBudget( twitterTemplateDto.getDailyBudget() );
        twitterTemplateInputForm.setEndTime( twitterTemplateDto.getEndTime() );
        if ( twitterTemplateDto.getLocation() != null ) {
            twitterTemplateInputForm.setLocation( Integer.parseInt( twitterTemplateDto.getLocation() ) );
        }
        if ( twitterTemplateDto.getObjective() != null ) {
            twitterTemplateInputForm.setObjective( Integer.parseInt( twitterTemplateDto.getObjective() ) );
        }
        List<String> list = twitterTemplateDto.getRegions();
        if ( list != null ) {
            twitterTemplateInputForm.setRegions(       new ArrayList<String>( list )
            );
        }
        twitterTemplateInputForm.setSimilarToFollowersOfUser( twitterTemplateDto.getSimilarToFollowersOfUser() );
        twitterTemplateInputForm.setStartTime( twitterTemplateDto.getStartTime() );
        twitterTemplateInputForm.setTemplateId( twitterTemplateDto.getTemplateId() );
        twitterTemplateInputForm.setTemplateName( twitterTemplateDto.getTemplateName() );
        twitterTemplateInputForm.setTemplatePriority( twitterTemplateDto.getTemplatePriority() );
        twitterTemplateInputForm.setTemplateType( twitterTemplateDto.getTemplateType() );
        twitterTemplateInputForm.setTotalBudget( twitterTemplateDto.getTotalBudget() );

        return twitterTemplateInputForm;
    }

    @Override
    public TwitterAdsDto adsFormToDto(TwitterAdsInputForm twitterAdsInputForm) {
        if ( twitterAdsInputForm == null ) {
            return null;
        }

        TwitterAdsDto twitterAdsDto = new TwitterAdsDto();

        twitterAdsDto.setCampaignId( twitterAdsInputForm.getCampaignId() );
        twitterAdsDto.setCampaignName( twitterAdsInputForm.getCampaignName() );
        if ( twitterAdsInputForm.getDailyBudget() != null ) {
            twitterAdsDto.setDailyBudget( twitterAdsInputForm.getDailyBudget() );
        }
        twitterAdsDto.setEndTime( twitterAdsInputForm.getEndTime() );
        List<TwitterTweet> list = twitterAdsInputForm.getFollowersTweetList();
        if ( list != null ) {
            twitterAdsDto.setFollowersTweetList(       new ArrayList<TwitterTweet>( list )
            );
        }
        twitterAdsDto.setLocation( twitterAdsInputForm.getLocation() );
        twitterAdsDto.setObjective( twitterAdsInputForm.getObjective() );
        List<String> list_ = twitterAdsInputForm.getRegions();
        if ( list_ != null ) {
            twitterAdsDto.setRegions(       new ArrayList<String>( list_ )
            );
        }
        twitterAdsDto.setStartTime( twitterAdsInputForm.getStartTime() );
        List<TwitterTemplateDto> list__ = twitterAdsInputForm.getTemplateList();
        if ( list__ != null ) {
            twitterAdsDto.setTemplateList(       new ArrayList<TwitterTemplateDto>( list__ )
            );
        }
        if ( twitterAdsInputForm.getTotalBudget() != null ) {
            twitterAdsDto.setTotalBudget( twitterAdsInputForm.getTotalBudget() );
        }
        List<String> list___ = twitterAdsInputForm.getTweetIdList();
        if ( list___ != null ) {
            twitterAdsDto.setTweetIdList(       new ArrayList<String>( list___ )
            );
        }
        List<TwitterTweet> list____ = twitterAdsInputForm.getTweetList();
        if ( list____ != null ) {
            twitterAdsDto.setTweetList(       new ArrayList<TwitterTweet>( list____ )
            );
        }
        twitterAdsDto.setTwitter_campaign_manage_id( twitterAdsInputForm.getTwitter_campaign_manage_id() );
        List<TwitterTweet> list_____ = twitterAdsInputForm.getWebsiteTweetList();
        if ( list_____ != null ) {
            twitterAdsDto.setWebsiteTweetList(       new ArrayList<TwitterTweet>( list_____ )
            );
        }

        return twitterAdsDto;
    }
}
