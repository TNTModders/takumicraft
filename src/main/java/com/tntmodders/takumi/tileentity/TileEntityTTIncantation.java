package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.block.BlockTTIncantation;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.mobs.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityTTIncantation extends TileEntity implements ITickable {

    private int tick;

    @Override
    public void update() {
        if (!this.world.isRemote) {
            try {
                tick++;
                if (tick % 100 == 0) {
                    IBlockState state = this.world.getBlockState(this.getPos());
                    if (state.getBlock() == TakumiBlockCore.TT_INCANTATION) {
                        switch (state.getValue(BlockTTIncantation.INCANTATION)) {
                            case THUNDER: {
                                if (this.world.rand.nextInt(5) == 0) {
                                    for (int i = 0; i < 10; i++) {
                                        EntityLightningBolt bolt = new EntityLightningBolt(this.world, this.getPos().getX() + this.world.rand.nextInt(15) - 7,
                                                this.getPos().getY(), this.getPos().getZ() + this.world.rand.nextInt(15) - 7, false);
                                        this.world.addWeatherEffect(bolt);
                                        this.world.spawnEntity(bolt);
                                    }
                                }
                                break;
                            }
                            case WATER: {
                                if (this.world.getEntitiesWithinAABB(EntityCreeper.class, new AxisAlignedBB(this.getPos().add(-8, 0, -8), this.getPos().add(8, 15, 8))).size() < 15) {
                                    EntityCreeper creeper = new EntitySquidCreeper(this.world);
                                    switch (this.world.rand.nextInt(4)) {
                                        case 1: {
                                            creeper = new EntityFishCreeper(this.world);
                                            break;
                                        }
                                        case 2: {
                                            creeper = new EntityBigFishCreeper(this.world);
                                            break;
                                        }
                                        case 3: {
                                            creeper = new EntitySeaGuardianCreeper(this.world);
                                            break;
                                        }
                                    }
                                    if (((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                                            ((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.MID) {
                                        creeper.setPosition(this.getPos().getX() + this.world.rand.nextInt(13) - 6, this.getPos().getY() + this.world.rand.nextInt(15),
                                                this.getPos().getZ() + this.world.rand.nextInt(13) - 6);
                                        this.world.spawnEntity(creeper);

                                    }
                                }
                                break;
                            }
                            case LAVA: {
                                if (this.world.rand.nextInt(5) == 0 &&
                                        this.world.getEntitiesWithinAABB(EntityCreeper.class, new AxisAlignedBB(this.getPos().add(-8, 0, -8), this.getPos().add(8, 15, 8))).size() < 20) {
                                    EntityCreeper creeper = this.world.rand.nextBoolean() ? new EntityRushCreeper(this.world) : new EntityChaseCreeper(this.world);
                                    if (((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                                            ((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.MID) {
                                        creeper.setPosition(this.getPos().getX() + this.world.rand.nextInt(13) - 6, this.getPos().getY() + this.world.rand.nextInt(15),
                                                this.getPos().getZ() + this.world.rand.nextInt(13) - 6);
                                        this.world.spawnEntity(creeper);

                                    }
                                }
                                break;
                            }
                            case EX: {
                                //未実装
                                break;
                            }
                            default: {
                                if (this.world.getEntitiesWithinAABB(EntityCreeper.class, new AxisAlignedBB(this.getPos().add(-8, 0, -8), this.getPos().add(8, 15, 8))).size() < 20) {
                                    Class<? extends ITakumiEntity> clazz = TakumiEntityCore.getEntityList().get(this.world.rand.nextInt(TakumiEntityCore.getEntityList().size())).getClass();
                                    Entity creeper = (Entity) clazz.getConstructor(World.class).newInstance(this.world);
                                    if (((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                                            ((ITakumiEntity) creeper).takumiRank() == ITakumiEntity.EnumTakumiRank.MID) {
                                        creeper.world = this.world;
                                        creeper.setPosition(this.getPos().getX() + this.world.rand.nextInt(13) - 6, this.getPos().getY() + this.world.rand.nextInt(15),
                                                this.getPos().getZ() + this.world.rand.nextInt(13) - 6);
                                        this.world.spawnEntity(creeper);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
