package joshie.harvest.npc.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.town.TownHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemNPCSpawner extends ItemHFFML<ItemNPCSpawner, NPC> {
    public ItemNPCSpawner() {
        super(NPCRegistry.REGISTRY, HFTab.TOWN);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName();
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        NPC npc = getObjectFromStack(stack);
        if (npc != null) {
            if (!world.isRemote) {
                if ((npc == HFNPCs.BUILDER && TownHelper.ensureTownExists(world, pos)) || npc != HFNPCs.BUILDER) {
                    EntityNPC entity = NPCHelper.getEntityForNPC(world, npc);
                    entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    entity.resetSpawnHome();
                    world.spawnEntityInWorld(entity);
                }
            }

            stack.splitStack(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public NPC getNullValue() {
        return HFNPCs.NULL_NPC;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (NPC npc: registry) {
            ModelLoader.setCustomModelResourceLocation(item, registry.getValues().indexOf(npc), new ModelResourceLocation(new ResourceLocation(MODID, "spawner_npc"), "inventory"));
        }
    }
}