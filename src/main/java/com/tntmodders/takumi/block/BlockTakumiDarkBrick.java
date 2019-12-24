package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageDarkShrine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTakumiDarkBrick extends Block {
    protected static final AxisAlignedBB SOUL_SAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockTakumiDarkBrick() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkbrick");
        this.setUnlocalizedName("darkbrick");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return SOUL_SAND_AABB;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.motionX *= 0.8D;
        entityIn.motionZ *= 0.8D;
        if (entityIn instanceof EntityPlayer) {
            Potion potion = MobEffects.MINING_FATIGUE;
            if (!((EntityPlayer) entityIn).isPotionActive(potion) || ((EntityPlayer) entityIn).getActivePotionEffect(potion).getAmplifier() < 2 || ((EntityPlayer) entityIn).getActivePotionEffect(potion).getDuration() < 1200) {
                if (!worldIn.isRemote) {
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(potion, 6000, 4, false, false));
                    ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 250, 0, true, false));
                }
                if (entityIn instanceof EntityPlayerMP) {
                   try{
                       TakumiPacketCore.INSTANCE.sendTo(new MessageDarkShrine(), ((EntityPlayerMP) entityIn));
                   }catch (Exception ignored){}
                }
            }
        }
    }

}
