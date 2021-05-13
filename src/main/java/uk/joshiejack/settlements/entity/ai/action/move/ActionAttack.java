package uk.joshiejack.settlements.entity.ai.action.move;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

@PenguinLoader("attack")
public class ActionAttack extends ActionPhysical {
    private EntityAIAttackMelee ai;
    private int delayCounter;
    private int attackTick;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double speed;

    public ActionAttack withData(Object... params) {
        this.speed = (double) params[0];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC attacker) {
        if (ai == null) {
            ai = new EntityAIAttackMelee(attacker, 1.0D, false);
        }

        if (player != null) {
            attacker.setAttackTarget(player);
            attacker.getLookHelper().setLookPositionWithEntity(player, 30.0F, 30.0F);
            double d0 = attacker.getDistanceSq(player.posX, player.getEntityBoundingBox().minY, player.posZ);
            --delayCounter;

            if (attacker.getEntitySenses().canSee(player) && delayCounter <= 0 && (targetX == 0.0D && targetY == 0.0D && targetZ == 0.0D || player.getDistanceSq(targetX, targetY, targetZ) >= 1.0D || attacker.getRNG().nextFloat() < 0.05F)) {
                targetX = player.posX;
                targetY = player.getEntityBoundingBox().minY;
                targetZ = player.posZ;
                delayCounter = 4 + attacker.getRNG().nextInt(7);

                if (d0 > 1024.0D) {
                    delayCounter += 10;
                } else if (d0 > 256.0D) {
                    delayCounter += 5;
                }

                if (!attacker.getNavigator().tryMoveToEntityLiving(player, speed)) {
                    delayCounter += 15;
                }
            }

            attackTick = Math.max(attackTick - 1, 0);
            checkAndPerformAttack(attacker, player, d0);
            return EnumActionResult.PASS;
        }

        return EnumActionResult.SUCCESS;
    }

    private void checkAndPerformAttack(EntityNPC attacker, EntityLivingBase enemy, double distToEnemySqr) {
        double d0 = getAttackReachSqr(attacker, enemy);

        if (distToEnemySqr <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            attacker.swingArm(EnumHand.MAIN_HAND);
            attacker.attackEntityAsMob(enemy);
        }
    }

    protected double getAttackReachSqr(EntityNPC attacker, EntityLivingBase attackTarget) {
        return attacker.width * 2.0F * attacker.width * 2.0F + attackTarget.width;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("Speed", speed);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        speed = nbt.getDouble("Speed");
    }
}
