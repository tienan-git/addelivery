package jp.acepro.haishinsan.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jp.acepro.haishinsan.util.ContextUtil;
import jp.acepro.haishinsan.util.MDCUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * カレントユーザIDをMDCに設定
 */
@Aspect
@Component
@Slf4j
public class MDCAspect {

	@Autowired
	HttpSession session;

	@Before("execution(* jp.acepro.haishinsan.controller..*.*(..))"
			+ " && !execution(* jp.acepro.haishinsan.controller.api..*.*(..))")
	public void before(JoinPoint joinPoint) {

		String userAgent = null;
		String ipAddress = null;

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			userAgent = request.getHeader("User-Agent");

			ipAddress = request.getHeader("X-FORWARDED-FOR");
			log.trace("Header X-FORWARDED-FOR:{}", ipAddress);
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				log.trace("request RemoteAddr:{}", ipAddress);
			} else {
				ipAddress = ipAddress.contains(",") && ipAddress.split(",").length > 0 ? ipAddress.split(",")[0]
						: ipAddress;
			}

		} else {
			log.warn("ServletRequestAttributesが取れない");
		}

		MDCUtil.setUserAgent(userAgent);
		MDCUtil.setIpAddress(ipAddress);
		MDCUtil.setUserId(String.valueOf(ContextUtil.getUserId()));
	}
}
