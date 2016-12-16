package joshie.harvest.plugins.crafttweaker.handler;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseUndoable;
import joshie.harvest.plugins.crafttweaker.wrapper.GreetingWrapper;
import joshie.harvest.plugins.crafttweaker.wrapper.PurchasableWrapper;
import joshie.harvest.plugins.crafttweaker.wrapper.PurchasableWrapperCarpenter;
import joshie.harvest.shops.HFShops;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.ShopRegistry;
import joshie.harvest.shops.purchasable.Purchasable;
import joshie.harvest.shops.purchasable.PurchasableBuilder;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.plugins.crafttweaker.base.BaseOnce.asStack;

@ZenClass("mods.harvestfestival.Shops")
public class Shops {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////Creating Shops////////////////////////////////////////////////////////////////////////////////////////////////////                                                                                             ///////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void addShopToNPC(String npc, String shop, String greeting, String openinghours, @Optional String hoursText) {
        NPC theNPC = NPCRegistry.REGISTRY.getValue(new ResourceLocation(MODID, npc));
        if (theNPC == null) CraftTweaker.logError(String.format("No NPC with the id %s could be found. Use /hf npcs for a list of ids", npc));
        else if (theNPC.getShop() != null) CraftTweaker.logError(String.format("Attempted to add a shop to %s when they already have a shop", theNPC.getLocalizedName()));
        else if (ShopRegistry.INSTANCE.shops.containsKey(new ResourceLocation("MineTweaker3", shop.toLowerCase()))) CraftTweaker.logError(String.format("Attempted to add a shop with a duplicate id: %s", "MineTweaker3:" + shop.toLowerCase()));
        else MineTweakerAPI.apply(new AddShop(theNPC, shop, greeting, openinghours, hoursText));
    }

    private static class AddShop extends BaseUndoable {
        private final ResourceLocation resource;
        private final String name;
        private final NPC npc;
        private final String greeting;
        private final String openinghours;
        private IGreeting hours;
        private Shop shop;

        AddShop(NPC npc, String shop, String greeting, String openinghours, @Nullable String hoursText) {
            this.resource = new ResourceLocation("MineTweaker3", shop.toLowerCase());
            this.name = shop;
            this.npc = npc;
            this.greeting = greeting;
            this.openinghours = openinghours;
            if (hoursText != null) this.hours = new GreetingWrapper(hoursText);
        }

        @Override
        public String getDescription() {
            return "Created the shop " + name;
        }

        private void processTimeString(String time) {
            String[] split = time.split(",");
            if (split.length == 3) {
                Weekday day = Weekday.valueOf(split[0].toUpperCase(Locale.ENGLISH));
                int start = Integer.parseInt(split[1]);
                int end = Integer.parseInt(split[2]);
                shop.addOpening(day, start, end);
            }
        }

        @Override
        public void apply() {
            shop = new Shop(resource) {
                @Override
                public String getLocalizedName() {
                    return name;
                }

                @Override
                public String getWelcome(EntityPlayer player, EntityNPC npc) {
                    return greeting;
                }
            };

            npc.setShop(shop);
            npc.setHasInfo(null, null);
            if (hours != null) npc.setHasInfo(HFNPCs.TOOLS.getStackFromEnum(NPCTool.CLOCK), hours);
            ShopRegistry.INSTANCE.shops.put(resource, shop);
            String[] hours = openinghours.replace(" ", "").split(";");
            for (String time: hours) processTimeString(time);
        }

        @Override
        public boolean canUndo() {
            return shop != null;
        }

        @Override
        public void undo() {
            npc.setShop(null);
            npc.setHasInfo(null, null);
            ShopRegistry.INSTANCE.shops.remove(resource);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////Adding Items//////////////////////////////////////////////////////////////////////////////////////////////////////                                                                                             ///////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void addPurchasable(String shop, IItemStack sellable, long cost) {
        Shop theShop = ShopRegistry.INSTANCE.getShop(new ResourceLocation(shop));
        if (theShop == null) CraftTweaker.logError(String.format("No shop with the id %s could be found. Use /hf shops for a list of ids", shop));
        MineTweakerAPI.apply(new AddPurchasable(theShop, asStack(sellable), cost));
    }

    private static class AddPurchasable extends BaseUndoable {
        protected final Shop shop;
        protected final ItemStack stack;
        protected final long cost;
        protected IPurchasable purchasable;

        AddPurchasable(Shop shop, ItemStack stack, long cost) {
            this.shop = shop;
            this.stack = stack;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adding " + stack.getDisplayName() + " to " + shop.getLocalizedName();
        }

        @Override
        public void apply() {
            purchasable = new Purchasable(cost, stack);
            shop.addItem(purchasable);
        }

        @Override
        public boolean canUndo() {
            return purchasable != null;
        }

        @Override
        public void undo() {
            shop.removeItem(purchasable);
        }
    }

    @ZenMethod
    @SuppressWarnings("unused")
    public static void addPurchasableToBuilder(IItemStack sellable, int wood, int stone, long cost) {
        MineTweakerAPI.apply(new AddBuilderPurchasable(asStack(sellable), wood, stone, cost));
    }

    private static class AddBuilderPurchasable extends AddPurchasable {
        protected final int stone;
        protected final int wood;

        AddBuilderPurchasable(ItemStack stack, int stone, int wood, long cost) {
            super((Shop) HFShops.CARPENTER, stack, cost);
            this.stone = stone;
            this.wood = wood;
        }

        @Override
        public String getDescription() {
            return "Adding " + stack.getDisplayName() + " to the Carpenter's shop.";
        }

        @Override
        public void apply() {
            purchasable = new PurchasableBuilder(cost, stone, wood, stack);
            shop.addItem(purchasable);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////Removing Items////////////////////////////////////////////////////////////////////////////////////////////////////                                                                                             ///////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    @SuppressWarnings("unused")
    public static void removePurchasable(String shop, String id) {
        Shop theShop = ShopRegistry.INSTANCE.getShop(new ResourceLocation(shop));
        if (theShop == null) CraftTweaker.logError(String.format("No shop with the id %s could be found. Use /hf shops for a list of ids", shop));
        else if (theShop.getPurchasableFromID(id) == null) CraftTweaker.logError(String.format("No purchasable with the id %s could be found in " + theShop.getLocalizedName(), id));
        else MineTweakerAPI.apply(new RemovePurchasable(theShop, theShop.getPurchasableFromID(id)));
    }

    private static class RemovePurchasable extends BaseUndoable {
        protected final Shop shop;
        protected final IPurchasable purchasable;

        RemovePurchasable(Shop shop, IPurchasable purchasable) {
            this.shop = shop;
            this.purchasable = purchasable;
        }

        @Override
        public String getDescription() {
            return "Removing " + purchasable.getDisplayStack().getDisplayName() + " from " + shop.getLocalizedName();
        }

        @Override
        public void apply() {
            shop.removeItem(purchasable);
        }

        @Override
        public void undo() {
            shop.addItem(purchasable);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////Adjusting Items///////////////////////////////////////////////////////////////////////////////////////////////////                                                                                             ///////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void adjustPurchasable(String shop, String id, long cost) {
        Shop theShop = ShopRegistry.INSTANCE.getShop(new ResourceLocation(shop));
        if (theShop == null) CraftTweaker.logError(String.format("No shop with the id %s could be found. Use /hf shoplist for a list of ids", shop));
        else if (theShop.getPurchasableFromID(id) == null) CraftTweaker.logError(String.format("No purchasable with the id %s could be found in " + theShop.getLocalizedName(), id));
        else MineTweakerAPI.apply(new AdjustPurchasable(theShop, theShop.getPurchasableFromID(id), cost));
    }

    private static class AdjustPurchasable extends BaseUndoable {
        protected final Shop shop;
        protected final IPurchasable purchasable;
        protected final long cost;
        IPurchasable wrapper;

        @SuppressWarnings("unchecked")
        AdjustPurchasable(Shop shop, IPurchasable purchasable, long cost) {
            this.shop = shop;
            this.purchasable = purchasable;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adjusting value of  " + purchasable.getDisplayStack().getDisplayName();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void apply() {
            shop.removeItem(purchasable); //Remove the item
            wrapper = new PurchasableWrapper(purchasable, cost);
            shop.addItem(wrapper);
        }

        @Override
        public boolean canUndo() {
            return wrapper != null;
        }

        @Override
        public void undo() {
            shop.removeItem(wrapper);
            shop.addItem(purchasable);
        }
    }

    @ZenMethod
    @SuppressWarnings("unused")
    @Deprecated //TODO: Remove in 0.7+
    public static void adjustCarpenter(String id, int logs, int stone, long cost) {
        Shop shop = (Shop) HFShops.CARPENTER;
        if (shop.getPurchasableFromID(id) == null) CraftTweaker.logError(String.format("No purchasable with the id %s could be found in " + HFShops.CARPENTER, id));
        else if (!(shop.getPurchasableFromID(id) instanceof PurchasableBuilder)) CraftTweaker.logError(String.format("The item %s did not originaly accept materials, you cannot adjust the values", id));
        else MineTweakerAPI.apply(new AdjustCarpenter((PurchasableBuilder)shop.getPurchasableFromID(id), cost, logs, stone));
    }

    private static class AdjustCarpenter extends AdjustPurchasable {
        protected final int wood;
        protected final int stone;
        protected PurchasableBuilder purchasable;
        PurchasableWrapperCarpenter wrapper;

        @SuppressWarnings("unchecked")
        AdjustCarpenter(PurchasableBuilder purchasable, long cost, int wood, int stone) {
            super((Shop) HFShops.CARPENTER, purchasable, cost);
            this.purchasable = purchasable;
            this.wood = wood;
            this.stone = stone;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void apply() {
            shop.removeItem(purchasable); //Remove the item
            wrapper = new PurchasableWrapperCarpenter(purchasable, wood, stone, cost);
            shop.addItem(wrapper);
        }
    }
}
