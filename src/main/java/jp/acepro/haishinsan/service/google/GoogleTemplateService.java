package jp.acepro.haishinsan.service.google;

import java.util.List;

import jp.acepro.haishinsan.dto.google.GoogleTemplateDto;

public interface GoogleTemplateService {

	// テンプレート作成（新規ユーザー時のデフォルトテンプレート作成）
	void createDefaultTemplate(long shopId);

	// テンプレート取得（店舗IDより）
	List<GoogleTemplateDto> getTemplateList(Long shopId);

	// テンプレート取得（テンプレートIDより）
	GoogleTemplateDto getTemplate(Long templateId);

	// テンプレート作成
	void createTemplate(GoogleTemplateDto googleTemplateDto);

	// テンプレート更新
	void updateTemplate(GoogleTemplateDto googleTemplateDto);

	// テンプレート削除
	void deleteTemplate(Long templateId);
}
