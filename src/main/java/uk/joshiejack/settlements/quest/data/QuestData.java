package uk.joshiejack.settlements.quest.data;

import net.minecraft.entity.player.EntityPlayer;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.settings.Information;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class QuestData implements INBTSerializable<NBTTagCompound> {
    private final Interpreter interpreter;
    private final PenguinGroup group;

    public QuestData(Quest script) {
        this.interpreter = script.getInterpreter().copy(script.getRegistryName());
        this.group = script.getSettings().getType();
        if (script.getSettings().isDaily()) { //If this is a daily quest, copy over the data from the main line quest
            NBTTagCompound tag = new NBTTagCompound();
            script.getInterpreter().callFunction("saveData", tag);
            this.deserializeNBT(tag);
        }
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void callFunction(String function, Object... objects) {
        interpreter.callFunction(function, objects);
    }

    public Information toInformation(EntityPlayer player) {
        Information information = new Information(group);
        if (interpreter.hasMethod("display")) {
            interpreter.callFunction("display", information, player);
        }

        return information;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (interpreter.hasMethod("saveData")) {
            interpreter.callFunction("saveData", tag);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        if (interpreter.hasMethod("loadData")) {
            interpreter.callFunction("loadData", tag);
        }
    }
}
