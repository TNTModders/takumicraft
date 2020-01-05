package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityVault extends TileEntityChest {

    public TileEntityVault() {
        super();
    }

    @Override
    public String getName() {
        return TakumiUtils.takumiTranslate("tile.creepervault.name");
    }

    @Override
    public boolean hasCustomName() {
        this.customName = TakumiUtils.takumiTranslate("tile.creepervault.name");
        return true;
    }
}
