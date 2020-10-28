package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.ArrayList;
import java.util.List;

public class EntityYukariCreeper extends EntityTakumiAbstractCreeper {

    public EntityYukariCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        List<BlockPos> posList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (pos.getY() > this.posY && (this.world.getBlockState(pos).getBlockHardness(this.world, pos) >= 0 ||
                    this.world.isAirBlock(pos)) || (this.world.getBlockState(pos).getBlock() == Blocks.BEDROCK && pos.getY() > 0)) {
                this.world.setBlockToAir(pos);
            } else if (this.world.isAirBlock(pos) || this.world.getBlockState(pos).getMaterial().isLiquid()) {
                IBlockState state = this.world.getBlockState(this.getPosition().down());
                if (state.getBlock() == TakumiBlockCore.TAKUMI_BOMB_YUKARI || (this.isInvisible() && this.hasIgnited())) {
                    state = TakumiBlockCore.YUKARI_DUMMY.getDefaultState();
                }
                if (/*!(state.getBlock() instanceof BlockTakumiMonsterBomb) &&*/
                        state.getBlockHardness(this.world, this.getPosition().down()) >= 0
                                && !state.getBlock().hasTileEntity(state)) {
                    TakumiUtils.setBlockStateProtected(this.world, pos, state);
                }
            }
        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos,
                                            IBlockState blockStateIn, float p_174816_5_) {
        return super.canExplosionDestroyBlock(explosionIn, worldIn, pos, blockStateIn, p_174816_5_);
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/yukaricreeper_armor.png");
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(TakumiBlockCore.TAKUMI_BOMB_YUKARI), 1);
        }
        super.onDeath(source);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 40 : 25;
            for (int x = -i; x <= i; x++) {
                for (int z = -i; z <= i; z++) {
                    if (x * x + z * z < i * i) {
                        this.world.createExplosion(this, this.posX + x, this.posY, this.posZ + z,
                                (float) Math.sqrt(i - Math.sqrt(x * x + z * z) + 1) * 1.75f, true);
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.YUKARI;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff00ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "yukaricreeper";
    }

    @Override
    public int getRegisterID() {
        return 405;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 && (blockStateIn.getBlock() != Blocks.BEDROCK) ? 10000000f : 1f;
    }
}
