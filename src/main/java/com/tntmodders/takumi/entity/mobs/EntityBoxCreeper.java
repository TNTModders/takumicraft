package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderBoxCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityBoxCreeper extends EntityTakumiAbstractCreeper {

    public EntityBoxCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    @Override
    public int getPrimaryColor() {
        return 0x00bbbb;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderBoxCreeper<>(manager);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
    }

    @Override
    public void takumiExplode() {
        List<BlockPos> posList = new ArrayList<>();
        this.world.loadedTileEntityList.forEach(tileEntity -> {
            if (tileEntity instanceof IInventory &&
                    tileEntity.getDistanceSq(this.posX, this.posY, this.posZ) < (this.getPowered() ? 2500 : 1000)) {
                posList.add(tileEntity.getPos());
            }
        });
        if (!posList.isEmpty()) {
            posList.forEach(
                    pos -> this.world.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            (this.getPowered() ? this.getExplosionPower() * 2 : this.getExplosionPower()), true));
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ffcc;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "boxcreeper";
    }

    @Override
    public int getRegisterID() {
        return 64;
    }
}
