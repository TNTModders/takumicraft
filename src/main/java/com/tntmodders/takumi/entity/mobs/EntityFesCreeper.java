package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityFesCreeper extends EntityTakumiAbstractCreeper {
    private List<ITakumiEntity> entities = new ArrayList<>();
    private int ticksTargeted;

    public EntityFesCreeper(World worldIn) {
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
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff3333;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fescreeper";
    }

    @Override
    public int getRegisterID() {
        return 279;
    }

    @Override
    public int getPrimaryColor() {
        return 0x333355;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onGround) {
            this.jump();
        }
        this.rotationPitch += 1;
        if (!this.world.isRemote && !this.world.playerEntities.isEmpty() && this.world.playerEntities.stream().anyMatch(
                entityPlayer -> this.getDistanceSqToEntity(entityPlayer) < 50)) {
            this.ticksTargeted++;
            double x = this.posX + this.rand.nextInt(10) * 2 - 10;
            double z = this.posZ + this.rand.nextInt(10) * 2 - 10;
            if (this.ticksTargeted % 10 == 0) {
                ItemStack item = getItemFireworks();
                EntityFireworkRocket firework =
                        new EntityFireworkRocket(world, x, this.world.getHeight((int) x, (int) z), z, item);
                this.world.spawnEntity(firework);
            }
            if (this.ticksTargeted % 40 == 0) {
                EntityCreeper creeper = null;
                if (!entities.isEmpty()) {
                    try {
                        creeper = (EntityCreeper) entities.get(
                                this.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(
                                this.world);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (creeper != null) {
                    creeper.copyLocationAndAnglesFrom(this);
                    creeper.setPosition(x, this.world.getHeight((int) x, (int) z), z);
                    if (creeper instanceof EntityLiving) {
                        creeper.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
                    }
                    this.world.spawnEntity(creeper);
                    this.world.createExplosion(this, x, this.world.getHeight((int) x, (int) z), z, 0, false);
                }
            }
            if (this.ticksTargeted > 600) {
                this.setDead();
            }
        } else {
            this.ticksTargeted = 0;
        }
    }

    ItemStack getItemFireworks() {
        ItemStack item = new ItemStack(Items.FIREWORKS, 1);
        int i = this.rand.nextInt(0xffffff);
        try {
            item.setTagCompound(JsonToNBT.getTagFromJson(
                    "{Fireworks:{Flight:0,Explosions:[{Type:3,Flicker:1,Trail:1,Colors:[I;" + i + "]," +
                            "FadeColors:[I;" + i + "]}]}}"));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        return item;
    }
}