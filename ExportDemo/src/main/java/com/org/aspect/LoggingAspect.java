package com.org.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

//Not working now .. need to make sure why it doesnt weave files
@Aspect
public class LoggingAspect
{
	//@Pointcut("execution(public * com.org.export..*.*(..))")
	@Pointcut("execution(* com.org.export..*.*(..))")
    public void methodsToBeProfiled(){}
	@Around("methodsToBeProfiled()")
    public Object profile(ProceedingJoinPoint proceedingJoinPoint) throws Throwable 
    {        
		Object returnVal=null;
		final String methodName=proceedingJoinPoint.getSignature().getName();
        try 
        {      
            final Object[] args=proceedingJoinPoint.getArgs();
            final String arguments;
            if (args == null || args.length == 0) 
            {
              arguments="";
            }
            else 
            {
              arguments=Arrays.deepToString(args);
            }
            System.out.println("Entering method [" + methodName + " with arguments ["+ arguments+ "]");
            returnVal=proceedingJoinPoint.proceed();
            return returnVal;
        } 
        finally 
        {
        	System.out.println("Leaving method [" + methodName + "] with return value ["+ (returnVal != null ? returnVal.toString() : "null")+ "].");
        }
       
    }
	
}
