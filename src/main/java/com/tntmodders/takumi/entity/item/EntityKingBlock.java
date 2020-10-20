package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.world.World;

public class EntityKingBlock extends EntityIceologerCreeperSpell {
    public EntityKingBlock(World worldIn) {
        super(worldIn);
    }

    public EntityKingBlock(World worldIn, double x, double y, double z, double tickIn) {
        super(worldIn, x, y, z, tickIn);
    }

    @Override
    protected void onGroundUpdate() {
        for (int i = 0; i < 10; i++) {
            double x = this.posX - 2.5 + this.rand.nextDouble() * 6;
            double y = this.posY + this.rand.nextDouble() - this.rand.nextDouble();
            double z = this.posZ - 2.5 + this.rand.nextDouble() * 6;
            TakumiUtils.takumiCreateExplosion(world, this, x, y, z, 5f, true, true, 4f);
        }
    }

    @Override
    public boolean canFreezeEntity() {
        return false;
    }
}
