package com.gcep.util;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
	
	@Pointcut("execution(* com.gcep..controller..*(..)) || execution(* com.gcep..data..*(..)) || execution(* com.gcep..exception..*(..)) || execution(* com.gcep..service..*(..))")
	public void monitor() {
		
	}
	
	@Bean
	public TracerLog tracerLog() {
		return new TracerLog(true);
	}
	
	@Bean
	public Advisor monitorAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("com.gcep.util.AopConfig.monitor()");
		return new DefaultPointcutAdvisor(pointcut, tracerLog());
	}

}
