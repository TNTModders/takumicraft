package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ITakumiEntity.EnumTakumiRank;
import com.tntmodders.takumi.entity.mobs.EntityAnnivCreeper;
import com.tntmodders.takumi.entity.mobs.EntityKingCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BlockTakumiAltar extends Block {
    
    public BlockTakumiAltar() {
        super(Material.TNT, MapColor.GREEN);
        this.setRegistryName(TakumiCraftCore.MODID, "takumialtar");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumialtar");
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float
            hitX, float hitY, float hitZ) {
        boolean flg = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        Entity entity = null;
        if (worldIn.getBlockState(pos.down()).getBlock() == TakumiBlockCore.CREEPER_BOMB) {
            entity = new EntityKingCreeper(worldIn);
        } else if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.CAKE) {
            entity = new EntityAnnivCreeper(worldIn);
        } else {
            List <ITakumiEntity> entities = new ArrayList <>();
            TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
                if (iTakumiEntity.takumiRank() == EnumTakumiRank.HIGH) {
                    entities.add(iTakumiEntity);
                }
            });
            if (!entities.isEmpty()) {
                entities.removeIf(iTakumiEntity -> iTakumiEntity instanceof EntityAnnivCreeper);
                try {
                    entity = (Entity) entities.get(worldIn.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(worldIn);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (entity != null) {
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            worldIn.setBlockToAir(pos);
            if (worldIn.getBlockState(pos.down()).getBlockHardness(worldIn, pos.down()) > 0) {
                worldIn.setBlockToAir(pos.down());
            }
            if (!worldIn.isRemote) {
                worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3f, true);
                if (worldIn.spawnEntity(entity)) {
                    return true;
                }
            }
        }
        return flg;
    }
}
