package com.tntmodders.takumi.network;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketManager {
    private static SimpleNetworkWrapper networkHandler = null;
    private int id = 0;

    private PacketManager() {
    }

    public static PacketManager init(String modId) {
        networkHandler = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
        return new PacketManager();
    }

    public static SimpleNetworkWrapper getNetworkHandler() {
        return networkHandler;
    }

    @SuppressWarnings("unchecked")
    public PacketManager registerPacket(Class<? extends AbstractPacket> packetClass) {
        Class<AbstractPacket> message = (Class<AbstractPacket>) packetClass;
        if (MessageToServer.class.isAssignableFrom(packetClass)) {
            networkHandler.registerMessage(packetClass, message, id, Side.SERVER);
            TakumiCraftCore.LOGGER.debug("Registered Packet:" + packetClass.getName() + " at " + id + " to SERVER");
            id++;
        }

        if (MessageToClient.class.isAssignableFrom(packetClass)) {
            networkHandler.registerMessage(packetClass, message, id, Side.CLIENT);
            TakumiCraftCore.LOGGER.debug("Registered Packet:" + packetClass.getName() + " at " + id + " to CLIENT");
            id++;
        }
        return this;
    }

}
