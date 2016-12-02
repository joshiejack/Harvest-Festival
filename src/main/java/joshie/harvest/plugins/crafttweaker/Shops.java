package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
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

import static joshie.harvest.api.HFApi.shops;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.plugins.crafttweaker.BaseOnce.asStack;

@ZenClass("mods.harvestfestival.Shops")
public class Shops {
    @ZenMethod
    public static void addShopToNPC(String npc, String shop, String greeting, String openinghours, @Optional String hoursText) {
        MineTweakerAPI.apply(new AddShop(npc, shop, greeting, openinghours, hoursText));
    }

    private static class AddShop extends BaseUndoable {
        private final ResourceLocation resource;
        private final String name;
        private final String npc;
        private final String greeting;
        private final String openinghours;
        private IGreeting hours;
        private Shop shop;

        public AddShop(String npc, String shop, String greeting, String openinghours, @Nullable String hoursText) {
            this.resource = new ResourceLocation("MineTweaker3", shop.toLowerCase());
            this.name = shop;
            this.npc = npc;
            this.greeting = greeting;
            this.openinghours = openinghours;
            if (hoursText != null) {
                this.hours = new GreetingShopTime(hoursText);
            }
        }

        @Override
        public String getDescription() {
            return "Created the shop " + name;
        }

        @Override
        public void apply() {
            NPC theNPC = NPCRegistry.REGISTRY.getValue(new ResourceLocation(MODID, npc));
            if (theNPC.getShop() == null && !ShopRegistry.INSTANCE.shops.containsKey(resource)) {
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

                theNPC.setShop(shop);
                theNPC.setHasInfo(null, null);
                if (hours != null) {
                    theNPC.setHasInfo(HFNPCs.TOOLS.getStackFromEnum(NPCTool.CLOCK), hours);
                }

                ShopRegistry.INSTANCE.shops.put(resource, shop);
                String[] hours = openinghours.replace(" ", "").split(";");
                for (String time: hours) {
                    String[] split = time.split(",");
                    if (split.length == 3) {
                        Weekday day = Weekday.valueOf(split[0].toUpperCase(Locale.ENGLISH));
                        int start = Integer.parseInt(split[1]);
                        int end = Integer.parseInt(split[2]);
                        shop.addOpening(day, start, end);
                    }
                }
            }
        }

        @Override
        public boolean canUndo() {
            return shop != null;
        }

        @Override
        public void undo() {
            NPC theNPC = NPCRegistry.REGISTRY.getValue(new ResourceLocation(MODID, npc));
            theNPC.setShop(null);
            ShopRegistry.INSTANCE.shops.remove(resource);
        }
    }

    @ZenMethod
    public static void addPurchasable(String shop, IItemStack sellable, long cost) {
        MineTweakerAPI.apply(new AddPurchasable(shop, asStack(sellable), cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddPurchasable extends BaseUndoable {
        protected final IShop shop;
        protected final ItemStack stack;
        protected final long cost;
        protected IPurchasable purchasable;

        public AddPurchasable(String shop, ItemStack stack, long cost) {
            this.shop = shops.getShop(new ResourceLocation(shop));
            this.stack = stack;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adding " + stack.getDisplayName() + " to the Carpenter's shop.";
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
            ((Shop)shop).removeItem(purchasable);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addPurchasableToBuilder(IItemStack sellable, int wood, int stone, long cost) {
        MineTweakerAPI.apply(new AddBuilderPurchasable(asStack(sellable), wood, stone, cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddBuilderPurchasable extends AddPurchasable {
        protected final int stone;
        protected final int wood;

        public AddBuilderPurchasable(ItemStack stack, int stone, int wood, long cost) {
            super("harvestfestival:carpenter", stack, cost);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removePurchasable(String shop, IItemStack sellable) {
        MineTweakerAPI.apply(new RemovePurchasable(shop, asStack(sellable)));
    }

    @ZenMethod
    public static void removePurchasable(String shop, String id) {
        MineTweakerAPI.apply(new RemovePurchasable(shop, id));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class RemovePurchasable extends BaseUndoable {
        protected final IShop shop;
        protected ItemStack stack;
        protected String id;
        protected IPurchasable purchasable;

        public RemovePurchasable(String shop, ItemStack stack) {
            this.shop = shops.getShop(new ResourceLocation(shop));
            this.stack = stack;
        }

        public RemovePurchasable(String shop, String id) {
            this.shop = shops.getShop(new ResourceLocation(shop));
            this.id = id;
        }

        @Override
        public String getDescription() {
            if (stack != null) return "Removing " + stack.getDisplayName() + " from " + ((Shop)shop).getLocalizedName();
            else return "Removing " + id + " from " + ((Shop)shop).getLocalizedName();
        }

        @Override
        public void apply() {
            if (stack != null) purchasable = ((Shop)shop).removeItem(stack);
            else purchasable = ((Shop)shop).removeItem(id);
        }

        @Override
        public boolean canUndo() {
            return purchasable != null;
        }

        @Override
        public void undo() {
            shop.addItem(purchasable);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void adjustPurchasable(String shop, String id, long cost) {
        MineTweakerAPI.apply(new AdjustPurchasble(shop, id, cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AdjustPurchasble extends BaseUndoable {
        protected final IShop shop;
        protected final String id;
        protected final long cost;
        protected IPurchasable purchasable;
        protected PurchasableWrapper wrapper;

        public AdjustPurchasble(String shop, String id, long cost) {
            this.shop = shops.getShop(new ResourceLocation(shop));
            this.id = id;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adjusting value of  " + id;
        }

        @Override
        public void apply() {
            purchasable = ((Shop)shop).removeItem(id);
            wrapper = new PurchasableWrapper(purchasable, cost);
            shop.addItem(wrapper);
        }

        @Override
        public boolean canUndo() {
            return purchasable != null;
        }

        @Override
        public void undo() {
            ((Shop)shop).removeItem(wrapper);
            shop.addItem(purchasable);
        }
    }

    @ZenMethod
    public static void adjustCarpenter(String id, int logs, int stone, long cost) {
        MineTweakerAPI.apply(new AdjustCarpenter(id, logs, stone, cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AdjustCarpenter extends BaseUndoable {
        protected final IShop shop;
        protected final String id;
        protected final long cost;
        protected final int wood;
        protected final int stone;
        protected PurchasableBuilder purchasable;
        protected PurchasableWrapperCarpenter wrapper;

        public AdjustCarpenter(String id, int wood, int stone, long cost) {
            this.shop = HFShops.CARPENTER;
            this.id = id;
            this.wood = wood;
            this.stone = stone;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adjusting value of  " + id;
        }

        @Override
        public void apply() {
            purchasable = (PurchasableBuilder)((Shop)shop).removeItem(id);
            wrapper = new PurchasableWrapperCarpenter(purchasable, wood, stone, cost);
            shop.addItem(wrapper);
        }

        @Override
        public boolean canUndo() {
            return purchasable != null;
        }

        @Override
        public void undo() {
            ((Shop)shop).removeItem(wrapper);
            shop.addItem(purchasable);
        }
    }
}
