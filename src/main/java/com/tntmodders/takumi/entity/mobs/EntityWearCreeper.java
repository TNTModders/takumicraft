package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Enchantments;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityWearCreeper extends EntityTakumiAbstractCreeper {

    public EntityWearCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            entity.getArmorInventoryList().forEach(itemStack -> {
                itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
                itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
            });
        });
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff8800;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "wearcreeper";
    }

    @Override
    public int getRegisterID() {
        return 53;
    }
}

