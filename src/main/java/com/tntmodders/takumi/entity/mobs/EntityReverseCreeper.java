package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityReverseCreeper extends EntityTakumiAbstractCreeper {

    public EntityReverseCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB),
                    this.rand.nextInt(32) * (this.getPowered() ? 2 : 1));
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "reversecreeper";
    }

    @Override
    public int getRegisterID() {
        return 223;
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (cause.getImmediateSource() instanceof EntityPlayer && !this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 12 : 6, true);
        }
        super.onDeath(cause);
    }

    @Override
    public int getPrimaryColor() {
        return 0x55ff00;
    }
}
