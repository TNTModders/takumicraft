package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiMinecart;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTakumiMinecart extends ItemMinecart {

    private static final EntityMinecart.Type TYPE = EntityMinecart.Type.RIDEABLE;
    private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
        private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

        /**
         * Dispense the specified stack, play the dispense sound and spawn particles.
         */
        @Override
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
            World world = source.getWorld();
            double d0 = source.getX() + (double) enumfacing.getFrontOffsetX() * 1.125D;
            double d1 = Math.floor(source.getY()) + (double) enumfacing.getFrontOffsetY();
            double d2 = source.getZ() + (double) enumfacing.getFrontOffsetZ() * 1.125D;
            BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            IBlockState iblockstate = world.getBlockState(blockpos);
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(world, blockpos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double d3;

            if (BlockRailBase.isRailBlock(iblockstate)) {
                if (blockrailbase$enumraildirection.isAscending()) {
                    d3 = 0.6D;
                } else {
                    d3 = 0.1D;
                }
            } else {
                if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }

                IBlockState iblockstate1 = world.getBlockState(blockpos.down());
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate1.getBlock()).getRailDirection(world, blockpos.down(), iblockstate1, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;

                if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending()) {
                    d3 = -0.4D;
                } else {
                    d3 = -0.9D;
                }
            }

            EntityMinecart entityminecart = EntityTakumiMinecart.create(world, d0, d1 + d3, d2, TYPE);

            if (stack.hasDisplayName()) {
                entityminecart.setCustomNameTag(stack.getDisplayName());
            }

            world.spawnEntity(entityminecart);
            stack.shrink(1);
            return stack;
        }

        /**
         * Play the dispense sound from the specified block.
         */
        @Override
        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    };

    public ItemTakumiMinecart() {
        super(TYPE);
        this.maxStackSize = 1;
        this.setRegistryName(TakumiCraftCore.MODID, "creeperminecart");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperminecart");
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, MINECART_DISPENSER_BEHAVIOR);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (!BlockRailBase.isRailBlock(iblockstate)) {
            return EnumActionResult.FAIL;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);

            if (!worldIn.isRemote) {
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(worldIn, pos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockrailbase$enumraildirection.isAscending()) {
                    d0 = 0.5D;
                }

                EntityMinecart entityminecart = EntityTakumiMinecart.create(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D, TYPE);

                if (itemstack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(itemstack.getDisplayName());
                }

                worldIn.spawnEntity(entityminecart);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
    }
}
