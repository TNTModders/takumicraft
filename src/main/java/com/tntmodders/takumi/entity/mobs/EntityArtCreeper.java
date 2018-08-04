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

public class EntityArtCreeper extends EntityTakumiAbstractCreeper {

    public EntityArtCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 5 : 3, true);
        }
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
        return 0;
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
        return "artcreeper";
    }

    @Override
    public int getRegisterID() {
        return 221;
    }

    @Override
    @Deprecated
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            try {
                List<IRecipe> list = Lists.newArrayList(CraftingManager.REGISTRY.iterator());
                for (BlockPos pos : event.getAffectedBlocks()) {
                    IRecipe iRecipe;
                    boolean flg = false;
                    while (!flg) {
                        iRecipe = list.get(this.rand.nextInt(list.size()));
                        if (iRecipe.getRecipeOutput().getItem() instanceof ItemBlock) {
                            try {
                                IBlockState blockState =
                                        ((ItemBlock) iRecipe.getRecipeOutput().getItem()).getBlock().getStateFromMeta(
                                                iRecipe.getRecipeOutput().getMetadata());
                                if (!(blockState.getBlock().hasTileEntity(blockState) &&
                                        blockState.getBlock().createTileEntity(this.world,
                                                blockState) instanceof IInventory) && blockState.isFullCube() &&
                                        blockState.getBlockHardness(this.world, pos) > -1) {
                                    TakumiUtils.setBlockStateProtected(this.world, pos, blockState);
                                    flg = true;
                                }
                            } catch (Exception ignore) {
                            }
                        }
                    }
                }
            } catch (Exception ignore) {
            }
            event.getAffectedBlocks().clear();
            event.getAffectedEntities().clear();
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaa0066;
    }
}
