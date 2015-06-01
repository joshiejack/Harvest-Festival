package joshie.harvest.asm.transformers;

import java.util.HashSet;
import java.util.Iterator;

import joshie.harvest.asm.overrides.ItemPumpkin;
import joshie.harvest.core.config.Vanilla;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


public class PumpkinTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.PUMPKIN_OVERRIDE;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "adb" : "net.minecraft.item.Item";
    }

    public static HashSet registerPumpkin(HashSet set) {
        set.add(Blocks.pumpkin);
        Item.itemRegistry.addObject(Block.getIdFromBlock(Blocks.pumpkin), "pumpkin", new ItemPumpkin(Blocks.pumpkin));
        return set;
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        String name = isObfuscated ? "func_150900_l" : "registerItems";
        String desc = "()V";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();
        labelTop: while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                for (int j = 0; j < m.instructions.size(); j++) {
                    AbstractInsnNode instruction = m.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("record_wait")) {
                            m.instructions.insert(m.instructions.get(j + 124), new VarInsnNode(Opcodes.ASTORE, 1));
                            m.instructions.insert(m.instructions.get(j + 124), new MethodInsnNode(Opcodes.INVOKESTATIC, HFModInfo.ASMPATH + "asm/transformers/PumpkinTransformer", "registerPumpkin", "(Ljava/util/HashSet;)Ljava/util/HashSet;", false));
                            m.instructions.insert(m.instructions.get(j + 124), new VarInsnNode(Opcodes.ALOAD, 1));
                            break labelTop;
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
