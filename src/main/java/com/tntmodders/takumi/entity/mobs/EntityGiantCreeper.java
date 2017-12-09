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
    
    private static final DataParameter <Boolean> EXPLODED = EntityDataManager.createKey(EntityGiantCreeper.class, DataSerializers.BOOLEAN);
    private boolean dead;
    
    public EntityGiantCreeper(World worldIn) {
        super(worldIn);
        this.setSize(6f, 20f);
        this.isImmuneToFire = true;
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        if (this.getDataManager().get(EXPLODED)) {
            if (!this.isInvisible()) {
                this.setInvisible(true);
            }
            int di = 40;
            for (float f1 = -1; f1 <= 1; f1 += 0.025) {
                for (float f2 = -1; f2 <= 1; f2 += 0.025) {
                    for (float f3 = -1; f3 <= 1; f3 += 0.025) {
                        if (f1 * f1 + f2 * f2 * 1.5 + f3 * f3 < 1 && f1 * f1 + f2 * f2 * 1.5 + f3 * f3 > 0.81) {
                            if (this.rand.nextInt(15) == 0) {
                                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + f1 * 1.1 * this.ticksExisted / di,
                                                         this.posY + f2 * 1.1 * this.ticksExisted / di, this.posZ + f3 * 1.1 * this.ticksExisted / di,
                                                         f1 / 5, f2 / 5, f3 / 5);
                            }
                            int x = (int) Math.floor(this.posX + f1 * this.ticksExisted / di);
                            int y = (int) Math.floor(this.posY + f2 * this.ticksExisted / di);
                            int z = (int) Math.floor(this.posZ + f3 * this.ticksExisted / di);
                            BlockPos pos = new BlockPos(x, y, z);
                            if (!this.world.isAirBlock(pos) && this.rand.nextInt((int) Math.sqrt(this.ticksExisted) + 1) == 0) {
                                this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 2.5f, true);
                            }
                        }
                    }
                }
            }
            if (this.ticksExisted > (this.getPowered() ? 75 * di : 60 * di)) {
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
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
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
        return 403;
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
