package joshie.harvest.knowledge.letter;

import joshie.harvest.api.core.Letter;

import java.util.Set;

public abstract class LetterData {
    public abstract Set<Letter> getLetters();
    public abstract boolean hasUnreadLetters();
}
