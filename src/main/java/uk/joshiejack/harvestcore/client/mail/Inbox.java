package uk.joshiejack.harvestcore.client.mail;

import com.google.common.collect.Lists;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class Inbox {
    public static final Inbox PLAYER = new Inbox();
    private final List<Letter> unread_letters = Lists.newArrayList();

    public static List<Letter> getLetters() {
        return PLAYER.unread_letters;
    }

    public void add(Letter letter) {
        unread_letters.add(letter);
    }

    public void remove(Letter letter) {
        unread_letters.remove(letter);
    }

    public void set(List<Letter> letters, PenguinGroup... groups) {
        unread_letters.removeIf((letter -> {
            for (PenguinGroup group : groups) {
                if (letter.getGroup() == group) return true;
            }

            return false;
        }));

        unread_letters.addAll(letters);
    }
}
