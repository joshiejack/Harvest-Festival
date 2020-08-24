package joshie.harvest.knowledge.gui.stats.relations.button;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.item.ItemCrop.Crops;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ButtonRelationsAnimal extends ButtonBook<GuiStats> {
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
    private final NonNullList<ItemStack> products;
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
    @Nonnull
    private final ItemStack stack;

    private final boolean doProductTick;
    private ItemStack product;
    private int productTick;
    private int productID;

    public ButtonRelationsAnimal(GuiStats gui, EntityAnimal animal, AnimalStats stats, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, animal.getName());
        this.gui = gui;
        this.width = 120;
        this.height = 16;
        this.stack = stats.getType().getIcon();
        this.type = stats.getType();
        this.relationship = stats.getHappiness();
        this.petted = stats.performTest(AnimalTest.BEEN_LOVED);
        this.eaten = stats.performTest(AnimalTest.HAS_EATEN);
        this.needsCleaning = stats.performTest(AnimalTest.CAN_CLEAN);
        this.isClean = stats.performTest(AnimalTest.IS_CLEAN);
        this.isSick = stats.performTest(AnimalTest.IS_SICK);
        this.isPregnant = stats.performTest(AnimalTest.IS_PREGNANT);
        this.isTreated = stats.performTest(AnimalTest.HAD_TREAT);
        this.products =  stats.getType().getProductsForDisplay(stats);
        this.product = products.get(0);
        this.doProductTick = products.size() > 0;
        this.collected = !stats.canProduce();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mouseDragged(mc, mouseX, mouseY);
            drawForeground();
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawString(TextFormatting.BOLD + displayString, x + 4, y - 7, 0x857754);
            mc.fontRenderer.setUnicodeFlag(flag);
            drawRect(x + 4, y + 17, x + 120, y + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            //Draw hearts
            int hearts = (int)((((double)relationship)/ RelationshipType.ANIMAL.getMaximumRP()) * 9);
            mc.getTextureManager().bindTexture(HEARTS);
            for (int i = 0; i < 9; i++) {
                drawTexturedModalRect(x + 24 + 10 * i, y + 6, 16, 0, 9, 9);
                if (i < hearts) {
                    drawTexturedModalRect(x + 24 + + 10 * i, y + 6, 52, 0, 9, 9);
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
        if (doProductTick) {
            productTick++;
            if (productTick == 1 || productTick % 300 == 0) {
                product = products.get(productID);
                productID++; //Increase the id
                if (productID >= products.size()) {
                    productID = 0;
                }
            }
        }

        drawStack(collected, product, 114, 4, 0.75F);
        StackRenderHelper.drawStack(stack, x + 4, y + 1, 1F);
    }

    private void drawStack(boolean value, @Nonnull ItemStack icon, int x, int y) {
        drawStack(value, icon, x, y, 0.5F);
    }

    private void drawStack(boolean value, @Nonnull ItemStack icon, int posX, int posY, float scale) {
        if (!value) StackRenderHelper.drawGreyStack(icon, x + posX, y + posY, scale);
        else StackRenderHelper.drawStack(icon, x + posX, y + posY, scale);
    }

    @Nonnull
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
