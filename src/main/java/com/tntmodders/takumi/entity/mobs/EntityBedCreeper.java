package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;

public class EntityBedCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityBedCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.BED;
    }
    
    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.25f;
    }
    
    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world, pos, null) ? 10 :
               super.getBlockPathWeight(pos);
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            if (event.getExplosion() instanceof TakumiExplosion) {
                event.getAffectedEntities().clear();
                event.getAffectedBlocks().forEach(pos -> {
                    if (EntityBedCreeper.this.world.getTileEntity(pos) instanceof IInventory) {
                        ((IInventory) EntityBedCreeper.this.world.getTileEntity(pos)).clear();
                        EntityBedCreeper.this.world.setBlockToAir(pos);
                    } else if (EntityBedCreeper.this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world, pos, null)) {
                        EntityBedCreeper.this.world.setBlockToAir(pos);
                    }
                });
            } else {
                event.getAffectedEntities().forEach(entity -> {
                    if (entity instanceof EntityPlayer) {
                        BlockPos pos = ((EntityPlayer) entity).getBedLocation(this.world.provider.getDimension());
                        if (this.world.getBlockState(pos).getBlock().isBed(this.world.getBlockState(pos), this.world, pos, entity)) {
                            TakumiUtils.takumiCreateExplosion(this.world, this, pos.getX(), pos.getY(), pos.getZ(),
                                    this.getPowered() ? 20 : 12, false, true);
                        }
                    }
                });
            }
        }
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x33ff33;
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 8 : 5, false);
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_D;
    }
    
    @Override
    public int getExplosionPower() {
        return 0;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xff8888;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "bedcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 238;
    }
}
