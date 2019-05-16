package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.*;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public class EntityMeatCreeper extends EntityTakumiAbstractCreeper {
    private static final List<Class<? extends EntityLivingBase>> LIST = new ArrayList<>();

    static {
        LIST.add(EntityPig.class);
        LIST.add(EntityCow.class);
        LIST.add(EntityChicken.class);
        LIST.add(EntityRabbit.class);
        LIST.add(EntitySheep.class);
        LIST.add(EntityZombie.class);
        LIST.add(EntityZombieHorse.class);
        LIST.add(EntityPigZombie.class);
        LIST.add(EntityHusk.class);
        LIST.add(EntityZombieVillager.class);
    }

    public EntityMeatCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int index = this.rand.nextInt(LIST.size());
            for (int i = 0; i < (this.getPowered() ? 12 : 6); i++) {
                EntityLivingBase base =
                        ((EntityLivingBase) EntityRegistry.getEntry(LIST.get(index)).newInstance(this.world));
                base.copyLocationAndAnglesFrom(this);
                this.world.spawnEntity(base);
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0000;
    }

    @Override
    public int getPrimaryColor() {
        return 0xff3333;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "meatcreeper";
    }

    @Override
    public int getRegisterID() {
        return 80;
    }
}
