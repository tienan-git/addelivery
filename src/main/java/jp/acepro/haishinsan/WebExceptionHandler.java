package jp.acepro.haishinsan;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class WebExceptionHandler {

	// 例外ハンドリング
	@ExceptionHandler(Throwable.class)
	public ModelAndView handleException(Throwable exception) {

		String errMsg = null;
		if (exception instanceof AccessDeniedException) {
			errMsg = "お客様の操作権限が足りません。";
		} else {
			errMsg = "システムエラーが発生しました。";
		}
		exception.printStackTrace();

		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errMsg", errMsg);
		return mv;
	}
}