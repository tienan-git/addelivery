package jp.acepro.haishinsan.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import jp.acepro.haishinsan.ApplicationProperties;
import jp.acepro.haishinsan.dto.EmailCampDetailDto;
import jp.acepro.haishinsan.dto.EmailDto;
import jp.acepro.haishinsan.enums.EmailTemplateType;
import jp.acepro.haishinsan.exception.SystemException;
import jp.acepro.haishinsan.service.account.ShopService;
import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.DateUtil;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    ShopService shopservice;

    @Override
    public void sendEmail(EmailDto emailDto) {

        try {

            // Email テンプレート用コンテキストの初期化
            final Context ctx = new Context(Locale.US);
            // 案件/キャンペイン作成時間
            ctx.setVariable("createDateTime", DateUtil.toDateTime(LocalDateTime.now().toString()));
            // 案件ID
            Long issueId = emailDto.getIssueId();
            ctx.setVariable("issueId", issueId);
            // Linkの設定
            ctx.setVariable("urlPrefix", applicationProperties.getUrlPrefix());
            // LogoのURL
            String imageUrl = applicationProperties.getUrlPrefix() + "/images/acepro.png";
            ctx.setVariable("imageUrl", imageUrl);
            // キャンペイン内容
            List<EmailCampDetailDto> campaignList = emailDto.getCampaignList();
            ctx.setVariable("campaignList", campaignList);

            htmlTemplateEngine = emailTemplateEngine();
            String templateName = "";
            String title = "";
            switch (EmailTemplateType.of(emailDto.getTemplateType())) {
            case CAMPAIGN:
                templateName = applicationProperties.getEmailTemplateNameCampaign();
                title = EmailTemplateType.CAMPAIGN.getLabel();
                break;
            case ISSUEREQUEST:
                templateName = applicationProperties.getEmailTemplateNameIssueRequest();
                title = EmailTemplateType.ISSUEREQUEST.getLabel();
                break;
            default:
                break;
            }

            // メールリスト
            String[] shopMailList = null;
            String[] salesMailList = null;
            String[] adminMailList = null;
            if (ContextUtil.getCurrentShop().getShopMailList() != null
                    && !"".equals(ContextUtil.getCurrentShop().getShopMailList().trim())) {
                shopMailList = ContextUtil.getCurrentShop().getShopMailList().replaceAll(" ", "").split(";");
            }
            if (ContextUtil.getCurrentShop().getSalesMailList() != null
                    && !"".equals(ContextUtil.getCurrentShop().getSalesMailList().trim())) {
                salesMailList = ContextUtil.getCurrentShop().getSalesMailList().replaceAll(" ", "").split(";");
            }
            if (applicationProperties.getEmailAdmin() != null
                    && !"".equals(applicationProperties.getEmailAdmin().trim())) {
                adminMailList = applicationProperties.getEmailAdmin().replaceAll(" ", "").split(";");
            }

            // メール内容編集
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true /* multipart */,
                    applicationProperties.getEmailEncoding());
            message.setFrom(new InternetAddress(applicationProperties.getEmailSendFrom(),
                    applicationProperties.getSenderName()));
            // タイトルの設定
            message.setSubject(title + "作成のお知らせ[案件ID: " + issueId + "]");

            final String htmlContent = htmlTemplateEngine.process(templateName, ctx);
            message.setText(htmlContent, true /* isHtml */);
            // 添付ファイル
            List<String> attachmentList = emailDto.getAttachmentList();
            List<String> imageNameList = emailDto.getImageNameList();

            if (Objects.nonNull(attachmentList) && !attachmentList.isEmpty()) {
                for (String attachment : attachmentList) {

//                    final InputStreamSource attachmentSource = new ByteArrayResource(attachment.getBytes());
//                    message.addAttachment(attachment.getOriginalFilename(), attachmentSource, attachment);
                }

            }

            // Send mail to shop
            if (Objects.nonNull(shopMailList) && shopMailList.length != 0) {
                message.setTo(shopMailList);
                mailSender.send(mimeMessage);
            }
            // Send mail to sales
            if (Objects.nonNull(salesMailList) && salesMailList.length != 0) {
                message.setTo(salesMailList);
                mailSender.send(mimeMessage);
            }
            // Send mail to admin
            message.setTo(adminMailList);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("システムエラーが発生しました。");
        }

    }

    private ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(0));
        templateResolver.setResolvablePatterns(Collections.singleton(applicationProperties.getEmailPattern()));
        templateResolver.setPrefix(applicationProperties.getEmailPrefix());
        templateResolver.setSuffix(applicationProperties.getEmailSuffix());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(applicationProperties.getEmailEncoding());
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

}
