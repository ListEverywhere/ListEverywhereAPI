package com.gcep.util;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * This class configures Spring AOP and enables a monitoring method to provide trace logs when a method is executed.
 * @author Gabriel Cepleanu
 * @version 1.0
 *
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
	
	/**
	 * Contains the paths for the pointcut to monitor
	 */
	@Pointcut("execution(* com.gcep..controller..*(..)) || execution(* com.gcep..data..*(..)) || execution(* com.gcep..exception..*(..)) || execution(* com.gcep..service..*(..))")
	public void monitor() {
		
	}
	
	/**
	 * Creates the tracer instance
	 * @return Tracer
	 */
	@Bean
	public TracerLog tracerLog() {
		return new TracerLog(true);
	}
	
	/**
	 * Creates the pointcut utilizing the monitor() method
	 * @return Default Pointcut Advisor
	 */
	@Bean
	public Advisor monitorAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("com.gcep.util.AopConfig.monitor()");
		return new DefaultPointcutAdvisor(pointcut, tracerLog());
	}

}
