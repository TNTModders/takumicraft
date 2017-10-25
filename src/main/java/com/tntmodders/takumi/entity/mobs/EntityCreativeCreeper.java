package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderVillagerCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
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
    public RenderLiving getRender(RenderManager manager) {
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
            case 0: {
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            this.world.setBlockState(new BlockPos(x, y, z), TakumiBlockCore.CREEPER_GLASS.getDefaultState());
                        }
                    }
                }
                this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
                            this.world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                        }
                    }
                }
                break;
            }

            /*TNTハウス*/
            case 1: {
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            this.world.setBlockState(new BlockPos(x, y, z), Blocks.PLANKS.getDefaultState());
                        }
                    }
                }
                this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
                            if (y >= oy + 4) {
                                this.world.setBlockState(new BlockPos(x, y, z), Blocks.FIRE.getDefaultState());
                            } else {
                                this.world.setBlockState(new BlockPos(x, y, z), Blocks.TNT.getDefaultState());
                            }
                        }
                    }
                }
                break;
            }

            /*高性能爆弾*/
            case 2: {
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            this.world.setBlockState(new BlockPos(x, y, z), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
                        }
                    }
                }
                this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 1; y <= oy + 4; y++) {
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
                break;
            }

            /*モンスターハウス*/
            case 3: {
                for (int x = ox - 4; x <= ox + 4; x++) {
                    for (int z = oz - 4; z <= oz + 4; z++) {
                        for (int y = oy; y <= oy + 5; y++) {
                            this.world.setBlockState(new BlockPos(x, y, z), TakumiBlockCore.CREEPER_BRICK.getDefaultState());
                        }
                    }
                }
                this.world.setBlockState(new BlockPos(ox + 4, oy + 1, oz - 3), Blocks.AIR.getDefaultState());
                this.world.setBlockState(new BlockPos(ox + 4, oy + 2, oz - 3), Blocks.AIR.getDefaultState());
                for (int x = ox - 3; x <= ox + 3; x++) {
                    for (int z = oz - 3; z <= oz + 3; z++) {
                        for (int y = oy + 3; y >= oy + 1; y--) {
                            this.world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                            if (this.world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR && this.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.AIR && this.rand.nextInt(5) == 0) {
                                Class clazz = (TakumiEntityCore.entityList.get(this.rand.nextInt(TakumiEntityCore.entityList.size()))).getClass();
                                try {
                                    Entity creeper = (Entity) (clazz.getConstructor(World.class).newInstance(this.world));
                                    creeper.world = this.world;
                                    creeper.setPosition(x, y, z);
                                    this.world.spawnEntity(creeper);
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
