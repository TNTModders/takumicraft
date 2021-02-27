package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderSmokeCreeper;
import com.tntmodders.takumi.entity.item.EntitySmokeParticle;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntitySmokeCreeper extends EntityEPCreeper {
    public boolean isGlowingSP = true;

    public EntitySmokeCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
    }

    @Override
    public void takumiExplode() {
        TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, this.getPowered() ? 4 : 2, false, true);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public String getRegisterName() {
        return "smokecreeper";
    }

    @Override
    public int getRegisterID() {
        return 309;
    }

    @Override
    public int getPrimaryColor() {
        return 0x337733;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setGlowing(this.isGlowingSP);
        if (!this.world.isRemote && this.ticksExisted % 10 == 0 && this.isEntityAlive()) {
            EntitySmokeParticle smokeParticle = new EntitySmokeParticle(this.world, this.posX + this.rand.nextDouble() * 2 - 1, this.posY + this.rand.nextDouble() * 2,
                    this.posZ + this.rand.nextDouble() * 2 - 1);
            smokeParticle.smokeInit(this);
            this.world.spawnEntity(smokeParticle);
        }
    }

    @Override
    public boolean canBeHitWithPotion() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSmokeCreeper<>(manager);
    }
}
