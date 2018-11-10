package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityAnvilCreeper extends EntityTakumiAbstractCreeper {

    public EntityAnvilCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.GROUND_D;
    }

    @Override
    public int getExplosionPower() {
        return 7;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "anvilcreeper";
    }

    @Override
    public int getRegisterID() {
        return 267;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
/*        event.getAffectedBlocks().forEach(pos -> {
            if (!this.world.isAirBlock(pos)) {
                TakumiUtils.setBlockStateProtected(this.world, pos, TakumiBlockCore.ANVIL_CREEPER.getDefaultState());
            }
        });*/
        this.superJump();
        List<BlockPos> posList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            BlockPos pos1 = new BlockPos(pos.getX(), 0, pos.getZ());
            if (!posList.contains(pos1) && this.rand.nextInt(25) == 0) {
                posList.add(pos1);
            }
        }

        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase && !posList.contains(entity.getPosition())) {
                posList.add(entity.getPosition().down((int) entity.posY));
            }
        }

        event.getAffectedBlocks().clear();
        event.getAffectedEntities().clear();

        for (BlockPos pos : posList) {
            TakumiUtils.setBlockStateProtected(this.world, pos.up((int) this.posY),
                    TakumiBlockCore.ANVIL_CREEPER.getDefaultState());
        }
        return true;
    }

    protected void superJump() {
        this.motionY = 20d;
        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }
        this.move(MoverType.SELF, motionX, motionY, motionZ);
        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    @Override
    public int getPrimaryColor() {
        return 0x777788;
    }
}
