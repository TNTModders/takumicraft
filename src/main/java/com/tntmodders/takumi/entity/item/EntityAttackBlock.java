package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAttackBlock extends EntityLiving {
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.attackblock.name"),
                    BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_20).setDarkenSky(true);

    public EntityAttackBlock(World worldIn) {
        super(worldIn);
        this.setSize(1, 2);
        this.isImmuneToFire = true;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25000);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(source, amount);
        }
        if (source.isExplosion() && amount > 2) {
            this.setDead();
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLiving) {
                    ((EntityLiving) entity).setAttackTarget(null);
                }
            });
            this.world.playerEntities.forEach(player -> {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.attackEntityFrom(
                            DamageSource.causeMobDamage(this).setDamageIsAbsolute().setDamageAllowedInCreativeMode(),
                            Integer.MAX_VALUE);
                    player.sendMessage(new TextComponentTranslation("entity.attackblock.lose"));
                }
            });
        }
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (!this.world.isAirBlock(this.getPosition())) {
            this.world.setBlockToAir(this.getPosition());
        }
        if (!this.world.isAirBlock(this.getPosition().up())) {
            this.world.setBlockToAir(this.getPosition().up());
        }
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }
}
