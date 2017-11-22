package com.tntmodders.takumi.entity.mobs;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityGiantCreeper extends EntityZombieCreeper {
    private static final DataParameter<Boolean> EXPLODED = EntityDataManager.createKey(EntityGiantCreeper.class, DataSerializers.BOOLEAN);
    private boolean dead;

    public EntityGiantCreeper(World worldIn) {
        super(worldIn);
        this.setSize(6f, 19.5f);
        this.isImmuneToFire = true;
    }

    @Override
    public void onUpdate() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        if (this.getDataManager().get(EXPLODED)) {
            int di = 40;
            for (float f1 = -1; f1 <= 1; f1 += 0.025) {
                for (float f2 = -1; f2 <= 1; f2 += 0.025) {
                    for (float f3 = -1; f3 <= 1; f3 += 0.025) {
                        if (f1 * f1 + f2 * f2 * 1.5 + f3 * f3 < 1 && f1 * f1 + f2 * f2 * 1.5 + f3 * f3 > 0.81) {
                            if (this.rand.nextInt(10) == 0) {
                                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + f1 * this.ticksExisted / di, this.posY + f2 * this.ticksExisted / di,
                                        this.posZ + f3 * this.ticksExisted / di, 0.0D, 0.0D, 0.0D);
                            }
                            int x = (int) Math.floor(this.posX + f1 * this.ticksExisted / di);
                            int y = (int) Math.floor(this.posY + f2 * this.ticksExisted / di);
                            int z = (int) Math.floor(this.posZ + f3 * this.ticksExisted / di);
                         /*   if (!this.world.isRemote && Math.abs(this.posX + f1 * this.ticksExisted / 20 - x) < 0.1 &&
                                    Math.abs(this.posY + f1 * this.ticksExisted / 20 - y) < 0.1 && Math.abs(this.posZ + f1 * this.ticksExisted / 20 - z) < 0.1)*/
                            BlockPos pos = new BlockPos(x, y, z);
                            if (!this.world.isAirBlock(pos) && this.rand.nextInt(this.ticksExisted / 40 < 20 ? 20 : this.ticksExisted / 40 < 75 ? this.ticksExisted / 40 : this.ticksExisted / 160) == 0) {
                                this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 2.5f, true);
                             /*if(this.rand.nextBoolean()){
                                 for(int t = 0; t < 3; t++){
                                     this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + f1 * this.ticksExisted / 40, this.posY + f2 * this.ticksExisted / 40,
                                             this.posZ + f3 * this.ticksExisted / 40, 0.0D, 0.0D, 0.0D);
                                 }
                             }
                             this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
                             for (int i = 0; i < 6; i++) {
                                 this.world.setBlockState(pos.offset(EnumFacing.values()[i]), Blocks.AIR.getDefaultState(), 4);
                             }*/
                            }
                        }
                    }
                }
            }
            if (this.ticksExisted > (this.getPowered() ? 75 * di : 50 * di)) {
                this.getDataManager().set(EXPLODED, false);
                this.dead = true;
                this.setDead();
            }
        } else {
            super.onUpdate();
        }
    }

    @Override
    public void setDead() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL || !this.getDataManager().get(EXPLODED) || this.dead) {
            super.setDead();
        }
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.5f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EXPLODED, Boolean.FALSE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.dataManager.get(EXPLODED)) {
            compound.setBoolean("exploded", true);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(EXPLODED, compound.getBoolean("exploded"));
    }

    @Override
    public void takumiExplode() {
        this.setInvisible(true);
        this.ticksExisted = 0;
        this.getDataManager().set(EXPLODED, true);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 0;
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
        return "giantcreeper";
    }

    @Override
    public int getRegisterID() {
        return 503;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!event.getWorld().isRemote) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                event.getWorld().setBlockToAir(pos);
            }
        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0;
    }

    @Override
    public double getSizeAmp() {
        return 10;
    }
}
