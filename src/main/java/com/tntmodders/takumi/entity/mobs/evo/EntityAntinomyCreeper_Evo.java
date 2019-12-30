package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.mobs.EntityAntinomyCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAntinomyCreeper_Evo extends EntityAntinomyCreeper {
    public EntityAntinomyCreeper_Evo(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getExplosionPower() {
        return super.getExplosionPower() * 3;
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
        return 296;
    }

    @Override
    public boolean isEvo() {
        return true;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 100 : 50;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, i / 7, false);
            for (EntityLivingBase entityLivingBase : this.world.getEntities(EntityLivingBase.class,
                    input -> EntityAntinomyCreeper_Evo.this.getDistanceSqToEntity(input) < i * i)) {
                this.buildIce(entityLivingBase.getPosition());
                for (int i1 = 0; i1 < (this.getPowered() ? 5 : 2); i1++) {
                    this.buildIce(
                            entityLivingBase.getPosition().add(this.rand.nextInt(7) - 3, 0, this.rand.nextInt(7) - 3));
                }
            }
            this.buildFire((int) this.posX, (int) this.posY, (int) this.posZ);
        }
    }
}
