package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.client.render.RenderShadowCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityShadowCreeper extends EntityTakumiAbstractCreeper {

    public final List<Vec3d> shadowList = new ArrayList<>();

    public EntityShadowCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff55ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "shadowcreeper";
    }

    @Override
    public int getRegisterID() {
        return 51;
    }

    @Override
    public int getPrimaryColor() {
        return 0x113311;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderShadowCreeper<>(manager);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        int t = this.getTimeSinceIgnited();
        if (t > 0 && t % 3 == 0 && this.getCreeperState() > 0) {
            for (int i = this.shadowList.size(); i < t * t; i++) {
                this.shadowList.add(new Vec3d(rand.nextInt(t * 2) - rand.nextInt(t * 2),
                        rand.nextInt(t * 2) / 3 - rand.nextInt(t * 2) / 3, rand.nextInt(t * 2) - rand.nextInt(t * 2)));
            }
        }
    }

    public int getTimeSinceIgnited() {
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
            field.setAccessible(true);
            return (int) field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
