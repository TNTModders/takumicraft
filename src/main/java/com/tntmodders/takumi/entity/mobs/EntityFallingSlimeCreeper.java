package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderFallingSlimeCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.MoverType;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class EntityFallingSlimeCreeper extends EntityTakumiAbstractCreeper {

    public EntityFallingSlimeCreeper(World worldIn) {
        super(worldIn);
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

        for (int t = 0; t < 100 * (this.getPowered() ? 2 : 1); t++) {
            Random rand = new Random();
            double x = this.posX + this.rand.nextInt(20) - 10;
            double y = this.posY + this.rand.nextInt(20) - 10;
            double z = this.posZ + this.rand.nextInt(20) - 10;

            EntitySlimeCreeper slimeCreeper = new EntitySlimeCreeper(this.world);
            int i = this.rand.nextInt(3);

            if (i < 2 && this.rand.nextFloat() < 0.5F * this.world.getDifficulty().getDifficultyId()) {
                ++i;
            }

            int j = 1 << i;
            slimeCreeper.setSlimeSize(j, true);
            slimeCreeper.motionX = 0;
            slimeCreeper.motionY = -5;
            slimeCreeper.motionZ = 0;
            slimeCreeper.setPosition(x, y, z);
            if (!this.world.isRemote) {
                this.world.spawnEntity(slimeCreeper);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaaffaa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fallingslimecreeper";
    }

    @Override
    public int getRegisterID() {
        return 256;
    }

    @Override
    public int getPrimaryColor() {
        return 0x004400;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderFallingSlimeCreeper<>(manager);
    }
}
