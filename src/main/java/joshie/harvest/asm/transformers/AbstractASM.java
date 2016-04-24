package joshie.harvest.asm.transformers;

import joshie.harvest.core.config.ASM;
import net.minecraft.tileentity.TileEntityChest;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public abstract class AbstractASM {
    public abstract boolean isClass(String name);

    public ClassVisitor newInstance(ClassWriter writer) {
        return null;
    }

    public Object getTarget() {
        return new TileEntityChest();
    }

    public boolean isVisitor() {
        return true;
    }

    public byte[] transform(byte[] modified) {
        return modified;
    }

    public boolean isActive(ASM config) {
        return true;
    }
}