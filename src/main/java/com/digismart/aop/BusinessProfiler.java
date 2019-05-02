package com.digismart.aop;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class BusinessProfiler {
	private Logger logger = LoggerFactory.getLogger(BusinessProfiler.class);

	@Around("@annotation(com.digismart.aop.ExecutionTimeCalculation)")
	public Object executionLogging(ProceedingJoinPoint pjp) throws Throwable {
		long lStartTime = System.currentTimeMillis();
		logger.debug("Going to call the method. {}", pjp.getSignature().getName());
		Object output = pjp.proceed();
		long lEndTime = new Date().getTime();
		long difference = lEndTime - lStartTime;
		if ((difference / 1000L) == 0) {
			logger.debug("Total Execution time for Complete Process :" + difference + " milliseconds.");
		} else if ((difference / 1000L) / 60 == 0) {
			logger.debug("Total Execution time for Complete Process :" + (difference / 1000L) + " Seconds.");
		} else {
			logger.debug("Total Execution time for Complete Process :" + (difference / 1000L) / 60 + " Minutes.");
		}
		return output;
	}
}