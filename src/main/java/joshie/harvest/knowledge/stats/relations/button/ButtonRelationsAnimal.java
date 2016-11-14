package joshie.harvest.knowledge.stats.relations.button;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.knowledge.stats.GuiStats;
import joshie.harvest.knowledge.stats.button.ButtonBook;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.RenderHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.item.ItemCrop.Crops;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import joshie.harvest.player.relationships.RelationshipDataClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.UUID;

import static joshie.harvest.npc.HFNPCs.MAX_FRIENDSHIP;

public class ButtonRelationsAnimal extends ButtonBook {
    private static final ResourceLocation HEARTS = new ResourceLocation("textures/gui/icons.png");
    private static final ItemStack PETTED = HFNPCs.TOOLS.getStackFromEnum(NPCTool.SPEECH);
    private static final ItemStack MIRACLE = HFAnimals.TOOLS.getStackFromEnum(Tool.MIRACLE_POTION);
    private static final ItemStack SICK = HFAnimals.TOOLS.getStackFromEnum(Tool.MEDICINE);
    private static final ItemStack VEGETABLE = new ItemStack(Items.CARROT);
    private static final ItemStack FRUIT = new ItemStack(Items.APPLE);
    private static final ItemStack GRAIN = HFCrops.CROP.getStackFromEnum(Crops.GRASS);
    private static final ItemStack SEED = HFAnimals.TOOLS.getStackFromEnum(Tool.CHICKEN_FEED);
    private static final ItemStack BEEF = new ItemStack(Items.BEEF);
    private static final ItemStack CHICKEN = new ItemStack(Items.CHICKEN);
    private static final ItemStack FISH = new ItemStack(Items.FISH);
    private final ItemStack product;
    private final boolean collected;
    private final IAnimalType type;
    private final int relationship;
    private final boolean petted;
    private final boolean eaten;
    private final boolean needsCleaning;
    private final boolean isClean;
    private final boolean isTreated;
    private final boolean isSick;
    private final boolean isPregnant;
    //If sick hearts are green
    //If pregnant



    //Products they make with x5
    //If you've collected their product
    private ItemStack stack;

    public ButtonRelationsAnimal(GuiStats gui, EntityAnimal animal, AnimalStats stats, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, animal.getName());
        this.gui = gui;
        this.width = 120;
        this.height = 16;
        this.stack = stats.getType().getIcon();
        RelationshipDataClient data = HFTrackers.getClientPlayerTracker().getRelationships();
        UUID uuid = EntityHelper.getEntityUUID(animal);
        this.type = stats.getType();
        this.relationship = data.getRelationship(uuid);
        this.petted = data.hasTalked(uuid);
        this.eaten = stats.performTest(AnimalTest.HAS_EATEN);
        this.needsCleaning = stats.performTest(AnimalTest.CAN_CLEAN);
        this.isClean = stats.performTest(AnimalTest.IS_CLEAN);
        this.isSick = stats.performTest(AnimalTest.IS_SICK);
        this.isPregnant = stats.performTest(AnimalTest.IS_PREGNANT);
        this.isTreated = stats.performTest(AnimalTest.HAD_TREAT);
        this.product =  stats.getType().getProduct(MCClientHelper.getPlayer(), stats);
        this.collected = !stats.canProduce();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            mouseDragged(mc, mouseX, mouseY);
            drawForeground();
            boolean flag = mc.fontRendererObj.getUnicodeFlag();
            mc.fontRendererObj.setUnicodeFlag(true);
            mc.fontRendererObj.drawString(TextFormatting.BOLD + displayString, xPosition + 4, yPosition - 7, 0x857754);
            mc.fontRendererObj.setUnicodeFlag(flag);
            drawRect(xPosition + 4, yPosition + 17, xPosition + 120, yPosition + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            //Draw hearts
            int hearts = (int)((((double)relationship)/MAX_FRIENDSHIP) * 9);
            mc.getTextureManager().bindTexture(HEARTS);
            for (int i = 0; i < 9; i++) {
                drawTexturedModalRect(xPosition + 24 + 10 * i, yPosition + 6, 16, 0, 9, 9);
                if (i < hearts) {
                    drawTexturedModalRect(xPosition + 24 + + 10 * i, yPosition + 6, 52, 0, 9, 9);
                }
            }
        }
    }

    private void drawForeground() {
        //Pregnant,Cleaned,Healthy,Treated,Petted,Eaten
        if (isPregnant) drawStack(true, MIRACLE, 65, -5);
        if (isSick) drawStack(true, SICK, 75, -5);
        if (needsCleaning) drawStack(isClean, HFAnimals.TOOLS.getStackFromEnum(Tool.BRUSH), 85, -5);
        drawStack(isTreated, HFAnimals.TREATS.getStackFromEnum(Treat.GENERIC), 95, -5);
        drawStack(petted, PETTED, 105, -5);
        drawStack(eaten, getFoodForAnimal(type), 115, -5);
        drawStack(collected, product, 114, 4, 0.75F);
        RenderHelper.drawStack(stack, xPosition + 4, yPosition + 1, 1F);
    }

    private void drawStack(boolean value, ItemStack icon, int x, int y) {
        drawStack(value, icon, x, y, 0.5F);
    }

    private void drawStack(boolean value, ItemStack icon, int x, int y, float scale) {
        if (!value) RenderHelper.drawGreyStack(icon, xPosition + x, yPosition + y, scale);
        else RenderHelper.drawStack(icon, xPosition + x, yPosition + y, scale);
    }

    private ItemStack getFoodForAnimal(IAnimalType type) {
        AnimalFoodType food = type.getFoodTypes()[0];
        switch (food) {
            case REDMEAT:
                return BEEF;
            case CHICKEN:
                return CHICKEN;
            case FISH:
                return FISH;
            case SEED:
                return SEED;
            case VEGETABLE:
                return VEGETABLE;
            case FRUIT:
                return FRUIT;
            case GRASS:
            default:
                return GRAIN;
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}
