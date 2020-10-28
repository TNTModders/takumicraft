package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

public class EntityExtremeCreeper extends EntityTakumiAbstractCreeper {
    private final int spTime = 60;
    private boolean flg;

    public EntityExtremeCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 80);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
    }


    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer && !((EntityPlayer) entity).isSpectator()) {
                    ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
                    NBTTagCompound tagCompound = stack.getTagCompound();
                    if (tagCompound == null) {
                        tagCompound = new NBTTagCompound();
                    }
                    tagCompound.setString("SkullOwner", entity.getName());
                    stack.setTagCompound(tagCompound);
                    this.world.setBlockState(entity.getPosition(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, entity.getAdjustedHorizontalFacing()));
                    if (this.world.getTileEntity(entity.getPosition()) instanceof TileEntitySkull) {
                        ((TileEntitySkull) this.world.getTileEntity(entity.getPosition())).setPlayerProfile(((EntityPlayer) entity).getGameProfile());
                    }
                } else if (entity instanceof EntityWitherSkeleton) {
                    ItemStack stack = new ItemStack(Items.SKULL, 1, 1);
                    this.world.setBlockState(entity.getPosition(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, entity.getAdjustedHorizontalFacing()));
                    if (this.world.getTileEntity(entity.getPosition()) instanceof TileEntitySkull) {
                        NBTTagCompound nbttagcompound = stack.getTagCompound();
                        ((TileEntitySkull) this.world.getTileEntity(entity.getPosition())).setType(stack.getMetadata());
                    }
                } else if (entity instanceof EntitySkeleton) {
                    ItemStack stack = new ItemStack(Items.SKULL, 1, 0);
                    this.world.setBlockState(entity.getPosition(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, entity.getAdjustedHorizontalFacing()));
                    if (this.world.getTileEntity(entity.getPosition()) instanceof TileEntitySkull) {
                        NBTTagCompound nbttagcompound = stack.getTagCompound();
                        ((TileEntitySkull) this.world.getTileEntity(entity.getPosition())).setType(stack.getMetadata());
                    }
                } else if (entity instanceof EntityZombie) {
                    ItemStack stack = new ItemStack(Items.SKULL, 1, 2);
                    this.world.setBlockState(entity.getPosition(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, entity.getAdjustedHorizontalFacing()));
                    if (this.world.getTileEntity(entity.getPosition()) instanceof TileEntitySkull) {
                        NBTTagCompound nbttagcompound = stack.getTagCompound();
                        ((TileEntitySkull) this.world.getTileEntity(entity.getPosition())).setType(stack.getMetadata());
                    }
                } else if (entity instanceof EntityCreeper) {
                    ItemStack stack = new ItemStack(Items.SKULL, 1, 4);
                    this.world.setBlockState(entity.getPosition(), Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, entity.getAdjustedHorizontalFacing()));
                    if (this.world.getTileEntity(entity.getPosition()) instanceof TileEntitySkull) {
                        NBTTagCompound nbttagcompound = stack.getTagCompound();
                        ((TileEntitySkull) this.world.getTileEntity(entity.getPosition())).setType(stack.getMetadata());
                    }
                }
                if (entity instanceof EntityLivingBase && entity.isNonBoss() && !(entity instanceof EntityPlayer && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator()))) {
                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(event.getExplosion()), ((EntityLivingBase) entity).getMaxHealth());
                }
            });
            event.getAffectedEntities().removeIf(entity -> entity instanceof EntityItem);
            event.getAffectedBlocks().removeIf(pos -> this.world.getBlockState(pos).getBlock() == Blocks.SKULL);
        }
        return true;
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
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "extremecreeper";
    }

    @Override
    public int getRegisterID() {
        return 413;
    }

    @Override
    public int getPrimaryColor() {
        return 0x222222;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() != null) {
            this.ignite();
            this.setCreeperState(1);
        }
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
            field.setAccessible(true);
            int time = field.getInt(this);
            if (time == this.spTime && this.getAttackTarget() != null) {
                double x = this.getAttackTarget().posX + Math.cos(Math.toRadians(this.getAttackTarget().rotationYaw - 90));
                double y = this.getAttackTarget().posY;
                double z = this.getAttackTarget().posZ + Math.sin(Math.toRadians(this.getAttackTarget().rotationYaw - 90));
                double prevX = this.posX;
                double prevY = this.posY;
                double prevZ = this.posZ;
                this.setPosition(x, y, z);
                if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                    this.setPosition(prevX, prevY, prevZ);
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1f, -1f);
                } else {
                    this.flg = true;
                    if (FMLCommonHandler.instance().getSide().isClient()) {
                        for (int i = 0; i < 30; i++) {
                            this.spawnParticle(x, y, z);
                            double dx = (x - prevX) / 30;
                            double dy = (y - prevY) / 30;
                            double dz = (z - prevZ) / 30;
                            this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.PORTAL.getParticleID(), prevX + dx * i, prevY + dy * i + 1, prevZ + dz * i,
                                    0, 0, 0);
                        }
                        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1f, 1f);
                    }
                }

                if (FMLCommonHandler.instance().getSide().isClient()) {
                    for (int i = 0; i < 30; i++) {
                        this.spawnParticle(prevX, prevY, prevZ);
                    }
                }
            } else if (flg && time > this.spTime && this.getAttackTarget() != null) {
                for (int i = 0; i < 5; i++) {
                    double x = this.getAttackTarget().posX + Math.cos(Math.toRadians(this.getAttackTarget().rotationYaw - 90)) * 0.2;
                    double y = this.getAttackTarget().posY + 1.5;
                    double z = this.getAttackTarget().posZ + Math.sin(Math.toRadians(this.getAttackTarget().rotationYaw - 90)) * 0.2;
                    this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), x, y, z,
                            (this.rand.nextDouble() - 0.5) * 0.5, (this.rand.nextDouble() - 0.5) * 0.5, (this.rand.nextDouble() - 0.5) * 0.5, Block.getIdFromBlock(Blocks.REDSTONE_BLOCK));
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        if (this.getCreeperState() <= 0 && this.getAttackTarget() == null) {
            super.move(type, x, y, z);
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticle(double x, double y, double z) {
        for (int i = 0; i < 30; i++) {
            double dx = (this.rand.nextDouble() - 0.5D) * 4;
            double dy = this.rand.nextDouble() * 4;
            double dz = (this.rand.nextDouble() - 0.5D) * 4;
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySinobiCreeper.ParticleSmokeSinobi(this.world, x + dx,
                    y + dy, z + dz, dx / 3, dy / 3, dz / 3, 1.0f, 10));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
            field.setAccessible(true);
            int time = field.getInt(this);
            if (time < this.spTime) {
                if (source.getTrueSource() != null && !source.isCreativePlayer()) {
                    source.getTrueSource().playSound(SoundEvents.BLOCK_ANVIL_PLACE, 1f, 1.5f);
                    source.getTrueSource().attackEntityFrom(DamageSource.causeMobDamage(this), amount * 1.5f);
                }
                return false;
            }
        } catch (Exception e) {
        }
        return super.attackEntityFrom(source, source.getTrueSource() instanceof EntityPlayer ? amount * 3 : amount);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.setAttackTarget(player);
        }
        return super.processInteract(player, hand);
    }

    @Override
    public boolean isImmuneToExplosions() {
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
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.EVO_CORE_EVO, this.rand.nextInt(1));
        }
        super.onDeath(source);
    }
}
