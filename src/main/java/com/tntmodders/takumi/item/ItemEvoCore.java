package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.ITakumiEvoEntity;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ItemEvoCore extends Item {
    boolean flg;

    public ItemEvoCore(boolean flg) {
        super();
        String name = flg ? "evocore_evo" : "evocore";
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(name);
    }

    public ItemEvoCore() {
        this(false);
    }


    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.EVO_CORE_EVO ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!playerIn.world.isRemote && target instanceof ITakumiEvoEntity && !((ITakumiEvoEntity) target).isEvo()) {
            try {
                Entity entity = ((Entity) ((ITakumiEvoEntity) target).getEvoCreeper().getClass().getConstructor(World.class).newInstance(playerIn.world));
                entity.copyLocationAndAnglesFrom(target);
                target.setDead();
                if (stack.getItem() == TakumiItemCore.EVO_CORE_EVO) {
                    TakumiUtils.takumiSetPowered(((EntityCreeper) entity), true);
                }
                playerIn.world.spawnEntity(entity);
                if (!playerIn.isCreative()) {
                    stack.shrink(1);
                }
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem instanceof EntityItem) {
            entityItem.setEntityInvulnerable(entityItem.ticksExisted < 1200);
        }
        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.EVO_CORE_EVO;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ActionResult result = new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        if (playerIn.getHeldItem(handIn).getItem() == TakumiItemCore.EVO_CORE_EVO) {
            List<EntityCreeper> list = worldIn.getEntitiesWithinAABB(EntityCreeper.class, playerIn.getEntityBoundingBox().grow(6),
                    entity -> entity instanceof ITakumiEvoEntity && !((ITakumiEvoEntity) entity).isEvo());
            if (!list.isEmpty()) {
                list.sort(Comparator.comparingDouble(playerIn::getDistanceSqToEntity));
                EntityCreeper target = list.get(0);
                if (target != null && (target.posX - playerIn.posX) * (target.posX - playerIn.posX) + (target.posZ - playerIn.posZ) * (target.posZ - playerIn.posZ) < 25
                        && (target.posY - playerIn.posY) * (target.posY - playerIn.posY) < 16) {
                    if (!worldIn.isRemote) {
                        try {
                            Entity entity = ((Entity) ((ITakumiEvoEntity) target).getEvoCreeper().getClass().getConstructor(World.class).newInstance(playerIn.world));
                            entity.copyLocationAndAnglesFrom(target);
                            target.setDead();
                            playerIn.world.spawnEntity(entity);
                            if (!playerIn.isCreative()) {
                                playerIn.getHeldItem(handIn).shrink(1);
                            }

                        } catch (Exception ignored) {
                        }
                    } else {
                        for (int x = -5; x <= 5; x++) {
                            for (int z = -5; z <= 5; z++) {
                                if ((x ^ 2 + z ^ 2) < 25 && (x ^ 2 + z ^ 2) > 20) {
                                    for (int i = 0; i < 10; i++) {
                                        worldIn.spawnParticle(EnumParticleTypes.PORTAL, playerIn.posX + x + worldIn.rand.nextDouble() - 0.5, playerIn.posY + 0.5,
                                                playerIn.posZ + z + worldIn.rand.nextDouble() - 0.5, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    }
                    return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
                }
            }
        } else {
/*            EntityItem item = new EntityItem(worldIn);
            item.setItem(TakumiUtils.generateRandomTipsBook(worldIn.rand));
            item.copyLocationAndAnglesFrom(playerIn);
            worldIn.spawnEntity(item);*/
        }
        return result;
    }
}
