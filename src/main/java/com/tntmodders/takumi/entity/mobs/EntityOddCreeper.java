package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderOddCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityOddDummyGhast;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityOddCreeper extends EntityTakumiAbstractCreeper {

    public EntityOddCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getPrimaryColor() {
        return 0x88ff88;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderOddCreeper<>(manager);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), 10);
            this.dropItem(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 10);
        }
        super.onDeath(source);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int t = 0; t < (this.getPowered() ? 35 : 20); t++) {
                EntityOddDummyGhast ghast = new EntityOddDummyGhast(this.world);
                ghast.setPosition(this.posX + this.rand.nextInt(10) - 5, this.posY + this.rand.nextInt(10) - 5,
                        this.posZ + this.rand.nextInt(10) - 5);
                this.world.spawnEntity(ghast);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS_MD;
    }

    @Override
    public int getExplosionPower() {
        return 5;
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
        return "oddcreeper";
    }

    @Override
    public int getRegisterID() {
        return 409;
    }
}
