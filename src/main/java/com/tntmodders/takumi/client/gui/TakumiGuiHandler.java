package com.tntmodders.takumi.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class TakumiGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0: {
                return new GuiTakumiBook(player);
            }
            case 1: {
                return new GuiFakeGameOver();
            }
            case 2: {
                return new GuiFakeGameOver_2();
            }
        }
        return null;
    }
}
