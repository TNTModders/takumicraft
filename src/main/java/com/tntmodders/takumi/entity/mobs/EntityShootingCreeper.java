package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Lists;
import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiChocolateBall;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

public class EntityShootingCreeper extends EntityTakumiAbstractCreeper {

    //true→アイテム投射 false→アイテム数に応じて爆破ダメージ増加&爆破終了後アイテム抽選
    private static final DataParameter<Boolean> TYPE =
            EntityDataManager.createKey(EntityShootingCreeper.class, DataSerializers.BOOLEAN);

    private static final DataParameter<ItemStack> ITEM =
            EntityDataManager.createKey(EntityShootingCreeper.class, DataSerializers.ITEM_STACK);

    public EntityShootingCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 10 : 5, false);
            if (!this.dataManager.get(TYPE)) {
                if (useChoco(this.world)) {
                    this.dataManager.set(ITEM, new ItemStack(TakumiItemCore.TAKUMI_CHOCO_BALL, 1));
                } else {
                    List<IRecipe> list = Lists.newArrayList(CraftingManager.REGISTRY.iterator());
                    IRecipe iRecipe = list.get(this.rand.nextInt(list.size()));
                    this.dataManager.set(ITEM, iRecipe.getRecipeOutput());
                }
            }
        }
    }

    public static boolean useChoco(World world) {
        Calendar calendar = world.getCurrentDate();
        return (calendar.get(Calendar.MONTH) + 1 == 2 || calendar.get(Calendar.MONTH) + 1 == 3) &&
                calendar.get(Calendar.DATE) == 14;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x884444;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "shootingcreeper";
    }

    @Override
    public int getRegisterID() {
        return 255;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, false);
        this.dataManager.register(ITEM, new ItemStack(Blocks.TNT, 1));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("type", this.dataManager.get(TYPE));
        compound.setInteger("item", Item.getIdFromItem(this.dataManager.get(ITEM).getItem()));
        compound.setInteger("meta", this.dataManager.get(ITEM).getMetadata());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(TYPE, compound.getBoolean("type"));
        this.dataManager.set(ITEM,
                new ItemStack(Item.getItemById(compound.getInteger("item")), 1, compound.getInteger("meta")));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() == null) {
            this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 25, 25));
        }
        if (this.getAttackTarget() != null) {
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 1f, 1f);
        }
        if (this.dataManager.get(TYPE) && !this.world.isRemote && this.ticksExisted > 1 &&
                (this.getAttackTarget() != null || this.world.getClosestPlayerToEntity(this, 5) != null)) {
            if (this.dataManager.get(ITEM).getItem() == TakumiItemCore.TAKUMI_CHOCO_BALL) {
                EntityTakumiChocolateBall chocolateBall = new EntityTakumiChocolateBall(this.world, this);
                chocolateBall.setPosition(this.posX + this.rand.nextDouble() - this.rand.nextDouble(),
                        this.posY + this.rand.nextDouble() + this.rand.nextDouble(),
                        this.posZ + this.rand.nextDouble() - this.rand.nextDouble());
                chocolateBall.setHeadingFromThrower(this, this.rotationPitch, this.rotationYawHead, 0.0F, 1.5F, 1.0F);
                this.world.spawnEntity(chocolateBall);
            } else {
                this.entityDropItem(new ItemStack(this.dataManager.get(ITEM).getItem(), 1,
                        this.dataManager.get(ITEM).getMetadata()), 2);
            }
        }
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                if (this.rand.nextBoolean()) {
                    EntityShootingCreeper shootingCreeper = new EntityShootingCreeper(this.world);
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    this.writeEntityToNBT(tagCompound);
                    tagCompound.setBoolean("ignited", false);
                    shootingCreeper.readEntityFromNBT(tagCompound);
                    shootingCreeper.setHealth(this.getHealth());
                    shootingCreeper.copyLocationAndAnglesFrom(this);
                    shootingCreeper.dataManager.set(TYPE, !this.dataManager.get(TYPE));
                    shootingCreeper.dataManager.set(ITEM, this.dataManager.get(ITEM));
                    if (this.getPowered()) {
                        TakumiUtils.takumiSetPowered(shootingCreeper, true);
                    }
                    shootingCreeper.setCreeperState(-1);
                    shootingCreeper.setAttackTarget(null);
                    this.world.spawnEntity(shootingCreeper);
                } else {
                    super.setDead();
                }
            }
        }
        super.setDead();
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.dataManager.get(TYPE) && !(event.getExplosion() instanceof TakumiExplosion)) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    int i = 0;
                    for (ItemStack itemStack : ((EntityPlayer) entity).inventoryContainer.inventoryItemStacks) {
                        if (itemStack.getItem() == this.dataManager.get(ITEM).getItem() &&
                                itemStack.getMetadata() == this.dataManager.get(ITEM).getMetadata()) {
                            i += itemStack.getCount();
                        }
                    }
                    if (i > 0) {
                        TakumiUtils.takumiCreateExplosion(this.world, this, entity.posX, entity.posY, entity.posZ,
                                i / 2, false, true);
                    }
                }
            });
        }
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }
}
