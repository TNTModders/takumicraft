package com.tntmodders.takumi.item;

import com.tntmodders.takumi.block.BlockTakumiMonsterBomb;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemTakumiMonsterBomb extends ItemBlock {

    private final BlockTakumiMonsterBomb monsterBomb;

    public ItemTakumiMonsterBomb(BlockTakumiMonsterBomb block) {
        super(block);
        this.monsterBomb = block;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TakumiUtils.takumiTranslate("entity." + this.monsterBomb.getName() + ".name") +
                TakumiUtils.takumiTranslate(monsterBomb.getUnlocalizedName() + ".name");
    }
}
