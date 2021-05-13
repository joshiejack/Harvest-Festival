package uk.joshiejack.economy.api.shops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShopTarget {
    public World world; //The world that this target is in
    public BlockPos pos; //The position of this target
    public Entity entity; //The entity associated with this target, EITHER the actual entity OR the player if it's an item or blockstate
    public EntityPlayer player; //The player entity that was interacting with this target
    public ItemStack stack; //The stack that is interacting, whether it's relevant or not
    public ShopInput input; //The input handler, for special situations

    public ShopTarget() {}
    public ShopTarget(World world, BlockPos pos, Entity entity, EntityPlayer player, ItemStack stack, ShopInput input) {
        this.world = world;
        this.pos = pos;
        this.entity = entity;
        this.player = player;
        this.stack = stack;
        this.input = input;
    }

    public ShopTarget asPlayer() {
        return new ShopTarget(world, pos, player, player, stack, input);
    }
}
