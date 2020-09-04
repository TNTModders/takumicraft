package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.tileentity.TileEntityTakumiCreepered;
import com.tntmodders.takumi.tileentity.TileEntityTakumiSuperPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityCraftsmanCreeper extends EntityTakumiAbstractCreeper {

    public EntityCraftsmanCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 10 : 6, true);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x660066;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "craftsmancreeper";
    }

    @Override
    public int getRegisterID() {
        return 244;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            if (!this.world.isAirBlock(pos)) {
                IBlockState state = event.getWorld().getBlockState(pos);
                event.getWorld().setBlockState(pos, TakumiBlockCore.TAKUMI_CREEPERED.getDefaultState());
                if (event.getWorld().getTileEntity(pos) instanceof TileEntityTakumiCreepered) {
                    ((TileEntityTakumiSuperPowered) event.getWorld().getTileEntity(pos)).setPath(
                            state.getBlock().getRegistryName().toString());
                    ((TileEntityTakumiSuperPowered) event.getWorld().getTileEntity(pos)).setMeta(
                            state.getBlock().getMetaFromState(state));
                }
            }
        });
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase && !(entity instanceof EntityCreeper)) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.CREEPERED, 30));
                }
            });
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x111111;
    }
}
