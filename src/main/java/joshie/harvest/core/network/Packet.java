package joshie.harvest.core.network;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
    /** The side this packet is for **/
    Side side() default Side.CLIENT;

    /** If this packet should be registered on one side **/
    boolean isSided() default false;
}