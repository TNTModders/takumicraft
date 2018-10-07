package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderTriZombieCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTriZombieCreeper extends EntityZombieCreeper {

    public EntityTriZombieCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            for (int i = 0; i < 3; i++) {
                EntityZombieCreeper zombieCreeper = new EntityZombieCreeper(this.world);
                zombieCreeper.copyLocationAndAnglesFrom(this);
                this.world.spawnEntity(zombieCreeper);
            }
        }
        return true;
    }

    @Override
    public int getExplosionPower() {
        return super.getExplosionPower() * 3;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTriZombieCreeper(manager);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getSecondaryColor() {
        return 0x888888;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "trizombiecreeper";
    }

    @Override
    public int getRegisterID() {
        return 67;
    }
}
