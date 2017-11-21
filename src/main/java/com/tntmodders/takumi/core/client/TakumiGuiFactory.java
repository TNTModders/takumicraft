package com.tntmodders.takumi.core.client;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Set;

public class TakumiGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new TakumiConfigGui(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    private static class TakumiConfigGui extends GuiConfig {
        public TakumiConfigGui(GuiScreen parent) {
            super(parent, new ConfigElement(TakumiConfigCore.cfg.getCategory(TakumiConfigCore.GENERAL)).getChildElements(), TakumiCraftCore.MODID, false, false, TakumiCraftCore.MODID);
        }
    }
}
