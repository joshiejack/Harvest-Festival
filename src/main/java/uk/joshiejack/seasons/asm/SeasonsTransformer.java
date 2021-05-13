package uk.joshiejack.seasons.asm;

import uk.joshiejack.penguinlib.asm.Transformer;

public class SeasonsTransformer extends Transformer {
    public SeasonsTransformer() {
        super(new ASMTemperature());
    }
}