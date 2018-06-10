package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAntinomyCreeper extends EntityTakumiAbstractCreeper {

    public EntityAntinomyCreeper(World worldIn) {
        super(worldIn);
    }


    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.setFire(10);
            for (int i = 0; i < 4; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                this.world.spawnParticle(EnumParticleTypes.FLAME,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 50 : 25;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, i / 7, false);
            for (EntityLivingBase entityLivingBase : this.world.getEntities(EntityLivingBase.class,
                    input -> EntityAntinomyCreeper.this.getDistanceSqToEntity(input) < i * i)) {
                this.buildIce(entityLivingBase.getPosition());
                for (int i1 = 0; i1 < (this.getPowered() ? 5 : 2); i1++) {
                    this.buildIce(
                            entityLivingBase.getPosition().add(this.rand.nextInt(7) - 3, 0, this.rand.nextInt(7) - 3));
                }
            }
            this.buildFire((int) this.posX, (int) this.posY, (int) this.posZ);
        }
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
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff7777;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "antinomycreeper";
    }

    @Override
    public int getRegisterID() {
        return 236;
    }

    public void buildIce(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        y += this.rand.nextInt(4);
        int l = this.rand.nextInt(4) + 7;
        int i1 = l / 4 + this.rand.nextInt(2);

        if (i1 > 1 && this.rand.nextInt(60) == 0) {
            y += 10 + this.rand.nextInt(30);
        }

        int j1;
        int k1;
        int l1;

        for (j1 = 0; j1 < l; ++j1) {
            float f = (1.0F - j1 / (float) l) * i1;
            k1 = MathHelper.floor(f);

            for (l1 = -k1; l1 <= k1; ++l1) {
                float f1 = MathHelper.abs(l1) - 0.25F;

                for (int i2 = -k1; i2 <= k1; ++i2) {
                    float f2 = MathHelper.abs(i2) - 0.25F;

                    if ((l1 == 0 && i2 == 0 || f1 * f1 + f2 * f2 <= f * f) &&
                            (l1 != -k1 && l1 != k1 && i2 != -k1 && i2 != k1 || this.rand.nextFloat() <= 0.75F)) {
                        IBlockState blockState = world.getBlockState(new BlockPos(x + l1, y + j1, z + i2));
                        if (blockState.getBlockHardness(this.world, new BlockPos(x + l1, y + j1, z + i2)) >= 0) {
                            this.world.setBlockState(new BlockPos(x + l1, y + j1, z + i2),
                                    Blocks.PACKED_ICE.getDefaultState());
                            if (j1 != 0 && k1 > 1 &&
                                    blockState.getBlockHardness(this.world, new BlockPos(x + l1, y - j1, z + i2)) >=
                                            0) {
                                this.world.setBlockState(new BlockPos(x + l1, y - j1, z + i2),
                                        Blocks.PACKED_ICE.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        j1 = i1 - 1;

        if (j1 < 0) {
            j1 = 0;
        } else if (j1 > 1) {
            j1 = 1;
        }

        for (int j2 = -j1; j2 <= j1; ++j2) {
            k1 = -j1;

            while (k1 <= j1) {
                l1 = y - 1;
                int k2 = 50;

                if (Math.abs(j2) == 1 && Math.abs(k1) == 1) {
                    k2 = this.rand.nextInt(5);
                }

                while (true) {
                    if (l1 > 25) {
                        IBlockState block1 = world.getBlockState(new BlockPos(x + j2, l1, z + k1));

                        if (block1.getMaterial() == Material.AIR || block1 == Blocks.DIRT || block1 == Blocks.SNOW ||
                                block1 == Blocks.ICE || block1 == Blocks.PACKED_ICE) {
                            this.world.setBlockState(new BlockPos(x + j2, l1, z + k1),
                                    Blocks.PACKED_ICE.getDefaultState());
                            --l1;
                            --k2;

                            if (k2 <= 0) {
                                l1 -= this.rand.nextInt(5) + 1;
                                k2 = this.rand.nextInt(5);
                            }
                            continue;
                        }
                    }

                    ++k1;
                    break;
                }
            }
        }
    }

    protected void buildFire(int ox, int oy, int oz) {
        int blockX, blockY, blockZ;
        int searchRange = 10 * 2;
        for (int x = -1 * searchRange; x < searchRange; ++x) {
            blockX = x + ox;
            for (int y = -1 * searchRange; y < searchRange; ++y) {
                blockY = y + oy;
                for (int z = -1 * searchRange; z < searchRange; ++z) {
                    blockZ = z + oz;

                    BlockPos pos = new BlockPos(blockX, blockY, blockZ);

                    if (world.getBlockState(pos.down()).getBlock() != Blocks.AIR) {
                        if (world.isAirBlock(pos)) {

                            world.setBlockState(pos, Blocks.FIRE.getDefaultState());

                        }

                    }
                }
            }
        }
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isFireDamage()) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    public int getPrimaryColor() {
        return 0x7777ff;
    }
}
