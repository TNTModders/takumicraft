package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.world.World;

public class EntityFireworksCreeper extends EntityTakumiAbstractCreeper {
    public EntityFireworksCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3f, false);
            for (int i = 0; i < (this.getPowered() ? 6 : 3); i++) {
                ItemStack item = this.getItemFireworks();
                double x = this.posX + i * 2 - i;
                double z = this.posZ + i * 2 - i;
                EntityFireworkRocket firework = new EntityFireworkRocket(world, x, this.world.getHeight((int) x, (int) z), z, item);

                this.world.spawnEntity(firework);
            }
        }
    }

    public ItemStack getItemFireworks() {
        ItemStack item = new ItemStack(Items.FIREWORKS, 1);
        try {
            item.setTagCompound(JsonToNBT.getTagFromJson("{Fireworks:{Flight:0,Explosions:[{Type:3,Flicker:1,Trail:1,Colors:[I;65280],FadeColors:[I;65280]}]}}"));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 11451419;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fireworkscreeper";
    }

    @Override
    public int getRegisterID() {
        return 22;
    }
}
