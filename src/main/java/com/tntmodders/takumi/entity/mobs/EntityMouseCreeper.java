package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderMouseCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMouseCreeper extends EntityTakumiAbstractCreeper {

    public EntityMouseCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int yaw = 0; yaw <= 360; yaw += 5) {
                for (int pitch = 0; pitch <= 360; pitch += 5) {
                    if (this.rand.nextInt(this.getPowered() ? 2 : 5) == 0) {
                        EntityTakumiArrow arrow = ((EntityTakumiArrow) TakumiItemCore.TAKUMI_ARROW_HA.createArrow(this.world, new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA), this));
                        arrow.power = 0;
                        arrow.setPosition(this.posX, this.posY + 1, this.posZ);
                        arrow.setAim(this, yaw, pitch, 0f, 1, 0.5f);
                        this.world.spawnEntity(arrow);
                    }
                }
            }
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
        return 0xaaaaaa;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaaccaa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "mousecreeper";
    }

    @Override
    public int getRegisterID() {
        return 291;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderMouseCreeper<>(manager);
    }
}
