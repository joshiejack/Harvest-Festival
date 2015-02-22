package joshie.harvestmoon.asm;

import joshie.harvestmoon.HarvestOverride.OverrideConfig;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public interface ITransformer {
    public boolean isActive(OverrideConfig config);
    public String getClass(boolean isObfuscated);
    public byte[] transform(byte[] data, boolean isObfuscated);
}
