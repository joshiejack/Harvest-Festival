package uk.joshiejack.settlements.entity.ai.action.chat;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionChat;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.network.npc.PacketAsk;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

@PenguinLoader("ask")
public class ActionAsk extends ActionMental implements ActionChat {
    public String question;
    public String[] answers;
    public String[] formatting;
    private String[] functions;
    private boolean asked;
    private boolean answered;

    @Override
    public ActionAsk withData(Object... params) {
        this.question = (String) params[0];
        //Stuff
        List<String> answers = Lists.newArrayList();
        List<String> functions = Lists.newArrayList();
        List<String> formatting = Lists.newArrayList();
        boolean hitBreakPoint = false;
        for (int i = 1; i < params.length; i++) {
            String s1 = Objects.toString(params[i]);
            if (s1.equals("#")) {
                hitBreakPoint = true;
            } else {
                if (hitBreakPoint) formatting.add(s1);
                else if (s1.contains("->")) {
                    answers.add(s1.split("->")[0]);
                    functions.add(s1.split("->")[1]);
                } else answers.add(s1);
            }
        }

        this.answers = answers.toArray(new String[0]);
        this.functions = functions.toArray(new String[0]);
        this.formatting = formatting.toArray(new String[0]);
        return this;
    }

    @Override
    public void onGuiClosed(EntityPlayer player, EntityNPC npc, Object... parameters) {
        answered = true; //To allow this to exit the forever loop
        if (parameters.length == 1) {
            int option = (int) parameters[0]; //Always the case
            Interpreter it = isQuest ? AdventureDataLoader.get(player.world).getTrackerForQuest(player, Quest.REGISTRY.get(registryName)).getData(registryName).getInterpreter() : Scripting.get(registryName);
            it.callFunction(functions[option], player, npc, option);//Call this function with the option number
        }

        npc.removeTalking(player); //Close it no matter the circumstance
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (!asked && player != null) {
            asked = true; //Mark asked as true
            PenguinNetwork.sendToClient(new PacketAsk(player, npc, this), player);
            npc.addTalking(player); //We're talk to this player
        }

        //If the player no longer exists, then finish this action
        return player == null || npc.IsNotTalkingTo(player) || answered ? EnumActionResult.SUCCESS : EnumActionResult.PASS; //Always pass
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("RegistryName", registryName.toString());
        tag.setBoolean("IsQuest", isQuest);
        tag.setString("Question", question);
        tag.setByte("AnswersLength", (byte) answers.length);
        for (int i = 0; i < answers.length; i++) {
            tag.setString("Answer" + i, answers[i]);
        }

        tag.setByte("FormattingLength", (byte) formatting.length);
        for (int i = 0; i < formatting.length; i++) {
            tag.setString("Formatting" + i, formatting[i]);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        registryName = new ResourceLocation(nbt.getString("RegistryName"));
        isQuest = nbt.getBoolean("IsQuest");
        question = nbt.getString("Question");
        int length = nbt.getByte("AnswersLength");
        answers = new String[length];
        for (int i = 0; i < length; i++) {
            answers[i] = nbt.getString("Answer" + i);
        }

        formatting = new String[nbt.getByte("FormattingLength")];
        for (int i = 0; i < formatting.length; i++) {
            formatting[i] = nbt.getString("Formatting" + i);
        }
    }
}
