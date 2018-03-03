package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityAnnivCreeper extends EntityTakumiAbstractCreeper {

    public EntityAnnivCreeper(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.CAKE);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int t = 0; t < (this.getPowered() ? 60 : 30); t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 50 : 30;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 2;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 5 : 3, true);
            }
            for (int t = 0; t < (this.getPowered() ? 300 : 200); t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 50 : 30;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                double y = this.world.getHeight((int) x, (int) z);
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 10 : 7, true);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0000;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "annivcreeper";
    }

    @Override
    public int getRegisterID() {
        return 408;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 2.5f;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (!this.world.isAirBlock(pos)) {
                event.getWorld().setBlockToAir(pos);
                event.getWorld().setBlockState(pos.down(), Blocks.CAKE.getDefaultState());
            }
        }
        event.getAffectedBlocks().clear();
        event.getAffectedEntities().forEach(entity -> heal(20));
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xffffff;
    }
}
