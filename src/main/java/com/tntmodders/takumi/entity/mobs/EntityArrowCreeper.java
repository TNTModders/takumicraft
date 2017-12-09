package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class EntityArrowCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityArrowCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Items.ARROW, 32 + this.rand.nextInt(32));
        }
        super.onDeath(source);
    }
    
    @Override
    public void takumiExplode() {
        this.superJump();
    }
    
    protected void superJump() {
        this.motionY = 75d;
        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }
        
        if (this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }
        this.move(MoverType.SELF, motionX, motionY, motionZ);
        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
        
        for (int t = 0; t < 1000 * (this.getPowered() ? 2 : 1); t++) {
            Random rand = new Random();
            double x = this.posX + this.rand.nextInt(10) - 5;
            double y = this.posY + this.rand.nextInt(10) - 5;
            double z = this.posZ + this.rand.nextInt(10) - 5;
            
            EntityTippedArrow entityarrow = new EntityTippedArrow(this.world, x, y, z);
            entityarrow.setDamage(this.world.getDifficulty().getDifficultyId() * 8);
            entityarrow.setIsCritical(true);
            entityarrow.setKnockbackStrength(10);
            if (this.getPowered()) {
                entityarrow.setFire(100);
            }
            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            entityarrow.motionX = 0;
            entityarrow.motionY = -5;
            entityarrow.motionZ = 0;
            entityarrow.setPosition(x, y, z);
            if (!this.world.isRemote) {
                this.world.spawnEntity(entityarrow);
            }
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xf5d0a9;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "arrowcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 34;
    }
}
