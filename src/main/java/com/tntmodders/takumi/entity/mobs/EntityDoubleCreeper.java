package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityDoubleCreeper extends EntityTakumiAbstractCreeper {

    public EntityDoubleCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                try {
                    if (entity instanceof EntityLivingBase && (!(entity instanceof EntityPlayer)) &&
                            !(entity instanceof EntityDoubleCreeper)) {
                        EntityLivingBase base =
                                ((EntityLivingBase) EntityRegistry.getEntry(entity.getClass()).newInstance(this.world));
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        entity.writeToNBT(tagCompound);
                        tagCompound.setUniqueId("UUID", MathHelper.getRandomUUID());
                        base.readFromNBT(tagCompound);
                        base.copyLocationAndAnglesFrom(entity);
                        this.world.spawnEntity(base);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (entity instanceof EntityItem) {
                    EntityItem item = new EntityItem(this.world);
                    item.copyLocationAndAnglesFrom(entity);
                    item.setItem(((EntityItem) entity).getItem());
                    this.world.spawnEntity(item);
                }
            });
        }
        event.getAffectedEntities().removeIf(entity -> !(entity instanceof EntityPlayer));
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getPrimaryColor() {
        return 0xffaaaa;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "doublecreeper";
    }

    @Override
    public int getRegisterID() {
        return 79;
    }
}
