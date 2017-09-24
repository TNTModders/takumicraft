package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelZombieVillagerCreeper;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.client.render.layer.LayerVillagerCreeperArmor;
import com.tntmodders.takumi.entity.mobs.EntityZombieVillagerCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieVillagerCreeper<T extends EntityZombieVillagerCreeper> extends RenderBiped<T> implements ITakumiRender {
    private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_villager.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_farmer.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_librarian.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_priest.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_smith.png");
    private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/zombie_villager/zombie_butcher.png");

    public RenderZombieVillagerCreeper(RenderManager p_i47186_1_) {
        super(p_i47186_1_, new ModelZombieVillagerCreeper(), 0.5F);
        this.addLayer(new LayerVillagerCreeperArmor(this));
        this.addLayer(new LayerTakumiCharge(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        switch (entity.getProfession()) {
            case 0:
                return ZOMBIE_VILLAGER_FARMER_LOCATION;
            case 1:
                return ZOMBIE_VILLAGER_LIBRARIAN_LOC;
            case 2:
                return ZOMBIE_VILLAGER_PRIEST_LOCATION;
            case 3:
                return ZOMBIE_VILLAGER_SMITH_LOCATION;
            case 4:
                return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
            case 5:
            default:
                return ZOMBIE_VILLAGER_TEXTURES;
        }
    }

    @Override
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if (entityLiving.isConverting()) {
            rotationYaw += (float) (Math.cos((double) entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
        }

        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelZombieVillagerCreeper(2.0f, 0.0f, true);
    }
}
