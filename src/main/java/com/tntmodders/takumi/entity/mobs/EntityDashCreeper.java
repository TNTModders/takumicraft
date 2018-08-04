package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Biomes;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntityDashCreeper extends EntityTakumiAbstractCreeper {

    private int ticksSprint;

    public EntityDashCreeper(World worldIn) {
        super(worldIn);
    }


    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_D;
    }

    @Override
    public int getExplosionPower() {
        return 2;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "dashcreeper";
    }

    @Override
    public int getRegisterID() {
        return 264;
    }

    @Override
    public void additionalSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 1, 5, EnumCreatureType.MONSTER,
                Biomes.HELL);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaa0000;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isExplosion() && !damageSrc.isFireDamage() && !damageSrc.isMagicDamage() &&
                damageSrc != DamageSource.FALL && damageSrc != DamageSource.IN_WALL &&
                damageSrc != DamageSource.DROWN) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCreeperState() > 0) {
            if (this.getAttackTarget() != null) {
                this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 1f, 1f);
            }
            int i = 5;
            this.moveHelper.setMoveTo(this.posX + this.getLookVec().x * i, this.posY + this.getLookVec().y * i,
                    this.posZ + this.getLookVec().z * i, 1.5);
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 4f : 2f), true);
            }
            this.ticksSprint++;
        }
        if (this.ticksSprint > 30) {
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5f, true);
            }
            this.setDead();
        }
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f :
                super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) / 10;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1f;
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
}
