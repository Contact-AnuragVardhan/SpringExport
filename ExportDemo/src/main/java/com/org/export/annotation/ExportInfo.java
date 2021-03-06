package com.org.export.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportInfo 
{
	String headerText();
	/*
	 * This is not working as of now
	 */
	int order() default -1;
	float width() default -1.0f;
	float relativeWidth() default -1.0f;
	boolean wordWrap() default false;
}
