package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityLuckCreeper extends EntityTakumiAbstractCreeper {

    public EntityLuckCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.GRASS_M;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff8888;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "luckcreeper";
    }

    @Override
    public int getRegisterID() {
        return 234;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            for (Entity entity : event.getAffectedEntities()) {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).heal(((EntityLivingBase) entity).getMaxHealth());
                    ((EntityLivingBase) entity).curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LUCK, 600, 1));
                }
            }
        }
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (cause.getTrueSource() != null && cause.getTrueSource() instanceof EntityLivingBase &&
                !(cause.getTrueSource() instanceof EntityPlayer &&
                        ((EntityPlayer) cause.getTrueSource()).isCreative())) {
            cause.getTrueSource().attackEntityFrom(
                    new EntityDamageSource("takumicraft.luck", this).setDamageIsAbsolute(),
                    ((EntityLivingBase) cause.getTrueSource()).getMaxHealth() + 1);
        }
        super.onDeath(cause);
    }
}
