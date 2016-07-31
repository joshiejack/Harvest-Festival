package joshie.harvest.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Implement this on my packets to have them registered **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
    /** The side this packet is for **/
    Side value() default Side.BOTH;

    enum Side {
        CLIENT, SERVER, BOTH
    }
}