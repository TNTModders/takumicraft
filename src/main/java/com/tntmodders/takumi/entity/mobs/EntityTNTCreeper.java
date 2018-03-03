package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiTNTPrimed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityTNTCreeper extends EntityTakumiAbstractCreeper {

    public EntityTNTCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_MD;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "tntcreeper";
    }

    @Override
    public int getRegisterID() {
        return 404;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                this.world.setBlockState(pos, this.getPowered() ? TakumiBlockCore.TAKUMI_TNT.getDefaultState() :
                        Blocks.TNT.getDefaultState());
                if (this.rand.nextInt(5) == 0) {
                    Entity entity =
                            this.getPowered() ? new EntityTakumiTNTPrimed(this.world) : new EntityTNTPrimed(this.world);
                    entity.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
                    this.world.spawnEntity(entity);
                }
            }

        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xff0000;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(TakumiBlockCore.TAKUMI_TNT), this.rand.nextInt(5));
        }
        super.onDeath(source);
    }
}
