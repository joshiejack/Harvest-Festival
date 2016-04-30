package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import joshie.harvest.core.config.ASM;
import joshie.harvest.core.config.HFConfig;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EggTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.EGG_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("ack") || name.equals(ASMConstants.EGG);
    }

    @Override
    public boolean isVisitor() {
        return false;
    }

    private byte[] injectInterfaces(byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                Set<String> intf = new HashSet<String>();
                intf.addAll(Arrays.asList(interfaces));
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.SHIPPABLE));
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.CREATIVE_SORTED));
                intf.add(ASMHelper.toInternalClassName(ASMConstants.API.SIZED_PROVIDER));
                super.visit(version, access, name, signature, superName, intf.toArray(new String[0]));
            }
        };

        cr.accept(cv, 0);
        return cw.toByteArray();
    }

    private byte[] injectMethods(boolean isObfuscated, byte[] data) {
        ClassReader cr = new ClassReader(data);
        ClassWriter cw = new ClassWriter(cr, 0);

        //Sellable Sell Value
        String name = "getSellValue";
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.EGG), name, ASMHelper.toMethodDescriptor("J", ASMConstants.STACK), false);
        mv.visitInsn(Opcodes.LRETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Creative Sort
        name = "getSortValue";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("I", ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.EGG), name, ASMHelper.toMethodDescriptor("I"), false);
        mv.visitInsn(Opcodes.IRETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //Crop Provider
        name = "getSizeable";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.SIZEABLE), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.EGG), name, ASMHelper.toMethodDescriptor("L", ASMConstants.STACK) + ASMHelper.toInternalClassName(ASMConstants.API.SIZEABLE), false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();

        //Display Name Override
        name = isObfuscated ? "func_77653_i" : "getItemStackDisplayName";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor(ASMConstants.STRING, ASMConstants.STACK), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.EGG), "getItemStackDisplayName", ASMHelper.toMethodDescriptor(ASMConstants.STRING, ASMConstants.STACK), false);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();

        //Get Sub Items
        name = isObfuscated ? "func_150895_a" : "getSubItems";
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, name, ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 3);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Overrides.EGG), "getSubItems", ASMHelper.toMethodDescriptor("V", ASMConstants.ITEM, ASMConstants.CREATIVE_TABS, ASMConstants.LIST), false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(4, 1);
        mv.visitEnd();

        cr.accept(cw, 0);
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(byte[] data) {
        //Implements the Interfaces ~ Thanks to BluSunrize!!! :D
        byte[] modified = injectMethods(HFOverride.isObfuscated, injectInterfaces(data));
        if (!HFConfig.asm.EGG_DISABLE_THROWING) return modified;
        else {
            String name = HFOverride.isObfuscated ? "func_77659_a" : "onItemRightClick";
            String desc = ASMHelper.toMethodDescriptor(ASMConstants.ACTION_RESULT, ASMConstants.STACK, ASMConstants.WORLD, ASMConstants.PLAYER, ASMConstants.HAND);

            ClassNode node = new ClassNode();
            ClassReader classReader = new ClassReader(modified);
            classReader.accept(node, 0);

            //Remove the instructions from onItemRightClick for the ItemEgg
            for (MethodNode m : node.methods) {
                if ((m.name.equals(name) && m.desc.equals(desc))) {
                    Iterator<AbstractInsnNode> iter = m.instructions.iterator();
                    while (iter.hasNext()) {
                        AbstractInsnNode insn = iter.next();
                        m.instructions.remove(insn);
                    }

                    //return stack
                    m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    m.instructions.add(new InsnNode(Opcodes.ARETURN));
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(writer);
            return writer.toByteArray();
        }
    }
}