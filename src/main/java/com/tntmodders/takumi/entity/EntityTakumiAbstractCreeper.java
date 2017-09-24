package com.tntmodders.takumi.entity;

import com.tntmodders.takumi.client.render.RenderTakumiCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityTakumiAbstractCreeper extends EntityCreeper implements ITakumiEntity {
    public EntityTakumiAbstractCreeper(World worldIn) {
        super(worldIn);
        this.experienceValue = this.takumiRank().getExperiment();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.LIGHTNING_BOLT) {
            return;
        }
       /* if ((this.takumiRank().getLevel() > 2 && damageSrc.isExplosion()) ||
                (damageSrc.isFireDamage() && this.takumiType() == EnumTakumiType.FIRE) ||
                (damageSrc == DamageSource.DROWN && this.takumiType() == EnumTakumiType.WATER) ||
                (damageSrc == DamageSource.FALL && this.takumiType() == EnumTakumiType.WIND)) {
            return;
        }*/
        super.damageEntity(damageSrc, damageAmount);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        return true;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    public int getPrimaryColor() {
        return 39168;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public RenderLiving getRender(RenderManager manager) {
        return new RenderTakumiCreeper<>(manager);
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    public double getSizeAmp() {
        return 1;
    }
}
