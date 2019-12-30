package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.mobs.EntityCreativeCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityCreativeCreeper_Evo extends EntityCreativeCreeper {
    public EntityCreativeCreeper_Evo(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i < (this.getPowered() ? 5 : 3); i++) {
                BlockPos pos = this.getPosition().add(this.rand.nextInt(51) - 25, this.rand.nextInt(21) - 10, this.rand.nextInt(51) - 25);
                this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 4 * (this.getPowered() ? 2 : 1), false);
                this.buildHouse(pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return super.getRegisterName() + "_evo";
    }

    @Override
    public int getRegisterID() {
        return 293;
    }

    @Override
    public boolean isEvo() {
        return true;
    }
}
