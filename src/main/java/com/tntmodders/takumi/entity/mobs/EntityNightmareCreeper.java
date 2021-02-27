package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderNightmareCreeper;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityNightmareCreeper extends EntityTakumiAbstractCreeper {
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;

    public EntityNightmareCreeper(World worldIn) {
        super(worldIn);
        this.clientSideTailAnimation = this.rand.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x223322;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "nightmarecreeper";
    }

    @Override
    public int getRegisterID() {
        return 92;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.NIGHTMARE, 200, 0, true, false));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0, true, false));
                }
            });
            event.getAffectedEntities().clear();
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xff00ff;
    }

    @SideOnly(Side.CLIENT)
    public float getSpikesAnimation(float p_175469_1_) {
        return this.clientSideSpikesAnimationO +
                (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * p_175469_1_;
    }

    @SideOnly(Side.CLIENT)
    public float getTailAnimation(float p_175471_1_) {
        return this.clientSideTailAnimationO +
                (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.clientSideTailAnimationO = this.clientSideTailAnimation;
            this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
            this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
            this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
        }
        super.onLivingUpdate();
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderNightmareCreeper<>(manager);
    }
}
