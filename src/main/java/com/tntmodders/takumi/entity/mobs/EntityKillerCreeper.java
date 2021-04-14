package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityKillerCreeper extends EntityTakumiAbstractCreeper {

    static final IBlockState STONE = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.MOSSY_META);
    static final IBlockState IRON =
            Blocks.IRON_BARS.getDefaultState().withProperty(BlockPane.EAST, true).withProperty(BlockPane.NORTH,
                    false).withProperty(BlockPane.SOUTH, false).withProperty(BlockPane.WEST, true);
    static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
    static final IBlockState web = Blocks.WEB.getDefaultState();

    public EntityKillerCreeper(World worldIn) {
        super(worldIn);
    }

    //@TODO Unbreakable
    @Override
    public void takumiExplode() {
        if (this.getAttackTarget() == null) {
            this.setAttackTarget(this.world.getClosestPlayerToEntity(this, 100));
        }
        if (this.getAttackTarget() != null) {
            BlockPos pos = this.getAttackTarget().getPosition();
            for (int y = 0; y <= 6; y++) {
                for (int x = -1; x <= 8 - y; x++) {
                    for (int z = -1; z <= 1; z++) {
                        this.setBlockKC(this.world, pos.add(x, y, z), STONE);
                        if (z == -1 || z == 1) {
                            this.setBlockKC(this.world, pos.add(x, y + 1, z), STONE);
                            this.setBlockKC(this.world, pos.add(x, y + 2, z), IRON);
                        }
                    }
                }
            }
            this.setBlockKC(this.world, pos.add(0, 1, -1), IRON);
            this.setBlockKC(this.world, pos.add(0, 1, 0), LAVA);
            this.setBlockKC(this.world, pos.add(0, 1, 1), IRON);
            this.setBlockKC(this.world, pos.add(0, 2, 0), Blocks.AIR.getDefaultState());
            this.setBlockKC(this.world, pos.add(0, 3, 0), web);
            this.setBlockKC(this.world, pos.add(0, 4, 0), web);
            this.setBlockKC(this.world, pos.add(0, 3, -1), IRON);
            this.setBlockKC(this.world, pos.add(0, 4, -1), IRON);
            this.setBlockKC(this.world, pos.add(0, 3, 1), IRON);
            this.setBlockKC(this.world, pos.add(0, 4, 1), IRON);
            this.setBlockKC(this.world, pos.add(0, 5, 0), Blocks.AIR.getDefaultState());
            this.setBlockKC(this.world, pos.add(0, 6, 0), Blocks.AIR.getDefaultState());
            this.setBlockKC(this.world, pos.add(-1, 7, -1), STONE);
            this.setBlockKC(this.world, pos.add(-1, 7, 1), STONE);
            this.setBlockKC(this.world, pos.add(-1, 7, 0), STONE);
            this.setBlockKC(this.world, pos.add(0, 7, -1), STONE);
            this.setBlockKC(this.world, pos.add(0, 7, 0), Blocks.AIR.getDefaultState());
            this.setBlockKC(this.world, pos.add(0, 7, 1), STONE);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    this.setBlockKC(this.world, pos.add(x, 8, z), STONE);
                }
            }
            this.setBlockKC(this.world, pos.add(1, 8, 0), Blocks.GLOWSTONE.getDefaultState());

            this.getAttackTarget().setPositionAndUpdate(pos.getX(), pos.getY() + 6, pos.getZ());
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0000;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "killercreeper";
    }

    @Override
    public int getRegisterID() {
        return 252;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 2, 3, 10,
                EnumCreatureType.MONSTER, Biomes.HELL);
    }

    @Override
    public int getPrimaryColor() {
        return 0x333333;
    }

    protected void setBlockKC(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlockHardness(world, pos) != -1 || world.isAirBlock(pos)) {
            TakumiUtils.setBlockStateProtected(world, pos, state);
        }
    }
}
