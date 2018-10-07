package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;

public class EntitySleeperCreeper extends EntityTakumiAbstractCreeper {

    public EntitySleeperCreeper(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.BED;
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world, pos, null) ?
                10 : super.getBlockPathWeight(pos);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                    if (pos != null &&
                            this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world,
                                    pos, entity)) {
                        for (int i = 0; i < (this.getPowered() ? 10 : 5); i++) {
                            EntityCreeper creeper = this.rand.nextBoolean() ? new EntityBedCreeper(this.world) :
                                    new EntityReturnCreeper(this.world);
                            creeper.setPosition(pos.getX(), pos.getY() + 0.6, pos.getZ());
                            creeper.onStruckByLightning(null);
                            this.world.spawnEntity(creeper);
                        }
                    }
                }
            });
            event.getAffectedEntities().clear();
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x006600;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.entityDropItem(new ItemStack(Items.SKULL, 1, 4), 0.5f);
        }
        super.onDeath(source);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 8 : 5, false);
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
        return 0x88ff88;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "sleepercreeper";
    }

    @Override
    public int getRegisterID() {
        return 277;
    }
}
