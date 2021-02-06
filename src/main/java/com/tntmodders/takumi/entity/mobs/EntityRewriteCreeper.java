package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.List;

public class EntityRewriteCreeper extends EntityTakumiAbstractCreeper {

    public EntityRewriteCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.WIND_M;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "rewritecreeper";
    }

    @Override
    public int getRegisterID() {
        return 222;
    }

    @Override
    @Deprecated
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            List<IRecipe> list = Lists.newArrayList(CraftingManager.REGISTRY.iterator());
            IRecipe iRecipe;
            IBlockState blockState = null;
            boolean flg = false;
            while (!flg) {
                iRecipe = list.get(this.rand.nextInt(list.size()));
                if (iRecipe.getRecipeOutput().getItem() instanceof ItemBlock) {
                    blockState = ((ItemBlock) iRecipe.getRecipeOutput().getItem()).getBlock().getStateFromMeta(
                            iRecipe.getRecipeOutput().getMetadata());
                    if (!(blockState.getBlock().hasTileEntity(blockState) &&
                            blockState.getBlock().createTileEntity(this.world, blockState) instanceof IInventory) &&
                            blockState.getBlock().canPlaceBlockAt(this.world, this.getPosition()) &&
                            blockState.getBlockHardness(this.world, this.getPosition()) > -1 &&
                            !TakumiUtils.isExcludedBlockForRewrite(blockState.getBlock())) {
                        TakumiUtils.setBlockStateProtected(this.world, this.getPosition(), blockState);
                        if (blockState.getBlock().canPlaceBlockAt(this.world, this.getPosition().up())) {
                            flg = true;
                        }
                    }
                }
            }
            for (BlockPos pos : event.getAffectedBlocks()) {
                if (!this.world.isAirBlock(pos)) {
                    TakumiUtils.setBlockStateProtected(this.world, pos, blockState);
                }
            }
            event.getAffectedBlocks().clear();
            event.getAffectedEntities().clear();
            this.world.setBlockToAir(this.getPosition());
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x6600aa;
    }
}
