package uk.joshiejack.energy.asm;

import uk.joshiejack.penguinlib.asm.Transformer;

public class EnergyTransformer extends Transformer {
    public EnergyTransformer() {
        super(new ASMJump(), new ASMAppleSkin(), new ASMHungerRender(), new ASMSprinting());
    }
}