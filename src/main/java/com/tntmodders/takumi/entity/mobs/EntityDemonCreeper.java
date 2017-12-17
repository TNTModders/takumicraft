package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class EntityDemonCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityDemonCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F / 3, 1.7F / 3);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 10);
        }
        super.onDeath(source);
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xff0000;
    }
    
    @Override
    public double getSizeAmp() {
        return 0.3d;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
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
        
        for (int t = 0; t < (this.getPowered() ? 250 : 100); t++) {
            Random rand = new Random();
            int i = this.getPowered() ? 50 : 30;
            double x = this.posX + this.rand.nextInt(i * 2) - i;
            double y = this.posY + this.rand.nextInt(i) - i / 2;
            double z = this.posZ + this.rand.nextInt(i * 2) - i;
            
            EntityLargeFireball fireball = new EntityLargeFireball(this.world, x, y, z, 0, -0.5, 0);
            fireball.motionX = 0;
            fireball.motionY = -1;
            fireball.motionZ = 0;
            fireball.explosionPower = this.getPowered() ? 5 : 3;
            if (!this.world.isRemote) {
                this.world.spawnEntity(fireball);
            }
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_MD;
    }
    
    @Override
    public int getExplosionPower() {
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "demoncreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 406;
    }
}
