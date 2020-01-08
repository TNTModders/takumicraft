package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.client.render.RenderParalysisCreeper;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class EntityParalysisCreeper extends EntityTakumiAbstractCreeper {

    public EntityParalysisCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() == null) {
            if (this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 10, false) != null) {
                this.setAttackTarget(this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 10, false));
            }
        }
        if (this.getAttackTarget() != null) {
            if (this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).isCreative() && !this.hasIgnited()) {
                this.setCreeperState(-1);
            }
            if (this.getCreeperState() > 0) {
                this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 50, 50);
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 100));
            }
        }
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 7;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0f0f;
    }

    @Override
    public int getPrimaryColor() {
        return 0x220000;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "paralysiscreeper";
    }

    @Override
    public int getRegisterID() {
        return 411;
    }

    @Override
    public boolean canRegister() {
        return TakumiConfigCore.inDev;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderParalysisCreeper(manager);
    }
}
