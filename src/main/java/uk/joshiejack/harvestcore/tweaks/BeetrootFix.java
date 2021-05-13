package uk.joshiejack.harvestcore.tweaks;

/*
public class BeetrootFix extends Transformer {
    public BeetrootFix() {
        super(new ASMBeetroot());
    }

    public static class ASMBeetroot extends AbstractASM {
        @Override
        public boolean isClass(String name) {
            return name.equals("net.minecraft.block.BlockBeetroot") || name.equals("aot");
        }

        private boolean isValidMethod(String name) {
            return name.equals("updateTick") || name.equals("func_180650_b");
        }

        @Override
        public byte[] transform(byte[] data) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(data);
            classReader.accept(classNode, 0);

            MethodNode updateTick = null;
            for (MethodNode method : classNode.methods) {
                if (isValidMethod(method.name)) {
                    updateTick = method;
                }
            }

            if (updateTick != null) classNode.methods.remove(updateTick);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
        }
    }
}*/
