package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.world.teleport.TakumiTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;

public class BlockTakumiPortalFrame extends Block {
    
    public BlockTakumiPortalFrame() {
        super(Material.TNT, MapColor.GREEN);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiportalframe");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiportalframe");
        this.setLightLevel(1f);
        this.setHardness(5f);
        this.setResistance(10000000f);
        this.setHarvestLevel("pickaxe", 3);
    }
    
    private boolean changeDim(EntityPlayer playerIn) {
        MinecraftServer server = playerIn.world.getMinecraftServer();
        if (server != null) {
            PlayerList playerList = server.getPlayerList();
            int i = playerIn.dimension == DimensionType.OVERWORLD.getId() ? TakumiWorldCore.TAKUMI_WORLD.getId() : DimensionType.OVERWORLD.getId();
            
            Teleporter teleporter = new TakumiTeleporter(server.getWorld(i));
            
            if (playerIn instanceof EntityPlayerMP) {
                playerList.transferPlayerToDimension((EntityPlayerMP) playerIn, i, teleporter);
            } else {
                int origin = playerIn.dimension;
                playerIn.dimension = i;
                playerIn.world.removeEntityDangerously(playerIn);
                playerIn.isDead = false;
                playerList.transferEntityToWorld(playerIn, origin, server.getWorld(origin), server.getWorld(i), teleporter);
            }
        }
        return true;
    }
}
