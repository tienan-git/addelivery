package jp.acepro.haishinsan.service.twitter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.acepro.haishinsan.constant.ErrorCodeConstant;
import jp.acepro.haishinsan.dao.TwitterTemplateCustomDao;
import jp.acepro.haishinsan.dao.TwitterTemplateDao;
import jp.acepro.haishinsan.db.entity.TwitterTemplate;
import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;
import jp.acepro.haishinsan.enums.Flag;
import jp.acepro.haishinsan.enums.TwitterLocationType;
import jp.acepro.haishinsan.exception.BusinessException;
import jp.acepro.haishinsan.mapper.TwitterMapper;
import jp.acepro.haishinsan.service.BaseService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.StringFormatter;
import jp.acepro.haishinsan.util.TwitterUtil;

@Service
public class TwitterTemplateServiceImpl extends BaseService implements TwitterTemplateService {

    @Autowired
    TwitterTemplateDao twitterTemplateDao;

    @Autowired
    TwitterTemplateCustomDao twitterTemplateCustomDao;

    // テンプレート作成
    @Override
    @Transactional
    public void createTemplate(TwitterTemplateDto twitterTemplateDto) {

        // テンプレート名チェック
        TwitterTemplate existName = twitterTemplateCustomDao.selectByName(twitterTemplateDto.getTemplateName(),ContextUtil.getCurrentShopId());
        if (existName != null) {
            throw new BusinessException(ErrorCodeConstant.E20001);
        }

        // テンプレート優先順チェック
        TwitterTemplate existPriority = twitterTemplateCustomDao.selectByPriority(twitterTemplateDto.getTemplatePriority(),ContextUtil.getCurrentShopId());
        if (existPriority != null) {
            throw new BusinessException(ErrorCodeConstant.E20002);
        }

        // テンプレート配信日チェック
        if (!"".equals(twitterTemplateDto.getStartTime()) && !"".equals(twitterTemplateDto.getEndTime())) {
            LocalDate startDate = LocalDate.parse(twitterTemplateDto.getStartTime());
            LocalDate endDate = LocalDate.parse(twitterTemplateDto.getEndTime());
            if (endDate.isBefore(startDate)) {
                throw new BusinessException(ErrorCodeConstant.E20003);
            }
        }

        // テンプレート予算チェック
        if (twitterTemplateDto.getDailyBudget() != null && twitterTemplateDto.getTotalBudget() != null) {
            if (twitterTemplateDto.getDailyBudget() > twitterTemplateDto.getTotalBudget()) {
                throw new BusinessException(ErrorCodeConstant.E20004);
            }
        }

        // 都道府県非空チェック
        if (String.valueOf(TwitterLocationType.REGION.getValue()).equals(twitterTemplateDto.getLocation()) && twitterTemplateDto.getRegions().isEmpty() == true) {
            // 配信都道府県を選択してください。
            throw new BusinessException(ErrorCodeConstant.E20006);
        }

        TwitterTemplate twitterTemplate = TwitterMapper.INSTANCE.tempDtoToEntity(twitterTemplateDto);
        twitterTemplate.setShopId(ContextUtil.getCurrentShop().getShopId());
        twitterTemplateDao.insert(twitterTemplate);

    }

    // テンプレートリスト
    @Override
    @Transactional
    public List<TwitterTemplateDto> templateList() {

        // DBからテンプレートをすべて取得して、リストとして返却
        List<TwitterTemplate> twitterTemplateList = twitterTemplateCustomDao.selectAll(ContextUtil.getCurrentShop().getShopId());
        List<TwitterTemplateDto> twitterTemplateDtoList = TwitterMapper.INSTANCE.tempListEntityToDto(twitterTemplateList);

        return twitterTemplateDtoList;
    }

    // テンプレート詳細
    @Override
    @Transactional
    public TwitterTemplateDto templateDetail(Long templateId) {

        // テンプレートIdでDBからテンプレート情報を取得
        TwitterTemplate twitterTemplate = twitterTemplateDao.selectById(templateId);
        // EntityからDtoまで変更して、返却する
        TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempEntityToDto(twitterTemplate);

        return twitterTemplateDto;
    }

    // テンプレート更新
    @Override
    @Transactional
    public void templateUpdate(TwitterTemplateDto twitterTemplateDto) {

        // テンプレート名チェック
        TwitterTemplate existName = twitterTemplateCustomDao.selectByName(twitterTemplateDto.getTemplateName(),ContextUtil.getCurrentShopId());
        if (existName != null && !existName.getTemplateId().equals(twitterTemplateDto.getTemplateId())) {
            throw new BusinessException(ErrorCodeConstant.E20001);
        }

        // テンプレート優先順チェック
        TwitterTemplate existPriority = twitterTemplateCustomDao.selectByPriority(twitterTemplateDto.getTemplatePriority(),ContextUtil.getCurrentShopId());
        if (existPriority != null && !existPriority.getTemplateId().equals(twitterTemplateDto.getTemplateId())) {
            throw new BusinessException(ErrorCodeConstant.E20002);
        }

        // テンプレート配信日チェック
        if (!"".equals(twitterTemplateDto.getStartTime()) && !"".equals(twitterTemplateDto.getEndTime())) {
            LocalDate startDate = LocalDate.parse(twitterTemplateDto.getStartTime());
            LocalDate endDate = LocalDate.parse(twitterTemplateDto.getEndTime());
            if (endDate.isBefore(startDate)) {
                throw new BusinessException(ErrorCodeConstant.E20003);
            }
        }

        // テンプレート予算チェック
        if (twitterTemplateDto.getDailyBudget() != null && twitterTemplateDto.getTotalBudget() != null) {
            if (twitterTemplateDto.getDailyBudget() > twitterTemplateDto.getTotalBudget()) {
                throw new BusinessException(ErrorCodeConstant.E20004);
            }
        }

        // 都道府県非空チェック
        if (String.valueOf(TwitterLocationType.REGION.getValue()).equals(twitterTemplateDto.getLocation()) && twitterTemplateDto.getRegions().isEmpty() == true) {
            // 配信都道府県を選択してください。
            throw new BusinessException(ErrorCodeConstant.E20006);
        }

        // テンプレートIdでDBからテンプレート情報を取得
        TwitterTemplate twitterTemplate = twitterTemplateDao.selectById(twitterTemplateDto.getTemplateId());
        twitterTemplate.setTemplateName(twitterTemplateDto.getTemplateName());
        twitterTemplate.setTemplatePriority(twitterTemplateDto.getTemplatePriority());
        twitterTemplate.setCampaignName(twitterTemplateDto.getCampaignName());
        twitterTemplate.setStartTime(StringFormatter.dateFormat(twitterTemplateDto.getStartTime()));
        twitterTemplate.setEndTime(StringFormatter.dateFormat(twitterTemplateDto.getEndTime()));
        twitterTemplate.setDailyBudget(twitterTemplateDto.getDailyBudget());
        twitterTemplate.setTotalBudget(twitterTemplateDto.getTotalBudget());
        twitterTemplate.setRegions(TwitterUtil.formatToOneString(twitterTemplateDto.getRegions()));
        twitterTemplate.setLocation(twitterTemplateDto.getLocation());
        // DB更新
        twitterTemplateDao.update(twitterTemplate);

    }

    // テンプレート削除
    @Override
    @Transactional
    public TwitterTemplateDto templateDelete(Long templateId) {

        // テンプレートIdでDBからテンプレート情報を取得
        TwitterTemplate twitterTemplate = twitterTemplateDao.selectById(templateId);
        twitterTemplate.setIsActived(Flag.OFF.getValue());
        // DB更新
        twitterTemplateDao.update(twitterTemplate);
        // Dtoとして返却
        TwitterTemplateDto twitterTemplateDto = TwitterMapper.INSTANCE.tempEntityToDto(twitterTemplate);

        return twitterTemplateDto;
    }

    // デフォルトで三つのテンプレートを作成
    @Transactional
    @Override
    public void createDefaultTemplate(long shopId) {

        // クリック重視
        TwitterTemplate clickTemplate = new TwitterTemplate();
        clickTemplate.setTemplateName("クリック重視");
        clickTemplate.setTemplatePriority(1);
        clickTemplate.setLocation(TwitterLocationType.JAPAN.getValue().toString());
        clickTemplate.setShopId(shopId);
        twitterTemplateDao.insert(clickTemplate);

        // フォロワー重視
        TwitterTemplate followerTemplate = new TwitterTemplate();
        followerTemplate.setTemplateName("フォロワー重視");
        followerTemplate.setTemplatePriority(2);
        followerTemplate.setLocation(TwitterLocationType.JAPAN.getValue().toString());
        followerTemplate.setShopId(shopId);
        twitterTemplateDao.insert(followerTemplate);

        // カスタム
        TwitterTemplate twitterTemplate = new TwitterTemplate();
        twitterTemplate.setTemplateName("カスタム");
        twitterTemplate.setTemplatePriority(3);
        twitterTemplate.setLocation(TwitterLocationType.JAPAN.getValue().toString());
        twitterTemplate.setShopId(shopId);
        twitterTemplateDao.insert(twitterTemplate);

    }
}
