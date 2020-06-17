package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.entity.mobs.EntityBoltCreeper;
import com.tntmodders.takumi.item.ItemTakumiArrow;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityTakumiArrow extends EntityArrow {

    public int power;
    private ItemStack stack;
    private boolean destroy;
    private Class<? extends EntityCreeper> container;
    private EnumArrowType type;

    public EntityTakumiArrow(World worldIn) {
        super(worldIn);
        this.type = EnumArrowType.NORMAL;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base,
                             Class<? extends EntityCreeper> creeper) {
        this(worldIn, itemStack, base);
        this.type = EnumArrowType.MONSTER;
        this.container = creeper;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base) {
        super(worldIn, base);
        this.stack = itemStack;
        this.power = ((ItemTakumiArrow) stack.getItem()).power;
        this.destroy = ((ItemTakumiArrow) stack.getItem()).destroy;
        this.container = null;
        this.type = EnumArrowType.NORMAL;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base, EnumArrowType type) {
        this(worldIn, itemStack, base);
        this.type = type;
    }

    public EntityTakumiArrow(World worldIn, EntityLivingBase base, int pierce, int power, boolean destroy,
                             Class<EntityCreeper> container, EnumArrowType type) {
        super(worldIn, base);
        this.stack = null;
        this.power = power;
        this.destroy = destroy;
        this.container = null;
        this.type = type;
    }

    @Override
    public void onUpdate() {
        if (this.type == EnumArrowType.LASER) {
            this.setNoGravity(true);
            if (this.world.isRemote) {
                for (int i = 0; i <= 1000; i++) {
                    this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.END_ROD.getParticleID(),
                            this.posX + this.rand.nextDouble() * 6 - 3, this.posY + this.rand.nextDouble() * 6 - 3,
                            this.posZ + this.rand.nextDouble() * 6 - 3, this.motionX / 3, this.motionY / 3,
                            this.motionZ / 3);
                }
            } else {
                TakumiUtils.takumiCreateExplosion(world, this.shootingEntity != null ? this.shootingEntity : this,
                        this.posX, this.posY, this.posZ, power / 2, false, destroy);
            }
            if (this.ticksExisted > 200 || this.isInWater() || this.isInLava() || this.isEntityInsideOpaqueBlock() ||
                    (this.posX == this.prevPosX && this.posY == this.prevPosY && this.posZ == this.prevPosZ)) {
                this.setDead();
            }
        }
        super.onUpdate();
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == Type.ENTITY) {
            if (raytraceResultIn.entityHit == this.shootingEntity) {
                return;
            }
            if (!TakumiConfigCore.inEventServer) {
                raytraceResultIn.entityHit.attackEntityFrom(
                        new EntityDamageSourceIndirect("explosion.player", this.shootingEntity, this), 15.0f);
            }
        }
        if (this.type == null) {
            this.type = EnumArrowType.NORMAL;
        }
        if (!this.world.isRemote) {
            switch (this.type) {
                case NORMAL: {
                    if (TakumiConfigCore.inEventServer) {
                        TakumiUtils.takumiCreateExplosion(world, this,
                                this.posX, this.posY, this.posZ, 15, false, false, 4);
                    } else {
                        TakumiUtils.takumiCreateExplosion(world, this.shootingEntity != null ? this.shootingEntity : this,
                                this.posX, this.posY, this.posZ, power, false, destroy);
                    }
                    break;
                }
                case SHOT: {
                    for (int i = 0; i < 5; i++) {
                        TakumiUtils.takumiCreateExplosion(world,
                                this.shootingEntity != null ? this.shootingEntity : this,
                                this.posX + rand.nextInt(7) - 3, this.posY + rand.nextInt(3),
                                this.posZ + rand.nextInt(7) - 3, power, false, destroy);
                    }
/*                    if (this.shootingEntity instanceof EntityCannonCreeper && this.shootingEntity.isGlowing() && ((EntityCannonCreeper) this.shootingEntity).getAttackTarget() instanceof EntityAttackBlock) {
                        TakumiUtils.takumiCreateExplosion(world, this.shootingEntity != null ? this.shootingEntity : this,
                                this.posX, this.posY, this.posZ, power * 2, false, true);
                        try {
                            EntityLiving entity = (EntityCreeper) EntityAttackBlock.ARTILLERIES.get(this.rand.nextInt(EntityAttackBlock.ARTILLERIES.size()))
                                    .getConstructor(World.class).newInstance(this.world);
                            entity.setPosition(this.posX, this.posY, this.posZ);
                            this.world.spawnEntity(entity);
                        } catch (Exception ignored) {
                        }
                    }*/
                    break;
                }
                case PIERCE: {
                    for (int i = 0; i < 5; i++) {
                        TakumiUtils.takumiCreateExplosion(world,
                                this.shootingEntity != null ? this.shootingEntity : this,
                                this.posX + this.motionX / 4 * i, this.posY + this.motionY / 4 * i,
                                this.posZ + this.motionZ / 4 * i, power, false, destroy);

                    }
                    break;
                }
                case MONSTER: {
                    try {
                        EntityCreeper creeper = this.container.getConstructor(World.class).newInstance(world);
                        creeper.setPosition(this.posX, this.posY, this.posZ);
                        NBTTagCompound compound = new NBTTagCompound();
                        creeper.writeEntityToNBT(compound);
                        compound.setShort("Fuse", (short) 1);
                        creeper.readEntityFromNBT(compound);
                        if (creeper instanceof EntityBoltCreeper || world.isThundering()) {
                            creeper.onStruckByLightning(null);
                        }
                        creeper.setInvisible(true);
                        creeper.ignite();
                        world.spawnEntity(creeper);
                        creeper.onUpdate();
                        if (creeper instanceof EntityBoltCreeper) {
                            creeper.setDead();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case LASER: {
                    for (int i = 0; i < 10; i++) {
                        TakumiUtils.takumiCreateExplosion(world,
                                this.shootingEntity != null ? this.shootingEntity : this, this.posX + this.motionX * i,
                                this.posY + this.motionY * i, this.posZ + this.motionZ * i, power, false, destroy);
                    }
                    break;
                }
            }
        }
        this.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("power", this.power);
        compound.setBoolean("destroy", this.destroy);
        if (this.container != null) {
            compound.setString("container", this.container.getName());
        }
        compound.setString("type", this.type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.power = compound.getInteger("power");
        this.destroy = compound.getBoolean("destroy");
        try {
            this.container = (Class<? extends EntityCreeper>) Class.forName(compound.getString("container"));
        } catch (Exception ignored) {
        }
        try {
            this.type = EnumArrowType.valueOf(compound.getString("type"));
        } catch (Exception e) {
            this.type = EnumArrowType.NORMAL;
            e.printStackTrace();
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return stack;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        if (TakumiConfigCore.inEventServer && this.type == EnumArrowType.NORMAL) {
            return 0;
        }
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f :
                super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) / 3;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public enum EnumArrowType {
        NORMAL, MONSTER, SHOT, PIERCE, LASER
    }
}
