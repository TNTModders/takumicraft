package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.entity.mobs.boss.EntityUnlimitedCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityPsychicCreeper extends EntityZombieCreeper {

    public EntityPsychicCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        if (this.getHeldItemMainhand() == ItemStack.EMPTY) {
            this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STICK));
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    int i = this.rand.nextInt(4);
                    switch (i) {
                        //大吉
                        case 0: {
                            ItemStack itemStack = new ItemStack(Items.DIAMOND).setStackDisplayName(TakumiUtils.takumiTranslate("entity.psychiccreeper.return.0"));
                            ((EntityPlayer) entity).dropItem(((EntityPlayer) entity).getHeldItemMainhand(), false);
                            ((EntityPlayer) entity).setHeldItem(EnumHand.MAIN_HAND, itemStack);

                            BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                            if (pos != null &&
                                    this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                                            pos, entity) && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this, null)
                                    < Blocks.OBSIDIAN.getExplosionResistance(world, pos, this, null)) {
                                for (int t = 0; t < 4; t++) {
                                    EntityCreeper creeper = new EntityDiamondCreeper(this.world);
                                    creeper.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                    this.world.spawnEntity(creeper);
                                }
                            }
                            entity.sendMessage(new TextComponentString(
                                    ((EntityPlayer) entity).getDisplayNameString() + " " + TakumiUtils.takumiTranslate("entity.psychiccreeper.message.0")));
                            break;
                        }
                        //中吉
                        case 1: {
                            ItemStack itemStack = new ItemStack(Items.FIREWORKS).setStackDisplayName(TakumiUtils.takumiTranslate("entity.psychiccreeper.return.1"));
                            ((EntityPlayer) entity).dropItem(((EntityPlayer) entity).getHeldItemMainhand(), false);
                            ((EntityPlayer) entity).setHeldItem(EnumHand.MAIN_HAND, itemStack);

                            BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                            if (pos != null &&
                                    this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                                            pos, entity) && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this, null)
                                    < Blocks.OBSIDIAN.getExplosionResistance(world, pos, this, null)) {
                                EntityCreeper creeper = new EntityLeadCreeper(this.world);
                                creeper.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper);
                            }
                            entity.sendMessage(new TextComponentString(
                                    ((EntityPlayer) entity).getDisplayNameString() + " " + TakumiUtils.takumiTranslate("entity.psychiccreeper.message.1")));
                            break;
                        }
                        //吉
                        case 2: {
                            ItemStack itemStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.NIGHT_VISION)
                                    .setStackDisplayName(TakumiUtils.takumiTranslate("entity.psychiccreeper.return.2"));
                            ((EntityPlayer) entity).dropItem(((EntityPlayer) entity).getHeldItemMainhand(), false);
                            ((EntityPlayer) entity).setHeldItem(EnumHand.MAIN_HAND, itemStack);

                            BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                            if (pos != null &&
                                    this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                                            pos, entity) && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this, null)
                                    < Blocks.OBSIDIAN.getExplosionResistance(world, pos, this, null)) {
                                EntityCreeper creeper1 = new EntityArtCreeper(this.world);
                                creeper1.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper1);

                                EntityCreeper creeper2 = new EntityRewriteCreeper(this.world);
                                creeper2.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper2);
                            }
                            entity.sendMessage(new TextComponentString(
                                    ((EntityPlayer) entity).getDisplayNameString() + " " + TakumiUtils.takumiTranslate("entity.psychiccreeper.message.2")));
                            break;
                        }
                        //凶
                        case 3: {
                            ItemStack itemStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.SWIFTNESS)
                                    .setStackDisplayName(TakumiUtils.takumiTranslate("entity.psychiccreeper.return.3"));
                            ((EntityPlayer) entity).dropItem(((EntityPlayer) entity).getHeldItemMainhand(), false);
                            ((EntityPlayer) entity).setHeldItem(EnumHand.MAIN_HAND, itemStack);
                            BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                            if (pos != null &&
                                    this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                                            pos, entity) && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this, null)
                                    < Blocks.OBSIDIAN.getExplosionResistance(world, pos, this, null)) {
                                EntityCreeper creeper1 = new EntityRushCreeper(this.world);
                                creeper1.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper1);

                                EntityCreeper creeper2 = new EntityDashCreeper(this.world);
                                creeper2.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper2);

                                EntityCreeper creeper3 = new EntityFastCreeper(this.world);
                                creeper3.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                                this.world.spawnEntity(creeper3);
                            }
                            entity.sendMessage(new TextComponentString(
                                    ((EntityPlayer) entity).getDisplayNameString() + " " + TakumiUtils.takumiTranslate("entity.psychiccreeper.message.3")));
                            break;
                        }
                    }
                }
            });
        }
        event.getAffectedEntities().clear();
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer entity = ((EntityPlayer) cause.getTrueSource());
            ItemStack itemStack = new ItemStack(Items.SKULL, 1, 4).setStackDisplayName(TakumiUtils.takumiTranslate("entity.psychiccreeper.return.4"));
            entity.dropItem(entity.getHeldItemMainhand(), false);
            entity.setHeldItem(EnumHand.MAIN_HAND, itemStack);
            BlockPos pos = entity.getBedLocation(this.world.provider.getDimension());
            if (pos != null &&
                    this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                            pos, entity) && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this, null)
                    < Blocks.OBSIDIAN.getExplosionResistance(world, pos, this, null)) {
                EntityCreeper creeper = new EntityUnlimitedCreeper(this.world);
                creeper.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                this.world.spawnEntity(creeper);
            }
            entity.sendMessage(new TextComponentString(
                    entity.getDisplayNameString() + " " + TakumiUtils.takumiTranslate("entity.psychiccreeper.message.4")));
        }
    }

    @Override
    public void setFire(int seconds) {
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.BLOCK;
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
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public int getPrimaryColor() {
        return 0x9999aa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public boolean canRegister() {
        return TakumiConfigCore.inDev;
    }

    @Override
    public String getRegisterName() {
        return "psychiccreeper";
    }

    @Override
    public int getRegisterID() {
        return 304;
    }
}
