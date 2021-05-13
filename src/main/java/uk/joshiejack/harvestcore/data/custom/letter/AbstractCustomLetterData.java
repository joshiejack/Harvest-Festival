package uk.joshiejack.harvestcore.data.custom.letter;

import joptsimple.internal.Strings;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;

public abstract class AbstractCustomLetterData<L extends Letter, A extends AbstractCustomLetterData> extends AbstractCustomData<L, A> {
    protected String text = Strings.EMPTY;
    protected boolean isRepeatable = false;
    protected int deliveryTime = 0;
}
