package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderMirrorCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMirrorCreeper extends EntityTakumiAbstractCreeper {

    public EntityMirrorCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote && this.getAttackTarget() != null) {
            this.world.createExplosion(this, this.getAttackTarget().posX, this.getAttackTarget().posY,
                    this.getAttackTarget().posZ, this.getPowered() ? 5f : 2f, true);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
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
        return 0x333333;
    }

    @Override
    public int getPrimaryColor() {
        return 0xdddddd;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderMirrorCreeper(manager);
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "mirrorcreeper";
    }

    @Override
    public int getRegisterID() {
        return 74;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote && source.getTrueSource() instanceof EntityPlayer && this.getDistanceSqToEntity(source.getTrueSource()) < 9f) {
            ((EntityPlayer) source.getTrueSource()).inventory.mainInventory.forEach(
                    itemStack -> this.entityDropItem(itemStack, 0f));
            ((EntityPlayer) source.getTrueSource()).inventory.armorInventory.forEach(
                    itemStack -> this.entityDropItem(itemStack, 0f));
            ((EntityPlayer) source.getTrueSource()).inventory.offHandInventory.forEach(
                    itemStack -> this.entityDropItem(itemStack, 0f));
            ((EntityPlayer) source.getTrueSource()).inventory.clear();
        }
        super.onDeath(source);
    }
}
