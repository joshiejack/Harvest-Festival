package uk.joshiejack.harvestcore.registry.letter;

import uk.joshiejack.penguinlib.scripting.Scripting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class LetterScript extends Letter {
    private ResourceLocation script;
    private boolean requiresAcceptance;

    public LetterScript(ResourceLocation resource, String text, boolean repeatable, int delay, ResourceLocation script) {
        super(resource, text, repeatable, delay);
        this.script = script;
    }

    private void callScript(String function, EntityPlayer player) {
        Scripting.get(script).callFunction(function, player, resource.toString());
    }

    @Override
    public void reject(EntityPlayer player) {
        if (requiresAcceptance) callScript("onLetterRejected", player);
    }

    @Override
    public void accept(EntityPlayer player) {
        if (requiresAcceptance) callScript("onLetterAccepted", player);
    }

    @Override
    public void onClosed(EntityPlayer player) {
        if (!requiresAcceptance) callScript("onLetterClosed", player);
    }
}
