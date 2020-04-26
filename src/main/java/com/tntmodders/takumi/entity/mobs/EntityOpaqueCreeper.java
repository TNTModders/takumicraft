package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityOpaqueCreeper extends EntityTakumiAbstractCreeper {

    public EntityOpaqueCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 5; ++i) {
                this.world.spawnParticle(EnumParticleTypes.END_ROD,
                        this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY + this.rand.nextDouble() * (double) this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public void additionalSpawn() {
        EntityRegistry.addSpawn(this.getClass(), 1, 1, 5, EnumCreatureType.MONSTER,
                Biomes.SKY);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (this.world.isAirBlock(pos)) {
                blockPosList.add(pos);
            }
            event.getWorld().setBlockState(pos, Blocks.PURPUR_BLOCK.getDefaultState());
        }
        for (BlockPos pos : event.getAffectedBlocks()) {
            for (int i = 0; i < 6; i++) {
                BlockPos newPos = pos.offset(EnumFacing.VALUES[i]);
                if (event.getWorld().getBlockState(newPos).getBlock() != Blocks.PURPUR_BLOCK &&
                        event.getWorld().getBlockState(newPos).getBlockHardness(world, newPos) != -1 &&
                        (event.getWorld().getBlockState(newPos).getBlock().getExplosionResistance(world, newPos,
                                event.getExplosion().getExplosivePlacedBy(), event.getExplosion()) <
                                Blocks.OBSIDIAN.getDefaultState().getBlock().getExplosionResistance(world, newPos,
                                        event.getExplosion().getExplosivePlacedBy(), event.getExplosion()) ||
                                event.getWorld().isAirBlock(newPos))) {
                    int r = this.getPowered() ? 4 : 2;
                    if (this.rand.nextBoolean() &&
                            Math.abs(newPos.getX() - this.posX) < r && Math.abs(newPos.getZ() - this.posZ) < r && newPos.getY() - this.posY < r * 2) {
                        EntityShulker shulker = new EntityShulker(this.world);
                        shulker.setHealth(2);
                        shulker.addPotionEffect(new PotionEffect(TakumiPotionCore.EP, 600));
                        shulker.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        this.world.spawnEntity(shulker);
                    } else {
                        event.getWorld().setBlockState(newPos, Blocks.END_STONE.getDefaultState());
                    }
                }
            }
        }
        blockPosList.forEach(blockPos -> this.world.setBlockToAir(blockPos));
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        this.world.setBlockState(this.getPosition(), Blocks.END_ROD.getDefaultState());
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityShulker);
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x9900bb;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "opaquecreeper";
    }

    @Override
    public int getRegisterID() {
        return 288;
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_SHULKER_BOX);
    }
}
