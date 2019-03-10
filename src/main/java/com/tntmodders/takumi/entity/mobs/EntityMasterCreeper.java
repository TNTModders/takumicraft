package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderVillagerCreeper;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMasterCreeper extends EntityTakumiAbstractCreeper {
    private List<ITakumiEntity> entities = new ArrayList<>();

    public EntityMasterCreeper(World worldIn) {
        super(worldIn);
        TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
            if (iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.MID ||
                    iTakumiEntity.takumiRank() == EnumTakumiRank.HIGH) {
                if (iTakumiEntity.getClass() != EntitySeaGuardianCreeper.class &&
                        iTakumiEntity.getClass() != EntitySquidCreeper.class) {
                    entities.add(iTakumiEntity);
                }
            }
        });
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
        return EnumTakumiType.WIND_M;
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
    public int getPrimaryColor() {
        return 0xfafffa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "mastercreeper";
    }

    @Override
    public int getRegisterID() {
        return 70;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                Map<EntityEquipmentSlot, Item> map = new HashMap<>();
                map.put(EntityEquipmentSlot.HEAD, Items.LEATHER_HELMET);
                map.put(EntityEquipmentSlot.CHEST, Items.LEATHER_CHESTPLATE);
                map.put(EntityEquipmentSlot.LEGS, Items.LEATHER_LEGGINGS);
                map.put(EntityEquipmentSlot.FEET, Items.LEATHER_BOOTS);
                map.keySet().forEach(entityEquipmentSlot -> {
                    if (!((EntityLivingBase) entity).getItemStackFromSlot(entityEquipmentSlot).isEmpty()) {
                        entity.entityDropItem(((EntityLivingBase) entity).getItemStackFromSlot(entityEquipmentSlot),
                                0f);
                    }
                    entity.setItemStackToSlot(entityEquipmentSlot, new ItemStack(map.get(entityEquipmentSlot), 1));
                });
            }
        });
        for (int i = 0; i < 6 * (this.getPowered() ? 2 : 1); i++) {
            EntityCreeper creeper = null;
            if (!entities.isEmpty()) {
                try {
                    creeper =
                            (EntityCreeper) entities.get(this.rand.nextInt(entities.size())).getClass().getConstructor(
                                    World.class).newInstance(this.world);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (creeper != null) {
                creeper.copyLocationAndAnglesFrom(this);
                if (creeper instanceof EntityLiving) {
                    creeper.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
                }
                this.world.spawnEntity(creeper);
            }
        }
        return true;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderVillagerCreeper<>(manager);
    }
}

