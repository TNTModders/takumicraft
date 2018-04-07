package com.tntmodders.takumi.entity;

import com.tntmodders.takumi.client.render.RenderTakumiCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.ai.EntityAIFollowCatCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIMoveToAttackBlock;
import com.tntmodders.takumi.entity.item.EntityAttackBlock;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityTakumiAbstractCreeper extends EntityCreeper implements ITakumiEntity {

    private int stoppingCounter;

    public EntityTakumiAbstractCreeper(World worldIn) {
        super(worldIn);
        this.experienceValue = this.takumiRank().getExperiment();
        this.tasks.addTask(3, new EntityAIFollowCatCreeper(this));
        this.tasks.addTask(3, new EntityAIMoveToAttackBlock(this, 1.1, false));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityAttackBlock.class, false) {
            @Override
            protected double getTargetDistance() {
                return 256;
            }
        });
    }

    @Override
    public void additionalSpawn() {
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        return true;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    public boolean isAnimal() {
        return false;
    }

    @Override
    public int getPrimaryColor() {
        return 39168;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTakumiCreeper<>(manager);
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(this.getDropItem(), this.rand.nextInt(3));
            if (this.takumiRank() == EnumTakumiRank.MID && this.rand.nextInt(5) == 0) {
                this.dropItem(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB), 1);
            }
        }
        super.onDeath(source);
    }

    @Override
    protected void despawnEntity() {
        Result result;
        if (this.isNoDespawnRequired()) {
            this.idleTime = 0;
        } else if ((this.idleTime & 0x1F) == 0x1F &&
                (result = ForgeEventFactory.canEntityDespawn(this)) != Result.DEFAULT) {
            if (result == Result.DENY) {
                this.idleTime = 0;
            } else {
                this.setHealth(0);
                this.setDead();
            }
        } else {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);

            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > 16384.0D) {
                    this.setHealth(0);
                    this.setDead();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                    this.setHealth(0);
                    this.setDead();
                } else if (d3 < 1024.0D) {
                    this.idleTime = 0;
                }
            }
        }
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.LIGHTNING_BOLT) {
            return;
        }
        if (damageSrc == DamageSource.IN_WALL && this.takumiRank().getLevel() >= 3) {
            return;
        }
       /* if ((this.takumiRank().getLevel() > 2 && damageSrc.isExplosion()) ||
                (damageSrc.isFireDamage() && this.takumiType() == EnumTakumiType.FIRE) ||
                (damageSrc == DamageSource.DROWN && this.takumiType() == EnumTakumiType.WATER) ||
                (damageSrc == DamageSource.FALL && this.takumiType() == EnumTakumiType.WIND)) {
            return;
        }*/
        super.damageEntity(damageSrc, damageAmount);
    }

    public double getSizeAmp() {
        return 1;
    }

    @Override
    public void onUpdate() {
        if (this.getAttackTarget() instanceof EntityAttackBlock) {
            this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY,
                    this.getAttackTarget().posZ, 1.0);
            double x = this.posX - this.getAttackTarget().posX;
            double z = this.posZ - this.getAttackTarget().posZ;
            if (x * x + z * z <= 9) {
                this.ignite();
            }
        }
        super.onUpdate();
    }
}
