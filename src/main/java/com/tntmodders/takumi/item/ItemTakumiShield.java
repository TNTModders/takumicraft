package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.model.ModelShield;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemTakumiShield extends ItemShield {
    
    public static final ResourceLocation SHIELD_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/shield_base.png");
    @SideOnly(Side.CLIENT)
    public final ModelShield modelShield = new ModelShield();
    
    public ItemTakumiShield() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumishield"); this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumishield");
    }
    
    @Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
        return true;
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TakumiUtils.takumiTranslate(this.getUnlocalizedName() + ".name");
    }
}
