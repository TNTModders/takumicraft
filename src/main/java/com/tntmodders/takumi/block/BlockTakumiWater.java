package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiFluidCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.entity.mobs.EntitySeaGuardianCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySquidCreeper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockTakumiWater extends BlockFluidClassic {

    public BlockTakumiWater() {
        super(TakumiFluidCore.TAKUMI_WATER, Material.WATER);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiwater");
        this.setUnlocalizedName("takumiwater");
        this.setResistance(10000000f);
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
        return new Vec3d(0.02F + f12, 0.2F + f12, 0.02F + f12);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityPlayer && !((EntityPlayer) entityIn).isCreative() && (entityIn.motionX < 0.5 || entityIn.motionZ < 0.5)) {
            entityIn.motionX *= 1.025;
            entityIn.motionZ *= 1.025;
        }
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity,
                                          double yToTest, Material materialIn, boolean testingHead) {
        return materialIn == Material.WATER || super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
    }

    @Override
    public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
        return entityIn instanceof EntitySeaGuardianCreeper || super.canEntitySpawn(state, entityIn);
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(world, pos, state, rand);
        if (!world.isRemote && world.provider.getDimensionType().getId() == TakumiWorldCore.TAKUMI_WORLD.getId()) {
            if (world.loadedEntityList.size() < 500 && rand.nextInt(1000) == 0) {
                Entity entity = rand.nextBoolean() ? new EntitySquidCreeper(world) : new EntitySeaGuardianCreeper(world);
                entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                world.spawnEntity(entity);
            }
        }
    }
}
