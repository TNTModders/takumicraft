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
import net.minecraft.world.DimensionType;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntityRushCreeper extends EntityTakumiAbstractCreeper {

    public EntityRushCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i < (this.getPowered() ? 5 : 3); i++) {
                this.world.createExplosion(this, this.posX, this.posY - 0.25, this.posZ, this.getPowered() ? 11 : 6,
                        true);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_D;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "rushcreeper";
    }

    @Override
    public int getRegisterID() {
        return 228;
    }

    @Override
    public void additionalSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 1, 5, EnumCreatureType.MONSTER,
                Biomes.HELL);
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

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && (this.world.provider.getDimensionType() != DimensionType.OVERWORLD || this.rand.nextInt(5) == 0);
    }
}
