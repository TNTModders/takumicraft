package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityKingDummy extends EntityCreeper {
    public int id;

    public EntityKingDummy(World worldIn) {
        super(worldIn);
        this.setEntityInvulnerable(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > 1 && !this.isDead) {
            if (this.world.isRemote) {
                for (int i = 0; i < 10; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                            this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 2,
                            this.posY + this.rand.nextDouble() * this.height * 2,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 2, 0.0D, 0.0D, 0.0D);
                }
            }
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5f, true);
            }
            this.setDead();
        }
    }
}
