package joshie.harvestmoon.items;

import joshie.harvestmoon.handlers.BuildingCodeGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCheat extends ItemHMMeta {
    private static final int COORD_SETTER = 0;
    private static final int CODE_GENERATOR = 1;
    private static int x1, x2, y1, y2, z1, z2;

    @Override
    public int getMetaCount() {
        return 2;
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        int damage = stack.getItemDamage();
        if(damage == COORD_SETTER) {
            if(player.isSneaking()) {
                x2 = xCoord;
                y2 = yCoord;
                z2 = zCoord;
            } else {
                x1 = xCoord;
                y1 = yCoord;
                z1 = zCoord;
            }
        } else if (damage == CODE_GENERATOR) {
            new BuildingCodeGenerator(world, x1, y1, z1, x2, y2, z2).getCode(player.isSneaking());
        }
        
        return false;
    }
    
    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case COORD_SETTER:
                return "coord_setter";
            case CODE_GENERATOR:
                return "code_generator";
            default:
                return "invalid";
        }
    }
}
