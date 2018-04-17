package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.network.MessageYMSMove;
import com.tntmodders.takumi.network.MessageYMSMoveHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TakumiPacketCore {

    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(TakumiCraftCore.MODID);


    public static void register() {
        INSTANCE.registerMessage(MessageYMSMoveHandler.class, MessageYMSMove.class, 0, Side.SERVER);
    }
}
