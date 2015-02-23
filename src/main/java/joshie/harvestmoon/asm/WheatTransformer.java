package joshie.harvestmoon.asm;

import java.util.Iterator;

import joshie.harvestmoon.config.Vanilla;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class WheatTransformer implements ITransformer {
    @Override
    public boolean isActive(Vanilla config) {
        return config.WHEAT_OVERRIDE;
    }

    @Override
    public String getClass(boolean isObfuscated) {
        return isObfuscated ? "adb" : "net.minecraft.item.Item";
    }

    @Override
    public byte[] transform(byte[] data, boolean isObfuscated) {
        byte[] modified = data;
        String name = isObfuscated ? "l" : "registerItems";
        String desc = "()V";

        ClassNode node = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(node, 0);

        //Remove all Instructions from the onItemUse Method
        boolean found = false;
        Iterator<MethodNode> methods = node.methods.iterator();
        while (methods.hasNext()) {
            MethodNode m = methods.next();
            if ((m.name.equals(name) && m.desc.equals(desc))) {
                for (int j = 0; j < m.instructions.size() && !found; j++) {
                    AbstractInsnNode instruction = m.instructions.get(j);
                    if (instruction.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInstruction = (LdcInsnNode) instruction;
                        if (ldcInstruction.cst.equals("wheat")) {
                            ((TypeInsnNode) m.instructions.get(j + 1)).desc = "joshie/harvestmoon/items/overrides/ItemWheat";
                            ((MethodInsnNode) m.instructions.get(j + 3)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            ((MethodInsnNode) m.instructions.get(j + 5)).desc = "(Ljava/lang/String;)Ljoshie/harvestmoon/items/overrides/ItemWheat;";
                            ((MethodInsnNode) m.instructions.get(j + 5)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            ((MethodInsnNode) m.instructions.get(j + 7)).desc = "(Lnet/minecraft/creativetab/CreativeTabs;)Ljoshie/harvestmoon/items/overrides/ItemWheat;";
                            ((MethodInsnNode) m.instructions.get(j + 7)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            ((MethodInsnNode) m.instructions.get(j + 9)).desc = "(Ljava/lang/String;)Ljoshie/harvestmoon/items/overrides/ItemWheat;";
                            ((MethodInsnNode) m.instructions.get(j + 9)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            //((MethodInsnNode) m.instructions.get(j + 3)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                           // ((MethodInsnNode) m.instructions.get(j + 5)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            //((MethodInsnNode) m.instructions.get(j + 7)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            //((MethodInsnNode) m.instructions.get(j + 9)).owner = "joshie/harvestmoon/items/overrides/ItemWheat";
                            
                            for (int i = 0; i < 32; i++) {
                                AbstractInsnNode n = m.instructions.get(j + i);
                                if (n instanceof TypeInsnNode) {
                                    System.out.println(i + "" + ((TypeInsnNode)n).desc);
                                } else if (n instanceof MethodInsnNode) {
                                    System.out.println(i + " Desc " + ((MethodInsnNode)n).desc);
                                    System.out.println(i + " Name " + ((MethodInsnNode)n).name);
                                    System.out.println(i + " Owner " + ((MethodInsnNode)n).owner);
                                }
                            }

                            found = true;
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
