package joshie.harvest.api.animals;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.render.ModelHarvestCow;
import joshie.harvest.animals.render.ModelHarvestSheep;
import joshie.harvest.animals.render.RenderHarvestAnimal;
import joshie.harvest.animals.type.AnimalChicken;
import joshie.harvest.animals.type.AnimalCow;
import joshie.harvest.animals.type.AnimalSheep;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;
import joshie.harvest.items.HFItems;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HFAnimals {
    public static void preInit() {
        EntityRegistry.registerModEntity(EntityHarvestCow.class, "HarvestCow", 5, HarvestFestival.instance, 150, 3, true);
        EntityRegistry.registerModEntity(EntityHarvestSheep.class, "HarvestSheep", 6, HarvestFestival.instance, 150, 3, true);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.CHICKEN, Items.chicken, Items.cooked_chicken);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.FISH, Items.fish, Items.cooked_fished);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.FRUIT, Items.apple, Items.melon);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.GRASS, Items.wheat);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.REDMEAT, Items.porkchop, Items.beef, Items.cooked_porkchop, Items.cooked_beef);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.SEED, Items.melon_seeds, Items.wheat_seeds, Items.pumpkin_seeds);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.VEGETABLE, Items.carrot);
        HFApi.ANIMALS.registerFoodAsType(new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED), AnimalFoodType.SEED);
        HFApi.ANIMALS.registerType("cow", new AnimalCow());
        HFApi.ANIMALS.registerType("sheep", new AnimalSheep());
        HFApi.ANIMALS.registerType("chicken", new AnimalChicken());
    }

    public static void init() {
        for (ICrop crop : Crop.crops) {
            HFApi.ANIMALS.registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestCow.class, new RenderHarvestAnimal(new ModelHarvestCow(), "cow"));
        RenderingRegistry.registerEntityRenderingHandler(EntityHarvestSheep.class, new RenderHarvestAnimal(new ModelHarvestSheep(), "sheep"));
    }
}
