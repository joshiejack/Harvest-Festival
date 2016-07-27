package joshie.harvest.asm.transformers;

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
}