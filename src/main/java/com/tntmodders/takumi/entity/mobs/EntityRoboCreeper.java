package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.client.render.RenderRoboCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.lang.reflect.Field;
import java.util.Random;

public class EntityRoboCreeper extends EntityTakumiAbstractCreeper {
    private BlockPos pos;

    public EntityRoboCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 120);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSize(1.2F, 3.4F);
    }

    public RayTraceResult rayTrace(double blockReachDistance) {
        Vec3d vec3d = this.getPositionEyes(0f);
        Vec3d vec3d1 = this.getLook(0f);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance,
                vec3d1.z * blockReachDistance);
        return this.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(5) == 0 && super.getCanSpawnHere();
    }

    @Override
    public void onUpdate() {
        if (this.world.getNearestAttackablePlayer(this, 32, 32) != null &&
                this.canEntityBeSeen(this.world.getNearestAttackablePlayer(this, 32, 32))) {
            this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 32, 32));
            this.ignite();
        }
        if (this.getAttackTarget() != null) {
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 0.25f, 0.25f);
        }
        if ((this.hasIgnited() || this.getCreeperState() > 0)) {
            try {
                RayTraceResult rayTraceResult = this.rayTrace(64);
                if (rayTraceResult.getBlockPos() != null) {
                    pos = rayTraceResult.getBlockPos();
                }
                if (this.getAttackTarget() != null &&
                        !this.world.getEntitiesWithinAABB(this.getAttackTarget().getClass(),
                                this.getEntityBoundingBox().grow(this.getLookVec().x, this.getLookVec().y,
                                        this.getLookVec().z).grow(32)).isEmpty() &&
                        this.getLookHelper().getIsLooking()) {
                    Entity entity = this.world.getEntitiesWithinAABB(this.getAttackTarget().getClass(),
                            this.getEntityBoundingBox().grow(this.getLookVec().x, this.getLookVec().y,
                                    this.getLookVec().z).grow(32)).get(0);
                    if (pos == null || this.getDistanceSqToEntity(entity) < this.getDistanceSq(pos)) {
                        pos = entity.getPosition();
                    }
                }
                if (pos != null) {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER,
                            SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
                    if (!this.world.isRemote) {
                        this.world.newExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4f, true,
                                true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.world.isRemote) {
            for (int i = 0; i < 5; i++) {
                this.world.createExplosion(this, this.posX + this.rand.nextDouble(), this.posY + this.rand.nextDouble(),
                        this.posZ + this.rand.nextDouble(), 0f, false);
            }
        }
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

        for (int t = 0; t < (this.getPowered() ? 30 : 10); t++) {
            Random rand = new Random();
            int i = this.getPowered() ? 3 : 5;
            EntityLargeFireball fireball = new EntityLargeFireball(this.world);
            if (this.getAttackTarget() != null) {
                double x = this.getAttackTarget().posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 2;
                double z = this.getAttackTarget().posZ + this.rand.nextInt(i * 2) - i;
                fireball.setPosition(x, y, z);
            } else {
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 2;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                fireball.setPosition(x, y, z);
            }
            fireball.motionX = 0;
            fireball.motionY = -5;
            fireball.motionZ = 0;
            fireball.accelerationY = -5;
            fireball.explosionPower = this.getPowered() ? 5 : 3;
            if (!this.world.isRemote) {
                this.world.spawnEntity(fireball);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x8e8e8e;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "robocreeper";
    }

    @Override
    public int getRegisterID() {
        return 261;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderRoboCreeper<>(manager);
    }

    @Override
    public double getSizeAmp() {
        return 2.0;
    }
}

