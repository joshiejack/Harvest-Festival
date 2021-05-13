package uk.joshiejack.husbandry.client.gui.labels;

import uk.joshiejack.husbandry.animals.AnimalType;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.data.TraitCleanable;
import uk.joshiejack.husbandry.animals.traits.data.TraitMammal;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.husbandry.item.ItemTool;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class LabelStatsDisplay extends LabelBook {
    private static final ItemStack PETTED = PenguinItems.SPECIAL.getStackFromEnum(ItemSpecial.Special.SPEECH_BUBBLE);
    private static final ItemStack MIRACLE = HusbandryItems.TOOL.getStackFromEnum(ItemTool.Tool.MIRACLE_POTION);
    private static final ItemStack BRUSH = HusbandryItems.TOOL.getStackFromEnum(ItemTool.Tool.BRUSH);
    private static final ItemStack HAY = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.FODDER);
    private static final ItemStack SLOP = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.SLOP);
    private static final ItemStack RABBIT = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.RABBIT_FOOD);
    private static final ItemStack CAT = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.CAT_FOOD);
    private static final ItemStack DOG = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.DOG_FOOD);
    private static final ItemStack BIRD = HusbandryItems.FEED.getStackFromEnum(ItemFeed.Feed.BIRD_FEED);
    private final EntityLiving entity;
    private final String name;
    private final int hearts;
    private final boolean isPregnant;
    private final boolean needsCleaning;
    private final boolean isClean;
    private final boolean isTreated;
    private final boolean petted;
    private final boolean eaten;
    private final boolean canProduce;
    private final ItemStack treat;
    private final ItemStack food;
    private final NonNullList<ItemStack> products;
    private ItemStack product;
    private final boolean doCycle;
    private boolean line = true;

    public LabelStatsDisplay(GuiBook gui, EntityLiving entity, AnimalStats<?> stats, int x, int y) {
        super(gui, x, y);
        this.width = 120;
        this.height = 16;
        this.entity = entity;
        this.name = entity.getName();
        this.hearts = stats.getHearts();
        this.isPregnant = stats.hasTrait("mammal") && ((TraitMammal)stats.getTrait("mammal")).isPregnant();
        this.needsCleaning = stats.hasTrait("cleanable");
        this.isClean = needsCleaning && ((TraitCleanable)stats.getTrait("cleanable")).isClean();
        this.isTreated = stats.hasBeenTreated();
        this.treat = stats.getType().getTreat();
        this.petted = stats.hasBeenLoved();
        this.eaten = stats.hasEaten();
        this.canProduce = stats.canProduceProduct();
        this.products = NonNullList.create();
        if (hearts >= stats.getMinLarge()) products.add(stats.getType().getProducts().getProduct(2).copy());
        if (hearts >= stats.getMinMedium()) products.add(stats.getType().getProducts().getProduct(1).copy());
        products.add(stats.getType().getProducts().getProduct(0).copy());
        this.product = products.get(0);
        this.doCycle = products.size() > 1;
        AnimalType type = stats.getType();
        this.food = type.hasTrait("eats_hay") ? HAY : type.hasTrait("eats_bird_feed")? BIRD : type.hasTrait("eats_cat_food")
                ? CAT : type.hasTrait("eats_dog_food") ? DOG: type.hasTrait("eats_rabbit_food") ? RABBIT : SLOP;
    }

    @SuppressWarnings("unused")
    public LabelStatsDisplay setNoLine() {
        this.line = false;
        return this;
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawStats();

            if (line) {
                drawRect(x + 4, y + 17, x + 120, y + 18, 0xFF857754);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F);

            //Draw hearts
            mc.getTextureManager().bindTexture(GuiElements.ICONS);
            for (int i = 0; i < 9; i++) {
                drawTexturedModalRect(x + 24 + 10 * i, y + 6, 16, 0, 9, 9);
                if (i < hearts) {
                    drawTexturedModalRect(x + 24 + + 10 * i, y + 6, 52, 0, 9, 9);
                }
            }
        }
//
        //
        GuiInventory.drawEntityOnScreen(x + 10, y + 16, 10, -180, 0, entity);
        GlStateManager.disableDepth();
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + name, x + 4, y - 7, 0xFFFFFF);
        mc.fontRenderer.setUnicodeFlag(flag);
        GlStateManager.enableDepth();
    }

    private int cycle;
    private int id;

    private void drawStats() {
    //Pregnant,Cleaned,Healthy,Treated,Petted,Eaten
        if (isPregnant) drawStack(MIRACLE, 75, -5, 0.5F);
        if (needsCleaning) drawStack(isClean, BRUSH, 85, -5, 0.5F);
        drawStack(isTreated, treat, 95, -5, 0.5F);
        drawStack(petted, PETTED, 105, -5, 0.5F);
        drawStack(eaten, food, 115, -5, 0.5F);
        if (doCycle) {
            cycle++;
            if (cycle %300 == 0) {
                product = products.get(id);
                id++;
                if (id >= products.size()) {
                    id = 0; //Reset to 0;
                }
            }
        }

        drawStack(!canProduce, product, 114, 4, 0.75F);
        //StackRenderHelper.drawStack(stack, xPosition + 4, yPosition + 1, 1F);
    }
}
