package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageYMSMove;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityYMS extends EntityFlying {

    public int attackModeTick;
    public boolean isAttackMode;

    public EntityYMS(World worldIn) {
        super(worldIn);
        this.setSize(10, 2);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    public double getMountedYOffset() {
        return this.height * 0.25;
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            if (this.getControllingPassenger() instanceof EntityPlayerSP) {
                if (((EntityPlayerSP) this.getControllingPassenger()).movementInput.forwardKeyDown) {
                    TakumiPacketCore.INSTANCE.sendToServer(new MessageYMSMove((byte) 1));
                }

                if (this.isAttackMode && this.attackModeTick < 20) {
                    this.attackModeTick++;
                } else if (this.attackModeTick > 0) {
                    this.attackModeTick--;
                }
            }
            this.rotationYaw = this.rotationYawHead = ((EntityPlayer) this.getControllingPassenger()).rotationYawHead;
            this.rotationPitch = this.getControllingPassenger().rotationPitch;
        }
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return this.getPassengers().get(0);
    }
}
