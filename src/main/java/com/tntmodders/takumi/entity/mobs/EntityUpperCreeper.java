package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.world.World;

public class EntityUpperCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityUpperCreeper(World worldIn) {
        super(worldIn);
    }
/*
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        Map<EntityPlayer, Vec3d> playerKnockbackMap = new HashMap<>();
        double x = event.getExplosion().getPosition().x;
        double y = event.getExplosion().getPosition().y;
        double z = event.getExplosion().getPosition().z;
        Vec3d vec3d = new Vec3d(x, y, z);

        for (Entity entity : event.getAffectedEntities()) {
            if (!entity.isImmuneToExplosions()) {
                double d3 = this.getPowered() ? 16 : 8;
                double d12 = entity.getDistance(x, y, z) / d3;

                if (d12 <= 1.0D) {
                    double d5 = entity.posX - x;
                    double d7 = entity.posY + entity.getEyeHeight() - y;
                    double d9 = entity.posZ - y;
                    double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (int) ((d10 * d10 + d10) / 2.0D * 7.0D * d3 + 1.0D));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase) {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                        }

                        entity.motionX += d5 * d11 * 100;
                        entity.motionY += d7 * d11 * 100;
                        entity.motionZ += d9 * d11 * 100;

                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityplayer = (EntityPlayer) entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
        try {
            Field field = event.getExplosion().getClass().getDeclaredField("playerKnockbackMap");
            field.setAccessible(true);
            field.set(event.getExplosion(), playerKnockbackMap);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        event.getAffectedEntities().clear();
        return true;
    }*/
    
    @Override
    public int getPrimaryColor() {
        return 0x002200;
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ,
                    this.getPowered() ? 8 : 4, false, false, this.getPowered() ? 15 : 5);
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND;
    }
    
    @Override
    public int getExplosionPower() {
        return 0;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xddffdd;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "uppercreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 232;
    }
}
