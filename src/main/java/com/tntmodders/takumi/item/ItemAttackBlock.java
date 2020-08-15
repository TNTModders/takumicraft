package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityAttackBlock;
import com.tntmodders.takumi.entity.item.EntityBigCreeperDummy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAttackBlock extends Item {
    public ItemAttackBlock() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "attackblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("attackblock");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && worldIn.loadedEntityList.stream().noneMatch(
                entity -> entity instanceof EntityAttackBlock && !entity.isDead)) {
            EntityAttackBlock attackBlock = new EntityAttackBlock(worldIn);
            BlockPos blockPos = pos.offset(facing);
            if (worldIn.isAirBlock(blockPos) && worldIn.isAirBlock(blockPos.up())) {
                attackBlock.setPosition(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
            }
            double theta = Math.toRadians(worldIn.rand.nextFloat() * 360);
            BlockPos bigPos = blockPos.add(Math.cos(theta) * EntityAttackBlock.DIST, 0, Math.sin(theta) * EntityAttackBlock.DIST);
            bigPos = worldIn.getHeight(bigPos);
            attackBlock.setPos(bigPos);
            double dx = bigPos.getX() - blockPos.getX();
            attackBlock.setDX(((float) (dx / EntityAttackBlock.ATTACK_TICK)));
            double dz = bigPos.getZ() - blockPos.getZ();
            attackBlock.setDZ(((float) (dz / EntityAttackBlock.ATTACK_TICK)));
            if (worldIn.spawnEntity(attackBlock)) {
                EntityBigCreeperDummy bigCreeperDummy = new EntityBigCreeperDummy(worldIn);
                bigCreeperDummy.setPosition(bigPos.getX(), bigPos.getY(), bigPos.getZ());
                worldIn.spawnEntity(bigCreeperDummy);
                worldIn.playBroadcastSound(1038, pos, 0);
                if (!player.isCreative()) {
                    player.getHeldItem(hand).shrink(1);
                }
                worldIn.getPlayers(EntityPlayer.class, input -> attackBlock.getDistanceSqToEntity(input) < 10000).forEach(player1
                                -> {
                            player1.sendMessage(new TextComponentTranslation("entity.attackblock.message.summon.01"));
                            player1.sendMessage(new TextComponentTranslation("entity.attackblock.message.summon.02"));
                            player1.sendMessage(new TextComponentTranslation("entity.attackblock.message.summon.03"));
                        }
                );
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
