package cz.ondraster.bettersleeping.api;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldSleepEvent extends WorldEvent {
   public WorldSleepEvent(World world) {
      super(world);
   }

   /**
    * This even is fired before any progress towards calculating new TOD or anything is run.
    * This event is NOT cancelable.
    */
   public static class Pre extends WorldSleepEvent {
      public Pre(World world) {
         super(world);
      }
   }

   /**
    * This even is fired after all of the calculating new TOD, players have been awoken etc has been done.
    * This event is NOT cancelable.
    */
   public static class Post extends WorldSleepEvent {
      private final long sleptTicks;

      public Post(World world, long sleptTicks) {
         super(world);
         this.sleptTicks = sleptTicks;
      }

      public long getSleptTicks() {
         return sleptTicks;
      }
   }

   /**
    * This event is fired before a person is supposed to fall on a ground outside of bed - when he reaches 0 on his Sleepybar.
    * This event is cancelable.
    * This event is NOT fired when sleeping on ground is disabled.
    */
   @Cancelable
   public static class SleepOnGround extends WorldSleepEvent {
      private EntityPlayer player;

      public SleepOnGround(EntityPlayer player) {
         super(player.worldObj);
         this.player = player;
      }

      public EntityPlayer getPlayer() {
         return player;
      }
   }
}
