package com.tntmodders.takumi.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.List;

public class EntityTakumiLightningBolt extends EntityWeatherEffect {
    private final boolean effectOnly;
    /**
     * A random long that is used to change the vertex of the lightning rendered in RenderLightningBolt
     */
    public long boltVertex;
    public float red;
    public float green;
    public float blue;
    public float alpha;
    /**
     * Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc.
     */
    private int lightningState;
    /**
     * Determines the time before the EntityLightningBolt is destroyed. It is a random integer decremented over time.
     */
    private int boltLivingTime;

    public EntityTakumiLightningBolt(World worldIn, double x, double y, double z, boolean effectOnlyIn) {
        super(worldIn);
        this.red = 1f;
        this.green = 0f;
        this.blue = 0.4f;
        this.alpha = 0.3f;

        this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        this.effectOnly = effectOnlyIn;
        BlockPos blockpos = new BlockPos(this);

        if (!effectOnlyIn && !worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10)) {
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }

            for (int i = 0; i < 4; ++i) {
                BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos1)) {
                    worldIn.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public EntityTakumiLightningBolt(World worldIn, double x, double y, double z, boolean effectOnlyIn, float red, float green, float blue, float alpha) {
        this(worldIn, x, y, z, effectOnlyIn);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }


    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.lightningState == 2) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;

                if (!this.effectOnly && !this.world.isRemote) {
                    this.boltVertex = this.rand.nextLong();
                    BlockPos blockpos = new BlockPos(this);

                    if (this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(blockpos, 10) && this.world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, blockpos)) {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }

        if (this.lightningState >= 0) {
            if (this.world.isRemote) {
                this.world.setLastLightningBolt(2);
            } else if (!this.effectOnly) {
                double d0 = 3.0D;
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 3.0D, this.posY - 3.0D, this.posZ - 3.0D, this.posX + 3.0D, this.posY + 6.0D + 3.0D, this.posZ + 3.0D));

                for (Entity entity : list) {
                    if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, null)) {
                        entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 8.0F);
                        entity.onStruckByLightning(null);
                    }
                }
            }
        }
    }

    @Override
    protected void entityInit() {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }
}
