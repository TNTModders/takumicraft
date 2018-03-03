package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.block.BlockTakumiAcid;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;

public class EntityAcidCreeper extends EntityTakumiAbstractCreeper {

    public EntityAcidCreeper(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(TakumiBlockCore.ACID_BLOCK);
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
        return EnumTakumiType.GROUND_D;
    }

    @Override
    public int getExplosionPower() {
        return 2;
    }

    @Override
    public int getSecondaryColor() {
        return 0x006600;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "acidcreeper";
    }

    @Override
    public int getRegisterID() {
        return 239;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            if (!this.world.isAirBlock(pos)) {
                event.getWorld().setBlockState(pos,
                        TakumiBlockCore.ACID_BLOCK.getDefaultState().withProperty(BlockTakumiAcid.META, 0));
            }
        });
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x111111;
    }
}
