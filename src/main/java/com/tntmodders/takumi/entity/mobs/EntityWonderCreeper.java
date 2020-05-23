package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.lang.reflect.Field;

public class EntityWonderCreeper extends EntityTakumiAbstractCreeper {

    private int wonderSinceIgnited;

    public EntityWonderCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.isImmuneToFire = true;
        this.experienceValue = 50;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.setFire(10);
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * 0.5 * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                if (this.rand.nextBoolean()) {
                    this.world.spawnParticle(EnumParticleTypes.FLAME,
                            this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                            this.posY + this.rand.nextDouble() * 0.5 * (double) this.height + 1,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        super.onLivingUpdate();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setGlowing(true);
        int i = Math.max(this.getCreeperState(), 0);
        this.wonderSinceIgnited += i;
        if (!this.world.isRemote) {
            if (this.wonderSinceIgnited == 50) {
                for (int t = 0; t < 10; t++) {
                    double x = this.posX + Math.cos((this.rotationYawHead + 90) * Math.PI / 180) * t * 4;
                    double z = this.posZ + Math.sin((this.rotationYawHead + 90) * Math.PI / 180) * t * 4;
                    EntityLightningBolt bolt = new EntityLightningBolt(this.world, x, this.posY, z, false);
                    this.world.addWeatherEffect(bolt);
                    this.world.spawnEntity(bolt);
                }
                this.wonderSinceIgnited += 1;
            } else if (this.wonderSinceIgnited == 100) {
                Explosion explosion = this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 8 : 4, true);
                explosion.getAffectedBlockPositions().forEach(pos -> this.world.setBlockState(pos, Blocks.FIRE.getDefaultState()));
                this.wonderSinceIgnited += 1;
            } else if (this.wonderSinceIgnited == 150) {
                for (int x = -5; x <= 5; x++) {
                    for (int z = -5; z <= 5; z++) {
                        if (x == -5 || z == -5 || x == 5 || z == 5) {
                            for (int y = -3; y <= 7; y++) {
                                if (this.world.getBlockState(this.getPosition().add(x, y, z)).getBlockHardness(world, this.getPosition().add(x, y, z)) >= 0) {
                                    TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, y, z), Blocks.PACKED_ICE.getDefaultState());
                                }
                            }
                        }
                    }
                }
                this.wonderSinceIgnited = 999;
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
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 0x550000;
    }

    @Override
    public int getPrimaryColor() {
        return 0x000055;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "wondercreeper";
    }

    @Override
    public int getRegisterID() {
        return 412;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        this.clearActivePotions();
        if (!this.dead) {
            return super.takumiExplodeEvent(event);
        }
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200, 9));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 1200, 9));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200, 9));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 9));
                }
            });
        }
        return true;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isFireDamage() && damageSrc != DamageSource.IN_WALL) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
}
