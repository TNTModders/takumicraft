package com.tntmodders.takumi.entity.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

public class EntityTakumiExpOrb extends EntityXPOrb {

    public EntityTakumiExpOrb(World worldIn, double x, double y, double z, int expValue) {
        super(worldIn, x, y, z, expValue);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        this.setDead();
        return true;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return true;
    }

    public EntityTakumiExpOrb(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote) {
            if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
                if (MinecraftForge.EVENT_BUS.post(new PlayerPickupXpEvent(entityIn, this))) {
                    return;
                }
                entityIn.xpCooldown = 2;
                entityIn.onItemPickup(this, 1);
                ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, entityIn);

                if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                    int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getItemDamage());
                    this.xpValue -= this.durabilityToXp(i);
                    itemstack.setItemDamage(itemstack.getItemDamage() - i);
                }

                if (this.xpValue > 0) {
                    entityIn.addExperience(this.xpValue);
                }
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1f, true);
                this.setDead();
            }
        }
    }

    private int xpToDurability(int xp) {
        return xp * 2;
    }

    private int durabilityToXp(int durability) {
        return durability / 2;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
}
