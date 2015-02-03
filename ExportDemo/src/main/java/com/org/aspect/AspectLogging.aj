package com.org.aspect;

import java.util.Arrays;



public aspect AspectLogging 
{
 pointcut mainMethod() : call(* com.org.export..*.*(..));
	 
	 Object around() : mainMethod() {
       Object returnVal=null;
       final String calleeMethodName =  thisEnclosingJoinPointStaticPart.getSignature().toShortString() ;
       final String methodName = thisJoinPoint.getSignature().getName();
       try 
       {      
           final Object[] args= thisJoinPoint.getArgs();
           final String arguments;
           if (args == null || args.length == 0) 
           {
             arguments="";
           }
           else 
           {
             arguments=Arrays.deepToString(args);
           }
           System.out.println("Method [" + calleeMethodName + "] called method [" + methodName + "] with arguments ["+ arguments+ "]");
           returnVal=  proceed();
           return returnVal;
       } 
       finally 
       {
       		System.out.println("Leaving method [" + methodName + "] with return value [" + (returnVal != null ? returnVal.toString() : "null")+ "].");
       }
	 }
	 
}
