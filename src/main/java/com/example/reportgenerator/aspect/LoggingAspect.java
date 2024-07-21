package com.example.reportgenerator.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging aspect for TRACE level logging
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory
            .getLogger(LoggingAspect.class);

    /**
     * Logs before entering the method
     * @param joinPoint join point
     */
    @Before("execution(* com.example.reportgenerator.service.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint)
    {
        logger.trace("Entering method: {} with arguments: {}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    /**
     * Logs after exiting the method
     * @param joinPoint join point
     */
    @AfterReturning(pointcut = "execution(* com.example.reportgenerator." +
            "service.*.*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result)
    {
        logger.trace("Exiting method: {} with result: {}",
                joinPoint.getSignature(), result);
    }

    /**
     * Logs after executing the method
     * @param joinPoint join point
     */
    @After("execution(* com.example.reportgenerator.service.*.*(..))")
    public void logAfterMethodCompletion(JoinPoint joinPoint)
    {
        logger.trace("Method {} completed execution.",
                joinPoint.getSignature());
    }
}
