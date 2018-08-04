package com.tntmodders.takumi.entity.mobs.noncreeper;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityDarkCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityDarkVillager extends EntitySpellcasterIllager {
    public EntityDarkVillager(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public void setDead() {
        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.getHealth() <= 1) {
            super.setDead();
        }
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote && this.world.loadedTileEntityList.stream().anyMatch(
                tileEntity -> tileEntity.getBlockType() == TakumiBlockCore.DARKCORE_ON)) {
            for (int i = 0; i < 10; ++i) {
                this.world.spawnParticle(EnumParticleTypes.FLAME,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
            this.setFire(100);
        }
        super.onLivingUpdate();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
    }

    @Override
    public IllagerArmPose getArmPose() {
        return IllagerArmPose.SPELLCASTING;
    }

    @Override
    public void onUpdate() {
        this.setAggressive(1, true);
        this.setHealth(20);
        this.setEntityInvulnerable(true);
        if (!this.world.isAirBlock(this.getPosition()) &&
                this.world.getBlockState(this.getPosition()).getBlockHardness(this.world, this.getPosition()) > 0) {
            this.world.setBlockToAir(this.getPosition());
        }
        if (!this.world.isAirBlock(this.getPosition().up()) &&
                this.world.getBlockState(this.getPosition().up()).getBlockHardness(this.world,
                        this.getPosition().up()) > 0) {
            this.world.setBlockToAir(this.getPosition().up());
        }
        this.world.loadedTileEntityList.forEach(tileEntity -> {
            if (tileEntity instanceof TileEntityDarkCore) {
                this.getLookHelper().setLookPosition(tileEntity.getPos().getX(), tileEntity.getPos().getY(),
                        tileEntity.getPos().getZ(), 1, 1);
                this.getLookHelper().onUpdateLook();
            }
        });
        super.onUpdate();
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }
}
