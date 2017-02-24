package joshie.harvest.knowledge.letter;

import joshie.harvest.api.core.Letter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class LetterDataClient extends LetterData {
    protected Set<ResourceLocation> letters = new HashSet<>(); //Unread letters

    @Override
    public Set<Letter> getLetters() {
        return letters.stream().map(Letter.REGISTRY::get).collect(Collectors.toSet());
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
