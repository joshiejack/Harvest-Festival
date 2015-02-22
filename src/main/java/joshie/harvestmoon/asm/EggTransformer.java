package joshie.harvestmoon.asm;

import static joshie.harvestmoon.helpers.SizeableHelper.getQuality;
import static joshie.harvestmoon.helpers.SizeableHelper.getSize;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import joshie.harvestmoon.HarvestOverride.OverrideConfig;
import joshie.harvestmoon.api.IShippable;
import joshie.harvestmoon.lib.SizeableMeta;
import net.minecraft.item.ItemStack;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class EggTransformer implements ITransformer {
    @Override
    public boolean isActive(OverrideConfig config) {
        return config.egg;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "ack" : "net.minecraft.item.ItemEgg";
    }

    public byte[] injectInterfaces(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> intf = new HashSet();
                intf.addAll(Arrays.asList(interfaces));
                intf.add("joshie/harvestmoon/api/IShippable");
                //intf.add("joshie/harvestmoon/api/IRateable");
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    public byte[] injectMethods(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        final String method1 = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method1, "(Lnet/minecraft/item/ItemStack;)J", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "joshie/harvestmoon/asm/EggTransformer", "getSellValue", "(Lnet/minecraft/item/ItemStack;)J", false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        String name = isObfuscated ? "a" : "onItemRightClick";
        String desc = isObfuscated ? "(Ladd;Lahb;Lyz;)Ladd;" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;";

        //Implements the Interfaces ~ Thanks to BlunSunrize!!! :D
        ClassNode node = new ClassNode();
        ClassReader classReader = new ClassReader(injectMethods(injectInterfaces(data)));
        classReader.accept(node, 0);

        //Remove the instructions from onRightClick for the ItemEgg
        Iterator<MethodNode> methods = node.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                int i = 0;
                Iterator<AbstractInsnNode> iter = m.instructions.iterator();
                while (iter.hasNext()) {
                    AbstractInsnNode insn = iter.next();
                    if (i <= 46) {
                        m.instructions.remove(insn);
                    } else break;

                    i++;
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static long getSellValue(ItemStack stack) {
        double quality = 1 + (getQuality(stack.getItemDamage()) * IShippable.SELL_QUALITY_MODIFIER);
        return (long) quality * SizeableMeta.EGG.getSellValue(getSize(stack.getItemDamage()));
    }
}
