package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.entity.EntityTakumiAbstranctCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

public class EntityLightCreeper extends EntityTakumiAbstranctCreeper {
    public EntityLightCreeper(World worldIn) {
        super(worldIn);
    }

    public float getBrightness() {
        return 100000f;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return ((int) this.getBrightness());
    }

    @Override
    public void onUpdate() {
        this.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10, 0));
        super.onUpdate();
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.GLOWING, 400, 0));
            }
        }
        float power = this.getPowered() ? 6f : 3f;
        try {
            Field field = TakumiASMNameMap.getField(Explosion.class, "size");
            field.setAccessible(true);
            power = field.getFloat(event.getExplosion());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (power > 0.5) {
            for (BlockPos pos : event.getAffectedBlocks()) {

                if (!this.world.isRemote && this.world.getBlockState(pos).getBlock().getLightValue(this.world.getBlockState(pos)) > 0.5f &&
                        TakumiUtils.takumiGetHardness(this.world.getBlockState(pos).getBlock()) != -1) {
                    this.world.setBlockToAir(pos);
                    this.world.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power - 0.2f, true);
                }
            }
        }
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 16776960;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "lightcreeper";
    }

    @Override
    public int getRegisterID() {
        return 10;
    }
}
