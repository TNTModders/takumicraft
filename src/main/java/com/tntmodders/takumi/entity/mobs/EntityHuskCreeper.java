package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderZombieCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.init.Biomes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHuskCreeper extends EntityZombieCreeper {
    private static final Biome[] biomes = {Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.MUTATED_DESERT};

    public EntityHuskCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.HUNGER, 400, this.getPowered() ? 2 : 0));
            }
        }
        if (!this.world.isRemote) {
            for (int i = 0; i < 3 * this.world.getDifficulty().getDifficultyId(); i++) {
                EntityHusk husk = new EntityHusk(this.world);
                husk.copyLocationAndAnglesFrom(this);
                this.world.spawnEntity(husk);
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public RenderLiving getRender(RenderManager manager) {
        return new RenderZombieCreeper(manager);
    }

    @Override
    public String getRegisterName() {
        return "huskcreeper";
    }

    @Override
    public int getRegisterID() {
        return 203;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    public int getSecondaryColor() {
        return 15983273;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }
}
