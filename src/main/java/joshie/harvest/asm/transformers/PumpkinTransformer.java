package joshie.harvest.asm.transformers;

import joshie.harvest.asm.ASMConstants;
import joshie.harvest.asm.ASMHelper;
import joshie.harvest.asm.HFOverride;
import joshie.harvest.asm.overrides.ItemPumpkin;
import joshie.harvest.core.config.ASM;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Iterator;


public class PumpkinTransformer extends AbstractASM {
    @Override
    public boolean isActive(ASM config) {
        return config.PUMPKIN_OVERRIDE;
    }

    @Override
    public boolean isClass(String name) {
        return name.equals("adb") || name.equals(ASMConstants.ITEM);
    }

    @Override
    public boolean isVisitor() {
        return false;
    }

    public static HashSet<Block> registerPumpkin(HashSet<Block> set) {
        set.add(Blocks.PUMPKIN);
        ForgeRegistries.ITEMS.register(new ItemPumpkin(Blocks.PUMPKIN)); //TODO Figure out if this works
        return set;
    }

    @Override
    public byte[] transform(byte[] data) {
        String name = HFOverride.isObfuscated ? "func_150900_l" : "registerItems";
        String desc = ASMHelper.toMethodDescriptor("V");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();
        labelTop:
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                for (int j = 0; j < m.instructions.size(); j++) {
                    AbstractInsnNode instruction = m.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("RECORD_WAIT")) {
                            m.instructions.insert(m.instructions.get(j + 124), new VarInsnNode(Opcodes.ASTORE, 1));
                            m.instructions.insert(m.instructions.get(j + 124), new MethodInsnNode(Opcodes.INVOKESTATIC, ASMHelper.toInternalClassName(ASMConstants.Transformers.PUMPKIN), "registerPumpkin", ASMHelper.toMethodDescriptor(ASMConstants.HASH_SET, ASMConstants.HASH_SET), false));
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