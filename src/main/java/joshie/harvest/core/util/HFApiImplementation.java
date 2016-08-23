package joshie.harvest.core.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Implemented on my classes that implement the main apis in HFApi **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HFApiImplementation {}