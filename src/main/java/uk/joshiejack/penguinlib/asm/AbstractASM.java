package uk.joshiejack.penguinlib.asm;

public abstract class AbstractASM {
    public abstract boolean isClass(String name);
    public abstract byte[] transform(byte[] modified);
}