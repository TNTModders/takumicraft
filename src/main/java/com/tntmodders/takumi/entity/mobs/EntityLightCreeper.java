package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLightCreeper extends EntityTakumiAbstractCreeper {
    public EntityLightCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
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

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.GLOWING, 400, 0));
            }
        }
        float power = this.getPowered() ? 6f : 3f;
        if (event.getExplosion() instanceof TakumiExplosion) {
            power = ((TakumiExplosion) event.getExplosion()).getSize();
        }
        if (power > 0.5) {
            for (BlockPos pos : event.getAffectedBlocks()) {

                if (!this.world.isRemote && this.world.getBlockState(pos).getBlock().getLightValue(this.world.getBlockState(pos)) > 0.5f &&
                        TakumiUtils.takumiGetBlockResistance(this, this.world.getBlockState(pos), pos) != -1) {
                    this.world.setBlockToAir(pos);
                    TakumiUtils.takumiCreateExplosion(this.world, this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power - 0.2f, false, true);
                }
            }
        }
        return true;
    }
}
