package jp.acepro.haishinsan.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * サービス層の開始・終了のトレース
 */
@Aspect
@Component
@Slf4j
public class TraceAspect {

	@Around("within(jp.acepro.haishinsan.controller.*)")
	public Object around(final ProceedingJoinPoint pjp) throws Throwable {
		if (!log.isTraceEnabled()) {
			return pjp.proceed();
		}

		log.trace("メソッド開始:{}\t引数:{}", pjp.getSignature(), Arrays.asList(pjp.getArgs()));
		final StopWatch sw = new StopWatch();
		sw.start();
		Object res;
		try {
			res = pjp.proceed();
		} catch (Throwable e) {
			sw.stop();
			log.trace("メソッド異常終了:{}\t処理時間:{}ミリ秒\t例外:{}", pjp.getSignature(), sw.getTotalTimeMillis(), e);
			throw e;
		}

		sw.stop();
		log.trace("メソッド終了:{}\t処理時間:{}ミリ秒\t戻り値:{}", pjp.getSignature(), sw.getLastTaskTimeMillis(), res);
		return res;
	}
}
