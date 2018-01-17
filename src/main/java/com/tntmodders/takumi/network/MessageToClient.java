package com.tntmodders.takumi.network.old;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface MessageToClient {
    
    IMessage handleClientSide(EntityPlayer player);
}
