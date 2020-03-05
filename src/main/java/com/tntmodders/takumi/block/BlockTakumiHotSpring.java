package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.material.TakumiMaterial;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiFluidCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockTakumiHotSpring extends BlockFluidClassic {

    public BlockTakumiHotSpring() {
        super(TakumiFluidCore.HOT_SPRING, TakumiMaterial.HOT_SPRING);
        this.setRegistryName(TakumiCraftCore.MODID, "takumihotspring");
        this.setUnlocalizedName("takumihotspring");
        this.setResistance(10000000f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote && rand.nextBoolean()) {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + (rand.nextDouble() - 0.5D),
                    pos.getY() + 0.375 + rand.nextDouble(), pos.getZ() + (rand.nextDouble() - 0.5D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityLivingBase && entityIn.ticksExisted % 60 == 0) {
            ((EntityLivingBase) entityIn).heal(0.25f);
            if (entityIn instanceof EntityPlayer && !worldIn.isRemote) {
                ((EntityPlayer) entityIn).getFoodStats().addStats(2, 0.7f);
            }
        }
        if (entityIn instanceof EntityLivingBase) {
            ((EntityLivingBase) entityIn).getActivePotionEffects().removeIf(potionEffect -> potionEffect.getPotion().isBadEffect() && potionEffect.getPotion() != TakumiPotionCore.INVERSION);
        }
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity,
                                          double yToTest, Material materialIn, boolean testingHead) {
        return materialIn == Material.WATER ? true :
                super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        if (world.getBlockState(pos).getMaterial().isLiquid() && world.getBlockState(pos).getBlock() != this) {
            world.setBlockState(pos, TakumiBlockCore.TAKUMI_STONE.getDefaultState());
        }
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.displaceIfPossible(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor,
                             float partialTicks) {
        float f12 = 0.0F;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = (EntityLivingBase) entity;
            f12 = (float) EnchantmentHelper.getRespirationModifier(ent) * 0.2F;
            if (ent.isPotionActive(MobEffects.WATER_BREATHING)) {
                f12 = f12 * 0.3F + 0.6F;
            }
        }
        return new Vec3d(0.1F + f12, 0.1F + f12, 0.1F + f12);
    }
}
