package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChaseCreeper extends EntityTakumiAbstractCreeper {

    public EntityChaseCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB);
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 1 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityChaseCreeper chaseCreeper = new EntityChaseCreeper(this.world);
                chaseCreeper.copyLocationAndAnglesFrom(this);
                chaseCreeper.setHealth(this.getHealth() - 1);
                chaseCreeper.setAttackTarget(this.getAttackTarget());
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(chaseCreeper, true);
                }
                this.world.spawnEntity(chaseCreeper);
            }
        }
        super.setDead();
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ,
                    this.getPowered() ? 20 : 10, false, false, this.getPowered() ? 7 : 3);
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().clear();
        List<Entity> list = event.getAffectedEntities();
        list.removeIf(entity -> !(entity instanceof EntityLivingBase));
        if (list.size() >= 1) {
            EntityLivingBase base = ((EntityLivingBase) list.get(this.rand.nextInt(list.size())));
            this.setAttackTarget(base);
        }
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() != null) {
            this.moveHelper.setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.5);
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.getPowered() && !this.isPotionActive(MobEffects.SPEED)) {
            this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 3, true, false));
        }
        if (this.world.isRemote) {
            for (int i = 0; i < 5; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaa0000;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "chasecreeper";
    }

    @Override
    public int getRegisterID() {
        return 289;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ffaa;
    }
}
