package uk.joshiejack.settlements.client.gui;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class NPCButtons {
    private static final List<ButtonData> BUTTONS = Lists.newArrayList();

    public static void register(ResourceLocation scriptID) {
        BUTTONS.add(new ButtonData(scriptID));
    }

    //Called on the server to let the client know
    public static List<ButtonData> getForDisplay(EntityNPC npc, EntityPlayer player) {
        List<ButtonData> list = Lists.newArrayList();
        for (ButtonData button: BUTTONS) {
            Interpreter interpreter = Scripting.get(button.getScript());
            if (interpreter != null && interpreter.isTrue("canDisplay", npc, player)) {
                ButtonData add = new ButtonData(button, interpreter);
                interpreter.callFunction("setupButton", npc, player, add);
                list.add(add);
            }
        }

        return list;
    }

    public static class ButtonData {
        private final ResourceLocation scriptID;
        private String name;
        private ItemStack icon;
        private Interpreter interpreter;

        /* For data transfer */
        public ButtonData (ButtonData data, Interpreter interpreter) {
            this.name = "Unitialized Button";
            this.icon = ItemStack.EMPTY;
            this.scriptID = data.scriptID;
            this.interpreter = interpreter;
        }

        /* For registry purposes only */
        public ButtonData (ResourceLocation scriptID) {
            this.scriptID = scriptID;
        }

        public ButtonData(ByteBuf buf) {
            this.name = ByteBufUtils.readUTF8String(buf);
            this.icon = ByteBufUtils.readItemStack(buf);
            this.scriptID = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        }

        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, name);
            ByteBufUtils.writeItemStack(buf, icon);
            ByteBufUtils.writeUTF8String(buf, scriptID.toString());
        }

        public ResourceLocation getScript() {
            return scriptID;
        }

        public void setName(String name) {
            this.name = name;
            this.name = interpreter.getLocalizedKey(scriptID, name);
        }

        public void setIcon(ItemStackJS stack) {
            this.icon = stack.penguinScriptingObject.copy();
            this.icon.setCount(1);
        }

        public ItemStack getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }
    }
}
