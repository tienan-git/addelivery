package jp.acepro.haishinsan.service.twitter;

import java.util.List;

import jp.acepro.haishinsan.dto.twitter.TwitterTemplateDto;

public interface TwitterTemplateService {

	/**
	 * Defaultで三つのテンプレートを作成
	 **/
	void createDefaultTemplate(long shopId);
	
	/**
	 * テンプレートリスト
	 **/
	List<TwitterTemplateDto> templateList();
	
	
	/**
	 * テンプレート詳細
	 **/
	TwitterTemplateDto templateDetail(Long templateId);
	
	/**
	 * テンプレート作成
	 **/
	void createTemplate(TwitterTemplateDto twitterTemplateDto);
	
	/**
	 * テンプレート更新
	 **/
	void templateUpdate(TwitterTemplateDto twitterTemplateDto);
	
	/**
	 * テンプレート削除
	 **/
	TwitterTemplateDto templateDelete(Long templateId);
}
