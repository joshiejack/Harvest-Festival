package joshie.harvest.core.util;


import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Implement this on my classes that should be registered to the eventbus
 *  Specify the side if neccessary **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HFEvents {
    /** The side this packet is for **/
    Side value() default Side.SERVER;
}