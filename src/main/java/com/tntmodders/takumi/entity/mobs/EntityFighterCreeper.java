package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import javax.annotation.Nullable;

public class EntityFighterCreeper extends EntityZombieCreeper {
    
    public EntityFighterCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
            @Nullable
                    IEntityLivingData livingdata) {
        this.addRandomArmor();
        EntityHorseCreeper horseCreeper = new EntityHorseCreeper(this.world);
        horseCreeper.copyLocationAndAnglesFrom(this);
        this.world.spawnEntity(horseCreeper);
        this.startRiding(horseCreeper, true);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_D;
    }
    
    @Override
    public int getExplosionPower() {
        ItemStack stack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (stack.getItem() instanceof ItemSword) {
            return (int) ((ItemSword) stack.getItem()).getDamageVsEntity() + 3;
        }
        return super.getExplosionPower();
    }
    
    @Override
    public String getRegisterName() {
        return "fightercreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 225;
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityHorseCreeper);
        if (!this.world.isRemote) {
            for (int i = 0; i < (6 + this.rand.nextInt(5)) * (this.getPowered() ? 1.5 : 1); i++) {
                EntityZombieCreeper zombieCreeper = new EntityZombieCreeper(this.world);
                zombieCreeper.copyLocationAndAnglesFrom(this);
                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                    zombieCreeper.setItemStackToSlot(slot, this.getItemStackFromSlot(slot));
                }
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(zombieCreeper, true);
                }
                zombieCreeper.setAttackTarget(this.getAttackTarget());
                this.world.spawnEntity(zombieCreeper);
            }
        }
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x000077;
    }
    
    private void addRandomArmor() {
        int r = this.rand.nextInt(11);
        if (r < 4) {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
            }
        } else if (r < 7) {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
            }
        } else if (r < 9) {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
            }
        } else if (r < 10) {
            if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            }
            if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY) {
                this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }
        } else {
         /*   if (rand.nextInt(3) == 0) {
                if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY)
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TakumiCraftCore.CreeperSW));
                if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY)
                    this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TakumiCraftCore.CreeperAH));
                if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY)
                    this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(TakumiCraftCore.CreeperAC));
                if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY)
                    this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TakumiCraftCore.CreeperAL));
                if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY)
                    this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TakumiCraftCore.CreeperAB));
            } else*/
            {
                if (this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == ItemStack.EMPTY) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                }
                if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == ItemStack.EMPTY) {
                    this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                }
                if (this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == ItemStack.EMPTY) {
                    this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                }
                if (this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) == ItemStack.EMPTY) {
                    this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                }
                if (this.getItemStackFromSlot(EntityEquipmentSlot.FEET) == ItemStack.EMPTY) {
                    this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                }
            }
        }
    }
}
