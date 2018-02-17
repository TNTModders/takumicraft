package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderEarthCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;

public class EntityEarthCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityEarthCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1.4F, 2.7F);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_IRONGOLEM_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRONGOLEM_DEATH;
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_IRON_GOLEM;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                IBlockState state = this.world.getBlockState(pos);
                if (state.getMaterial() != Material.AIR) {
                    for (int i = 0; i <= this.rand.nextInt(5) + (int) Math.sqrt(
                            this.getPowered() ? 100 : 25 - this.getDistanceSqToCenter(pos)) / 1.5; i++) {
                        if (state.getBlock().canPlaceBlockAt(this.world, pos.up(i))) {
                            this.world.setBlockState(pos.up(i), state);
                        }
                    }
                }
            }
        }
        event.getAffectedBlocks().clear();
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x115555;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderEarthCreeper(manager);
    }
    
    @Override
    public void takumiExplode() {
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
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xaaaaaa;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "earthcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 219;
    }
}
