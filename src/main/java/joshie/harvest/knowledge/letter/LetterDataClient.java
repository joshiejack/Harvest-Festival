package joshie.harvest.knowledge.letter;

import joshie.harvest.api.core.Letter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class LetterDataClient extends LetterData {
    protected Set<ResourceLocation> letters = new HashSet<>(); //Unread letters

    @Override
    public Set<Letter> getLetters() {
        Set<Letter> set = new HashSet<>();
        for (ResourceLocation letter: letters) {
            set.add(Letter.REGISTRY.get(letter));
        }

        return set;
    }

    @Override
    public boolean hasUnreadLetters() {
        return letters.size() > 0;
    }

    public void setLetters(Set<ResourceLocation> letters) {
        this.letters = letters;
    }

    public void add(ResourceLocation letter) {
        letters.add(letter);
    }

    public void remove(ResourceLocation letter) {
        letters.remove(letter);
    }
}
