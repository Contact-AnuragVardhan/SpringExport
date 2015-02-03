package com.org.aspect;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;


public aspect AspectLogging 
{
	pointcut loggerPointCut() : call(* com.org.export..*.*(..));
	
	private static final Logger log = Logger.getLogger("Logging.aspect");
	 
	 Object around() : loggerPointCut() 
	 {
	      Object returnVal=null;
	      final String calleeMethodName =  thisEnclosingJoinPointStaticPart.getSignature().toShortString() ;
	      final String methodName = thisJoinPoint.getSignature().getName();
	      try 
	      {      
	         if(log.isDebugEnabled())
	         {
	        	 printAllParameters(calleeMethodName,thisJoinPoint);
	         }
	         else
	         {
	        	 printParameters(calleeMethodName,thisJoinPoint);
	         }
	         returnVal=  proceed();
	         return returnVal;
	      } 
	      finally 
	      {
	    	 printExit(thisJoinPoint,returnVal);
	      }
	 }
	 
	 private void printParameters(String calleeMethodName,JoinPoint joinPoint)
	 {
		 CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
		 String methodName = methodSignature.getName();
		 final Object[] args= joinPoint.getArgs();
		 final String arguments;
         if (args == null || args.length == 0) 
         {
            arguments="";
         }
         else 
         {
            arguments=Arrays.deepToString(args);
         }
         log.info("Method [" + calleeMethodName + "] called method [" + methodName + "] with arguments ["+ arguments+ "]");
	 }
	 
	 private void printAllParameters(String calleeMethodName,JoinPoint joinPoint) 
	 {
        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        Object[] paramNames = methodSignature.getParameterNames();
        Class[] paramTypes = (Class[])methodSignature.getParameterTypes();
        Object[] paramObjects = joinPoint.getArgs();
        StringBuffer infoMsg = new StringBuffer();

        infoMsg.append("Function " + calleeMethodName + " called function: " + methodName);
        if (paramNames != null && paramNames.length > 0)
        {
            if (paramNames.length == 1)
            {
                infoMsg.append(" with input parameter: ["+ paramNames[1]+ "] = [" + paramObjects[1] + "]");
            }
            else 
            {
                infoMsg.append(" with input parameters: ");
            }
            for (int count = 1; count < paramNames.length; count++) 
            {
                infoMsg.append(" [" + paramTypes[count].getName() + " " + paramNames[count]+ "] = [" + paramObjects[count] + "]");
            }
        }
        else 
        {
            infoMsg.append(" NONE");
        }
       log.info(infoMsg.toString());
	 }
	 
	 private void printExit(JoinPoint joinPoint,Object returnVal) 
	 {
		 String methodName = joinPoint.getSignature().getName();
		 log.info("Leaving method [" + methodName + "] with return value [" + (returnVal != null ? returnVal.toString() : "null")+ "].");
	 }
	 
}
