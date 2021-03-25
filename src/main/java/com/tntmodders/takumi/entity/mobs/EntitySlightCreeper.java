package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.UUID;

public class EntitySlightCreeper extends EntityTakumiAbstractCreeper {

    protected static final UUID SPEED_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89C5");
    protected static final UUID HEALTH_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89C4");

    public EntitySlightCreeper(World worldIn) {
        super(worldIn);
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
        return 0x003333;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "slightcreeper";
    }

    @Override
    public int getRegisterID() {
        return 94;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                if (!((EntityLivingBase) entity).getHeldItemMainhand().getAttributeModifiers(EntityEquipmentSlot.MAINHAND)
                        .containsKey(SharedMonsterAttributes.MOVEMENT_SPEED.getName())) {
                    ((EntityLivingBase) entity).getHeldItemMainhand().addAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
                            new AttributeModifier(SPEED_MODIFIER, "Speed modifier", 0.025f, 0), EntityEquipmentSlot.MAINHAND);
                    ((EntityLivingBase) entity).getHeldItemMainhand().addAttributeModifier(SharedMonsterAttributes.MAX_HEALTH.getName(),
                            new AttributeModifier(HEALTH_MODIFIER, "Health modifier", -4f, 0), EntityEquipmentSlot.MAINHAND);
                    ((EntityLivingBase) entity).getHeldItemMainhand().setStackDisplayName(TakumiUtils.takumiTranslate("entity.slightcreeper.message")
                            + ((EntityLivingBase) entity).getHeldItemMainhand().getDisplayName());
                }
            }
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x991111;
    }
}
