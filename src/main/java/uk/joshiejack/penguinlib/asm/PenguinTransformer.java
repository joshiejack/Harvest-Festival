package uk.joshiejack.penguinlib.asm;

public class PenguinTransformer extends Transformer {
    public PenguinTransformer() {
        super(new ASMReloadEvent());
    }
}