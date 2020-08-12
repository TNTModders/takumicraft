package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.Random;

public class EntityRareCreeper extends EntityTakumiAbstractCreeper {

    public EntityRareCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F * 3, 1.7F * 3);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.ICE.getDefaultState());
        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x0000ff;
    }

    @Override
    public double getSizeAmp() {
        return 3d;
    }

    @Override
    public void onUpdate() {
        if (this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) < 49f) {
            if (!(this.getAttackTarget() instanceof EntityPlayer &&
                    ((EntityPlayer) this.getAttackTarget()).isCreative())) {
                this.ignite();
            }
        }
        super.onUpdate();
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 10);
        }
        super.onDeath(source);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int t = 0; t < (this.getPowered() ? 75 : 40); t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 40 : 25;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 2;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 5 : 3, true);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_MD;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "rarecreeper";
    }

    @Override
    public int getRegisterID() {
        return 407;
    }
}
