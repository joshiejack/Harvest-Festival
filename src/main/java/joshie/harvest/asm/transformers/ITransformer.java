package joshie.harvest.asm.transformers;

import joshie.harvest.core.config.Vanilla;

public interface ITransformer {
    public boolean isActive(Vanilla config);
    public String getClass(boolean isObfuscated);
    public byte[] transform(byte[] data, boolean isObfuscated);
}
