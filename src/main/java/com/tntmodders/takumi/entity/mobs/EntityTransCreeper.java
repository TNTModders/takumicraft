package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.client.render.RenderTransCreeper;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityTransCreeper extends EntityTakumiAbstractCreeper {
    
    private final BossInfoServer bossInfo = (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.transcreeper.name"), Color
            .GREEN, Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private EnumAttackFlg flg;
    
    public EntityTransCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }
    
    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }
    
    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityTransCreeper transCreeper = new EntityTransCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                transCreeper.readEntityFromNBT(tagCompound);
                transCreeper.setHealth(this.getHealth());
                transCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(transCreeper, true);
                }
                transCreeper.flg = this.flg;
                transCreeper.setCreeperState(-1);
                transCreeper.setAttackTarget(null);
                this.world.spawnEntity(transCreeper);
            }
        }
        super.setDead();
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!(event.getExplosion() instanceof TakumiExplosion)) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 600));
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 600));
                }
            });
            event.getAffectedEntities().clear();
        }
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x001100;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTransCreeper <>(manager);
    }
    
    @Override
    public boolean isNonBoss() {
        return false;
    }
    
    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
    }
    
    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }
    
    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
    
    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.IN_WALL) {
            if (!this.world.isRemote) {
                TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 5f, false, true);
            }
        }
        return source != DamageSource.FALL && !source.isExplosion() && super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            if (this.flg == null) {
                this.flg = EnumAttackFlg.EXPLODE;
            }
            switch (this.flg) {
                case EXPLODE: {
                    int i = this.getPowered() ? 20 : 10;
                    Map <BlockPos, IBlockState> stateMap = new HashMap <>();
                    for (int x = -i; x < i; x++) {
                        for (int y = -i; y < i; y++) {
                            for (int z = -i; z < i; z++) {
                                if (x * x + y * y + z * z <= i * i && this.world.getBlockState(this.getPosition().add(x, y, z)).getBlockHardness
                                        (this.world, this.getPosition().add(x, y, z)) >= 0) {
                                    stateMap.put(this.getPosition().add(x, 10 - y, z), this.world.getBlockState(this.getPosition().add(x, y, z)));
                                    this.world.setBlockToAir(this.getPosition().add(x, y, z));
                                }
                            }
                        }
                    }
                    stateMap.forEach((key, value) -> this.world.setBlockState(key, value));
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 10, false);
                    this.flg = EnumAttackFlg.HOMING;
                    break;
                }
                case HOMING: {
                    this.flg = EnumAttackFlg.LASER;
                    break;
                }
                case LASER: {
                    this.flg = EnumAttackFlg.FLOAT;
                    break;
                }
                case FLOAT: {
                    this.flg = EnumAttackFlg.DOWN;
                    break;
                }
                case DOWN: {
                    this.flg = EnumAttackFlg.EXPLODE;
                    break;
                }
            }
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.BOSS;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 0;
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
        return "transcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 502;
    }
    
    private enum EnumAttackFlg {
        EXPLODE, HOMING, LASER, FLOAT, DOWN
    }
}
