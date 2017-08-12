package com.tntmodders.takumi.utils;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class TakumiUtils {
    public static String takumiTranslate(String s) {
        return I18n.translateToLocal(s);
    }

    public static void takumiUnlockRecipes(ItemStack stack, EntityPlayer player) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            //レシピはjsonをスキャンして結果からリストでif判定できるように!
            Item item = stack.getItem();
            int meta = stack.getMetadata();
            ItemStack itemStack = new ItemStack(item, 1, meta);
            if (TakumiCraftCore.HOLDER.map.containsKey(itemStack)) {
                List<ResourceLocation> list = TakumiCraftCore.HOLDER.map.get(itemStack);
                player.unlockRecipes(list.toArray(new ResourceLocation[list.size()]));
            }
        }
    }

    public static float takumiGetHardness(Block block) {
        try {
            Field field = Block.class.getDeclaredField("blockHardness");
            field.setAccessible(true);
            float f = ((float) field.get(block));
            return f > -1 ? f : -1;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public static void takumiSetPowered(Entity entity) {
        try {
            Field field = EntityCreeper.class.getDeclaredField("POWERED");
            field.setAccessible(true);
            DataParameter<Boolean> parameter = ((DataParameter<Boolean>) field.get(entity));
            entity.getDataManager().set(parameter, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean getAdvancementUnlocked(ResourceLocation location) {
        ClientAdvancementManager manager = Minecraft.getMinecraft().player.connection.getAdvancementManager();
        try {
            Field field = manager.getClass().getDeclaredField("advancementToProgress");
            field.setAccessible(true);
            Map<Advancement, AdvancementProgress> advancementToProgress = ((Map) field.get(manager));
            if (advancementToProgress.containsKey(manager.getAdvancementList().getAdvancement(location))) {
                return advancementToProgress.get(manager.getAdvancementList().getAdvancement(location)).isDone();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
