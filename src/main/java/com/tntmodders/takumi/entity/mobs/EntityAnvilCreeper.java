package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.MoverType;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityAnvilCreeper extends EntityTakumiAbstractCreeper {

    public EntityAnvilCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    protected void superJump() {
        this.motionY = 10d;
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

        int i = this.getPowered() ? 5 : 3;
        for (int x = -i; x <= i; x++) {
            for (int z = -i; z <= i; z++) {
                this.world.setBlockState(this.getPosition().add(x, 0, z),
                        TakumiBlockCore.ANVIL_CREEPER.getDefaultState());
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            if (!this.world.isAirBlock(pos)) {
                this.world.setBlockState(pos, TakumiBlockCore.ANVIL_CREEPER.getDefaultState());
            }
        });
        event.getAffectedBlocks().clear();
        this.superJump();
        return true;
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
        return 3;
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
    public int getPrimaryColor() {
        return 0x777788;
    }
}
