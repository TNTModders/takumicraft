package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySinobiCreeper extends EntityTakumiAbstractCreeper {

    public EntitySinobiCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected SoundEvent getFallSound(int heightIn) {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0f;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
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
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x220022;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "sinobicreeper";
    }

    @Override
    public int getRegisterID() {
        return 254;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.spawnParticle(event);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticle(Detonate event) {
        for (int i = 0; i < 30; i++) {
            event.getAffectedBlocks().forEach(pos -> Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleSmokeSinobi(this.world, pos.getX() + (this.rand.nextDouble() - 0.5D),
                            pos.getY() + this.rand.nextDouble(), pos.getZ() + (this.rand.nextDouble() - 0.5D), 0.0D,
                            0.0D, 0.0D, 1.0f)));
        }
    }

    @Override
    public int getPrimaryColor() {
        return 0x222222;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/dragon_armor.png");
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

    }

    private class ParticleSmokeSinobi extends ParticleSmokeNormal {

        public ParticleSmokeSinobi(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46348_8_,
                double p_i46348_10_, double p_i46348_12_, float p_i46348_14_) {
            super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46348_8_, p_i46348_10_, p_i46348_12_, p_i46348_14_);
            this.particleRed = 1;
            this.particleGreen = 1;
            this.particleBlue = 1;
            this.particleMaxAge = (int) (4.0F / (this.rand.nextFloat() * 0.9F + 0.1F)) * 100;
        }

        @Override
        public void onUpdate() {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.particleAge++ >= this.particleMaxAge) {
                this.setExpired();
            }

            this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
            this.motionY -= 0.0005D;
            this.move(this.motionX, this.motionY, this.motionZ);

            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.9599999785423279D;
            this.motionZ *= 0.9599999785423279D;

            if (this.onGround) {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
            }
        }
    }
}
