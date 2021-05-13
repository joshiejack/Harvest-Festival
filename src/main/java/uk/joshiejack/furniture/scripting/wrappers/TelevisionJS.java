package uk.joshiejack.furniture.scripting.wrappers;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.furniture.network.PacketTVChatter;
import uk.joshiejack.furniture.television.TVProgram;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;

@SuppressWarnings("unused")
public class TelevisionJS extends AbstractJS<TileTelevision> {
    public TelevisionJS(TileTelevision television) {
        super(television);
    }

    public void chatter(PlayerJS player, String text) {
        PenguinNetwork.sendToClient(new PacketTVChatter(text, penguinScriptingObject.getPos()), player.penguinScriptingObject);
    }

    public void watch(String text) {
        penguinScriptingObject.setProgram(TVProgram.REGISTRY.get(new ResourceLocation(text)));
    }
}
