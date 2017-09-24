package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.client.model.ModelZombieVillagerCreeper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerVillagerCreeperArmor extends LayerBipedArmor {
    public LayerVillagerCreeperArmor(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
    }

    @Override
    protected void initArmor() {
        this.modelLeggings = new ModelZombieVillagerCreeper(0.5F, 0.0F, true);
        this.modelArmor = new ModelZombieVillagerCreeper(1.0F, 0.0F, true);
    }
}