package com.tntmodders.takumi.entity.mobs;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityBoarCreeper extends EntityPigCreeper {
    private boolean attackerFlg;
    private BlockPos target;
    private int expCounter;

    public EntityBoarCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.getAttackTarget() != null && !this.attackerFlg) {
                this.attackerFlg = true;
                Vec3d vec3d = new Vec3d(this.getAttackTarget().posX - this.posX, this.getAttackTarget().posY - this.posY, this.getAttackTarget().posZ - this.posZ);
                vec3d = vec3d.scale(70);
                this.target = this.getPosition().add(vec3d.x, vec3d.y, vec3d.z);
            }
            if (this.attackerFlg) {
                //TakumiCraftCore.LOGGER.info(this.getPosition().toString() + "/" + this.target.toString());
                this.setAttackTarget(null);
                this.setCreeperState(-1);
                this.tasks.taskEntries.clear();
                this.targetTasks.taskEntries.clear();
                this.getMoveHelper().setMoveTo(this.target.getX(), this.target.getY(), this.target.getZ(), 10);
                this.expCounter++;
                if (!this.world.isRemote) {
                    int i = 4 - this.expCounter / 200;
                    if (this.getPowered()) {
                        i = i * 2;
                    }
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, i, true);
                }
                if (this.expCounter > 800 || (this.expCounter > 60 && (this.getDistanceSq(this.target) < 0.2 || this.getDistanceSq(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ) < 0.05))) {
                    this.setDead();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().removeIf(pos -> pos.getY() < this.posY);
        event.getAffectedEntities().clear();
        return true;
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
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff6600;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "boarcreeper";
    }

    @Override
    public int getRegisterID() {
        return 290;
    }
}
