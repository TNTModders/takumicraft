package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.EntityTakumiLightningBolt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.lang.reflect.Field;
import java.util.Random;

public class EntitySuperDiamondCreeper extends EntityTakumiAbstractCreeper {
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.superdiamondcreeper.name"), BossInfo.Color.BLUE,
                    BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);

    public EntitySuperDiamondCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.experienceValue = 50;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
    }


    @Override
    public void takumiExplode() {
        this.setDead();
        if (!this.world.isRemote) {
            int limit = (30 + this.rand.nextInt(20)) * (this.getPowered() ? 2 : 1);
            for (int t = 0; t < limit; t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 100 : 70;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 1.5;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 5f : 4f, true);
            }
            for (int t = 0; t < (this.getPowered() ? 350 : 200); t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 100 : 70;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                double y = this.world.getHeight((int) x, (int) z);
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 12f : 9f, true);
            }

            for (int t = 0; t < 20; t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 10 : 5;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = 256;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                EntityItem item = new EntityItem(this.world, x, y, z);
                item.setItem(new ItemStack(TakumiItemCore.TAKUMI_DIAMOND, 1));
                this.world.spawnEntity(item);
            }
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.world.isRemote) {
            EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ);
            ItemStack stack = new ItemStack(TakumiItemCore.TAKUMI_DIAMOND, 1);
            stack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 1);
            item.setItem(stack);
            this.world.spawnEntity(item);
            this.dropItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 64);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getPrimaryColor() {
        return 0x9090a0;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "superdiamondcreeper";
    }

    @Override
    public int getRegisterID() {
        return 414;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 1f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.isEntityAlive() && (this.getCreeperState() > 0 || this.hasIgnited()) && this.rand.nextInt(20) == 0) {
            Random random = new Random(System.currentTimeMillis());
            double x = this.posX + random.nextInt(100) - 50;
            double z = this.posZ + random.nextInt(100) - 50;
            double y = this.world.getHeight(((int) x), ((int) z));
            EntityTakumiLightningBolt bolt = new EntityTakumiLightningBolt(this.world, x, y, z, false);
            this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
            if (!this.world.isRemote) {
                EntityDiamondCreeper diamondCreeper = new EntityDiamondCreeper(this.world);
                diamondCreeper.setPosition(x, y + 5, z);
                this.world.spawnEntity(diamondCreeper);
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (!this.world.isAirBlock(pos) && this.world.getBlockState(pos).getBlockHardness(this.world, pos) >= 0 &&
                    this.world.getBlockState(pos.down()).getBlockHardness(this.world, pos) >= 0) {
                event.getWorld().setBlockToAir(pos);
                event.getWorld().setBlockState(pos.down(), TakumiBlockCore.TAKUMI_DIAMOND_BLOCK.getDefaultState());
            }
        }
        event.getAffectedBlocks().clear();
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/dragon_armor.png");
    }

    @Override
    public int getSecondaryColor() {
        return 0x0000ff;
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

}
