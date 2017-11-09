package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderCeruleanCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityCeruleanCreeper extends EntityTakumiAbstractCreeper {
    public EntityCeruleanCreeper(World worldIn) {
        super(worldIn);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    @Override
    public boolean canRegister() {
        return Item.REGISTRY.containsKey(new ResourceLocation("japaricraftmod", "darksandstar"));
        //return true;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
            }
        }

        for (BlockPos pos : event.getAffectedBlocks()) {
            if (!this.world.isAirBlock(pos)) {
                this.world.setBlockState(pos, TakumiBlockCore.CREEPER_SANDSTAR_LOW.getDefaultState());
            }
        }

        event.getAffectedBlocks().clear();
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x000000;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderCeruleanCreeper<>(manager);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.inWater) {
            this.setDead();
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX + 0.5, this.posY, this.posZ + 0.5, 2f, true);
            }
            this.world.setBlockState(this.getPosition(), Blocks.OBSIDIAN.getDefaultState());
            this.world.setBlockState(this.getPosition().up(), Blocks.OBSIDIAN.getDefaultState());
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote) {
            this.dropItem(Item.REGISTRY.getObject(
                    new ResourceLocation("japaricraftmod", "darksandstar")), 1);
        }
        super.onDeath(cause);
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
        return EnumTakumiType.CERULEAN;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x007700;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "ceruleancreeper";
    }

    @Override
    public int getRegisterID() {
        return 700;
    }
}
