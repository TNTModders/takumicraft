package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.item.EntityTakumiTNTPrimed;
import com.tntmodders.takumi.entity.mobs.EntityGateCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityGateCreeper_Evo extends EntityGateCreeper {
    public EntityGateCreeper_Evo(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return super.getRegisterName() + "_evo";
    }

    @Override
    public int getRegisterID() {
        return 297;
    }

    @Override
    public boolean isEvo() {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        this.attackEntityFrom(DamageSource.causeMobDamage(this), 2f);
        for (int i = 0; i < (this.getPowered() ? 2 : 1); i++) {
            EntityTakumiTNTPrimed tntPrimed = new EntityTakumiTNTPrimed(this.world);
            tntPrimed.setPosition(this.posX + MathHelper.nextFloat(this.rand, -3, 3), this.posY + 2,
                    this.posZ + MathHelper.nextFloat(this.rand, -3, 3));
            double d0 = target.posX - this.posX;
            double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - tntPrimed.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            this.setThrowableHeading(tntPrimed, d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
                    (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
            this.world.spawnEntity(tntPrimed);
        }
    }
}
