package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityEdgyCreeper extends EntityTakumiAbstractCreeper {

    public EntityEdgyCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    for (int i = 0; i < ((EntityPlayer) entity).inventory.mainInventory.size(); i++) {
                        if (((EntityPlayer) entity).inventory.mainInventory.get(i) != ItemStack.EMPTY) {
                            ((EntityPlayer) entity).inventory.mainInventory.get(i).setStackDisplayName(this.getRandomName(((EntityPlayer) entity).inventory.mainInventory.get(i)));
                        }
                    }
                    for (int i = 0; i < ((EntityPlayer) entity).inventory.offHandInventory.size(); i++) {
                        if (((EntityPlayer) entity).inventory.offHandInventory.get(i) != ItemStack.EMPTY) {
                            ((EntityPlayer) entity).inventory.offHandInventory.get(i).setStackDisplayName(this.getRandomName(((EntityPlayer) entity).inventory.offHandInventory.get(i)));
                        }
                    }
                    for (int i = 0; i < ((EntityPlayer) entity).inventory.armorInventory.size(); i++) {
                        if (((EntityPlayer) entity).inventory.armorInventory.get(i) != ItemStack.EMPTY) {
                            ((EntityPlayer) entity).inventory.armorInventory.get(i).setStackDisplayName(this.getRandomName(((EntityPlayer) entity).inventory.armorInventory.get(i)));
                        }
                    }
                } else {
                   if(!entity.hasCustomName()){
                       entity.setCustomNameTag(TakumiUtils.takumiTranslate("entity.edgycreeper.tip_a." + this.rand.nextInt(12)) + entity.getDisplayName().getFormattedText());
                   }
                }
            });
            event.getAffectedEntities().clear();
        }
        return true;
    }

    private String getRandomName(ItemStack itemStack) {
        if (itemStack.hasDisplayName()) {
            return itemStack.getDisplayName();
        }
        if (this.rand.nextBoolean()) {
            return TakumiUtils.takumiTranslate("entity.edgycreeper.tip_a." + this.rand.nextInt(12))
                    + TakumiUtils.takumiTranslate("entity.edgycreeper.tip_b." + this.rand.nextInt(12));
        } else {
            return TakumiUtils.takumiTranslate("entity.edgycreeper.tip_a." + this.rand.nextInt(12)) + itemStack.getDisplayName();
        }
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
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0099;
    }

    @Override
    public int getPrimaryColor() {
        return 0x993399;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "edgycreeper";
    }

    @Override
    public int getRegisterID() {
        return 303;
    }
}
