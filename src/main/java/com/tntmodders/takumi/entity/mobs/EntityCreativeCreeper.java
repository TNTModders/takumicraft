package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderVillagerCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class EntityCreativeCreeper extends EntityTakumiAbstractCreeper {

    public EntityCreativeCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderVillagerCreeper<>(manager);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, posX, posY, posZ, 4 * (this.getPowered() ? 2 : 1), false);
            this.buildHouse((int) this.posX, (int) this.posY, (int) this.posZ);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x000055;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "creativecreeper";
    }

    @Override
    public int getRegisterID() {
        return 218;
    }

    private void buildHouse(int ox, int oy, int oz) {
        int r = this.rand.nextInt(4);
        switch (r) {
            /*豆腐*/
            case 0:
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0) {
                                this.world.setBlockState(new BlockPos(x, y, z),
                                        TakumiBlockCore.CREEPER_GLASS.getDefaultState());
                            }
                        }
                    }
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 1, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 1, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 1, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 2, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 2, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 2, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                }
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                this.world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }
                break;

            /*TNTハウス*/
            case 1:
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                this.world.setBlockState(new BlockPos(x, y, z), Blocks.PLANKS.getDefaultState());
                            }
                        }
                    }
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 1, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 1, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 1, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 2, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 2, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 2, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                }
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                if (y >= oy + 4) {
                                    this.world.setBlockState(new BlockPos(x, y, z), Blocks.FIRE.getDefaultState());
                                } else {
                                    this.world.setBlockState(new BlockPos(x, y, z), Blocks.TNT.getDefaultState());
                                }
                            }
                        }
                    }
                }
                break;

            /*高性能爆弾*/
            case 2:
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                this.world.setBlockState(new BlockPos(x, y, z),
                                        TakumiBlockCore.CREEPER_BOMB.getDefaultState());
                            }
                        }
                    }
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 1, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 1, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 1, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 2, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 2, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 2, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                }
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                if (y >= oy + 4) {
                                    this.world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                                    EntityTNTPrimed tnt = new EntityTNTPrimed(this.world, x, y, z, this);
                                    this.world.spawnEntity(tnt);
                                } else {
                                    this.world.setBlockState(new BlockPos(x, y, z), Blocks.TNT.getDefaultState());
                                }
                            }
                        }
                    }
                }
                break;

            /*モンスターハウス*/
            case 3:
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlockHardness(this.world,
                                    new BlockPos(x, y, z)) > 0 || this.world.isAirBlock(new BlockPos(x, y, z))) {
                                this.world.setBlockState(new BlockPos(x, y, z),
                                        TakumiBlockCore.CREEPER_BRICK.getDefaultState());
                            }
                        }
                    }
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 1, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 1, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 1, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                }
                if (this.world.getBlockState(new BlockPos(ox + 4, oy + 2, oz - 3)).getBlockHardness(this.world,
                        new BlockPos(ox + 4, oy + 2, oz - 3)) > 0 ||
                        this.world.isAirBlock(new BlockPos(ox + 4, oy + 2, oz - 3))) {
                    this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                }
                for (int x = ox - 2; x <= ox + 2; x++) {
                    for (int z = oz - 2; z <= oz + 2; z++) {
                        for (int y = oy + 2; y >= oy + 1; y--) {
                            for (int i = 0; i < (this.getPowered() ? 4 : 2); i++) {
                                this.world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                                if (this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR &&
                                        this.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR &&
                                        this.rand.nextInt(5) == 0) {
                                    Class<? extends ITakumiEntity> clazz = TakumiEntityCore.getEntityList().get(
                                            this.rand.nextInt(TakumiEntityCore.getEntityList().size())).getClass();
                                    try {
                                        Entity creeper =
                                                (Entity) clazz.getConstructor(World.class).newInstance(this.world);
                                        if (((ITakumiEntity) creeper).takumiRank() == EnumTakumiRank.LOW ||
                                                ((ITakumiEntity) creeper).takumiRank() == EnumTakumiRank.MID) {
                                            creeper.world = this.world;
                                            creeper.setPosition(x, y, z);
                                            this.world.spawnEntity(creeper);
                                        }
                                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
    }
}
