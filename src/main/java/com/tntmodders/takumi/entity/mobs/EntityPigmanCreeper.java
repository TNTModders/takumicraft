package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderPigmanCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityPigmanCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityPigmanCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityPigZombie && entity.getDistanceSqToEntity(this) < 1000 && this.getAttackTarget() != null) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this.getAttackTarget()), 0);
                    ((EntityLiving) entity).setAttackTarget(this.getAttackTarget());
                }
            });
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x661166;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "pigmancreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 251;
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1);
        }
        super.onDeath(source);
    }
    
    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 2, 3, 10, EnumCreatureType.MONSTER, Biomes.HELL);
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x004400;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderPigmanCreeper <>(manager);
    }
}
