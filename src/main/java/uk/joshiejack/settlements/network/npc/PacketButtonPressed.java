package uk.joshiejack.settlements.network.npc;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketButtonPressed extends PenguinPacket {
    private ResourceLocation script;
    private int npcID;

    public PacketButtonPressed() {}
    public PacketButtonPressed(ResourceLocation script, int npcID) {
        this.script = script;
        this.npcID = npcID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, script.toString());
        buf.writeInt(npcID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        script = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        npcID = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            Interpreter interpreter = Scripting.get(script);
            if (interpreter.isTrue("canDisplay", entity, player)) {
                interpreter.callFunction("onButtonPressed", entity, player);
            }
        }
    }
}
