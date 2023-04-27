package com.gcep.util;

import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

public class TracerLog extends AbstractMonitoringInterceptor {

	private static final long serialVersionUID = 8732437489213545919L;
	
	private static final Logger log = LoggerFactory.getLogger(TracerLog.class);
	
	private static final String LOG_START = "ListEverywhere API method ";
	
	public TracerLog() {}
	
	public TracerLog(boolean useDynamicLogger) {
		setUseDynamicLogger(useDynamicLogger);
	}

	@Override
	protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
		// get the current method name
		String method = createInvocationTraceName(invocation);
		// get start time of method execution
		long startTime = System.currentTimeMillis();
		// start log message
		log.trace(LOG_START + method + " executing at " + new Date());
		try {
			// run method
			return invocation.proceed();
		} finally {
			// get end time of method execution
			long endTime = System.currentTimeMillis();
			// calculate execution time
			long executionTime = endTime - startTime;
			// execution time log message
			log.trace(LOG_START + method + " execution time (ms): " + executionTime);
			// end log message
			log.trace(LOG_START + method + " finished at " + new Date());
		}
	}

}
