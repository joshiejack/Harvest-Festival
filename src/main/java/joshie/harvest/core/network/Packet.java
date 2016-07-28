package joshie.harvest.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
    /** The side this packet is for **/
    Side side() default Side.BOTH;

    enum Side {
        CLIENT, SERVER, BOTH;
    }
}