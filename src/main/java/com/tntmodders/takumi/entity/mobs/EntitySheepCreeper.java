package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Maps;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderSheepCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntitySheepCreeper extends EntityTakumiAbstractCreeper implements IShearable {
    
    private static final DataParameter <Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheepCreeper.class, DataSerializers.BYTE);
    private static final DataParameter <Boolean> RAINBOW = EntityDataManager.createKey(EntitySheepCreeper.class, DataSerializers.BOOLEAN);
    private static final Map <EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    
    static {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            DYE_TO_RGB.put(enumdyecolor, createSheepColor(enumdyecolor));
        }

        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{0.9019608F, 0.9019608F, 0.9019608F});
    }
    
    public EntitySheepCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 1.3F);
    }
    
    private static float[] createSheepColor(EnumDyeColor dyeColor) {
        float[] afloat = dyeColor.getColorComponentValues();
        float f = 0.75F;
        return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
    }
    
    @SideOnly(Side.CLIENT)
    public static float[] getDyeRgb(EnumDyeColor dyeColor) {
        return DYE_TO_RGB.get(dyeColor);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, (byte) 0);
        this.dataManager.register(RAINBOW, false);
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", this.getSheared());
        compound.setByte("Color", (byte) this.getFleeceColor().getMetadata());
        compound.setBoolean("Rainbow", this.getRainbow());
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
        this.setRainbow(compound.getBoolean("Rainbow"));
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        switch (this.getFleeceColor()) {
            case WHITE:
            default:
                return LootTableList.ENTITIES_SHEEP_WHITE; case ORANGE:
                return LootTableList.ENTITIES_SHEEP_ORANGE; case MAGENTA:
                return LootTableList.ENTITIES_SHEEP_MAGENTA; case LIGHT_BLUE:
                return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE; case YELLOW:
                return LootTableList.ENTITIES_SHEEP_YELLOW; case LIME:
                return LootTableList.ENTITIES_SHEEP_LIME; case PINK:
                return LootTableList.ENTITIES_SHEEP_PINK; case GRAY:
                return LootTableList.ENTITIES_SHEEP_GRAY; case SILVER:
                return LootTableList.ENTITIES_SHEEP_SILVER; case CYAN:
                return LootTableList.ENTITIES_SHEEP_CYAN; case PURPLE:
                return LootTableList.ENTITIES_SHEEP_PURPLE; case BLUE:
                return LootTableList.ENTITIES_SHEEP_BLUE; case BROWN:
                return LootTableList.ENTITIES_SHEEP_BROWN; case GREEN:
                return LootTableList.ENTITIES_SHEEP_GREEN; case RED:
                return LootTableList.ENTITIES_SHEEP_RED; case BLACK:
                return LootTableList.ENTITIES_SHEEP_BLACK;
        }
    }
    
    public boolean getSheared() {
        return (this.dataManager.get(DYE_COLOR) & 16) != 0;
    }
    
    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
    }
    
    /**
     * Sets the wool color of this sheep
     */
    public void setFleeceColor(EnumDyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getMetadata() & 15));
    }
    
    public boolean getRainbow() {
        return this.dataManager.get(RAINBOW);
    }
    
    public void setRainbow(boolean rainbow) {
        this.dataManager.set(RAINBOW, rainbow);
    }
    
    @Override
    public List <ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        this.setSheared(true);
        int i = 1 + this.rand.nextInt(3);
    
        List <ItemStack> ret = new ArrayList <>();
        for (int j = 0; j < i; ++j) {
            ret.add(new ItemStack(Item.getItemFromBlock(TakumiBlockCore.CREEPER_WOOL), 1, this.getFleeceColor().getMetadata()));
        }
    
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }
    
    @Override
    public int getExplosionPower() {
        return this.getRainbow() ? 10 : 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x002200;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "sheepcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 43;
    }
    
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return !this.getSheared() && !this.isChild();
    }
    
    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared) {
        byte b0 = this.dataManager.get(DYE_COLOR);
    
        if (sheared) {
            this.dataManager.set(DYE_COLOR, (byte) (b0 | 16));
        } else {
            this.dataManager.set(DYE_COLOR, (byte) (b0 & -17));
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }
    
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
            @Nullable
                    IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(EnumDyeColor.byDyeDamage(this.world.rand.nextInt(16)));
        if (this.rand.nextInt(100) == 0) {
            this.setRainbow(true);
            TakumiUtils.takumiSetPowered(this, true);
        }
        return livingdata;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return this.getRainbow() ? 15728880 : super.getBrightnessForRender();
    }
    
    @Override
    public float getBrightness() {
        return this.getRainbow() ? 1.0F : super.getBrightness();
    }
    
    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY) {
        if (stack.getItem() == Items.MUTTON) {
            stack = new ItemStack(Items.COOKED_MUTTON, stack.getCount());
        } else if (stack.getItem() == Item.getItemFromBlock(Blocks.WOOL)) {
            stack = new ItemStack(TakumiBlockCore.CREEPER_WOOL, stack.getCount(), stack.getMetadata());
        }
        return super.entityDropItem(stack, offsetY);
    }
    
    @Override
    public float getEyeHeight() {
        return 0.95F * this.height;
    }
    
    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }
    
    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 25, 5, 20, TakumiEntityCore.CREATURE_TAKUMI, TakumiEntityCore
                .biomes.toArray(new Biome[0]));
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xeeffee;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSheepCreeper(manager);
    }
    
    @Override
    public ResourceLocation getArmor() {
        return this.getRainbow() ? new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png") : super.getArmor();
    }
}
