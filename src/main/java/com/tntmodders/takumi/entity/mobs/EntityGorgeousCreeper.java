package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class EntityGorgeousCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityGorgeousCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        this.superJump();
        if (!this.world.isRemote) {
            for (int i = 0; i < (this.getPowered() ? 6 : 3); i++) {
                ItemStack item = EntityFireworksCreeper.getItemFireworks();
                double x = this.posX + i * 2 - i;
                double z = this.posZ + i * 2 - i;
                EntityFireworkRocket firework = new EntityFireworkRocket(world, x, this.world.getHeight((int) x, (int) z), z, item);
                
                this.world.spawnEntity(firework);
            }
        }
    }
    
    protected void superJump() {
        this.motionY = 100d;
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
        
        for (int t = 0; t < 125 * (this.getPowered() ? 2 : 1); t++) {
            Random rand = new Random();
            double x = this.posX + (this.rand.nextInt(30) - 15) * (this.getPowered() ? 2 : 1);
            double y = this.posY + (this.rand.nextInt(10) - 5) * (this.getPowered() ? 2 : 1);
            double z = this.posZ + (this.rand.nextInt(30) - 15) * (this.getPowered() ? 2 : 1);
            
            EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(this.world);
            entityTNTPrimed.setFuse(110);
            entityTNTPrimed.setPosition(x, y, z);
            if (!this.world.isRemote) {
                this.world.spawnEntity(entityTNTPrimed);
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
        return 3;
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
        return "gorgeouscreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 233;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xff7700;
    }
}
