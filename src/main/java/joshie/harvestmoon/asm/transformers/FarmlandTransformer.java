package joshie.harvestmoon.asm.transformers;

import joshie.harvestmoon.core.config.Vanilla;
import joshie.harvestmoon.core.lib.HMModInfo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FarmlandTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.FARMLAND_OVERRIDE;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "aky" : "net.minecraft.block.BlockFarmland";
    }

    public byte[] injectMethods(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Redirection for breakBlock to RemoveFarmland
        String name = "breakBlock";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.ILOAD, 3);
        mv.visitVarInsn(Opcodes.ILOAD, 4);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "core/helpers/CropHelper", "removeFarmland", "(Lnet/minecraft/world/World;III)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 7);
        mv.visitEnd();

        //Redirection for onBlockDestroyed to RemoveFarmland
        name = "onBlockDestroyedByExplosion";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/world/World;IIILnet/minecraft/world/Explosion;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitVarInsn(Opcodes.ILOAD, 3);
        mv.visitVarInsn(Opcodes.ILOAD, 4);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "core/helpers/CropHelper", "removeFarmland", "(Lnet/minecraft/world/World;III)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 7);
        mv.visitEnd();

        //Return the relative block hardness
        name = "getPlayerRelativeBlockHardness";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;III)F", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ILOAD, 3);
        mv.visitVarInsn(Opcodes.ILOAD, 4);
        mv.visitVarInsn(Opcodes.ILOAD, 5);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, HMModInfo.ASMPATH + "asm/overrides/BlockFarmland", name, "(Lnet/minecraft/block/Block;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;III)F", false);
        mv.visitInsn(Opcodes.FRETURN);
        mv.visitMaxs(5, 6);
        mv.visitEnd();

        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        return injectMethods(data);
    }
}
