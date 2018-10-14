package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityShulkerCreeper extends EntityTakumiAbstractCreeper {

    public EntityShulkerCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.superJump();
    }

    protected void superJump() {
        this.motionY = 75d;
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

        for (int t = 0; t < 20 * (this.getPowered() ? 2 : 1); t++) {
            Random rand = new Random();
            double x = this.posX + this.rand.nextInt(20) - 10;
            double y = this.posY + this.rand.nextInt(20) - 10;
            double z = this.posZ + this.rand.nextInt(20) - 10;
            EntityShulkerBullet shulkerBullet =
                    new EntityShulkerBullet(this.world, this, this.getAttackTarget(), Axis.Y);
            shulkerBullet.setPosition(x, y, z);
            if (!this.world.isRemote) {
                this.world.spawnEntity(shulkerBullet);

            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
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
        return true;
    }

    @Override
    public String getRegisterName() {
        return "shulkercreeper";
    }

    @Override
    public int getRegisterID() {
        return 247;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(EntityShulkerCreeper.class, this.takumiRank().getSpawnWeight(), 1, 1,
                EnumCreatureType.MONSTER, TakumiEntityCore.biomes.toArray(new Biome[0]));
        EntityRegistry.addSpawn(EntityShulkerCreeper.class, this.takumiRank().getSpawnWeight(), 5, 5,
                EnumCreatureType.MONSTER, Biomes.SKY);
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.SHULKER_SHELL;
    }

    @Override
    public int getPrimaryColor() {
        return 0x885588;
    }
}
