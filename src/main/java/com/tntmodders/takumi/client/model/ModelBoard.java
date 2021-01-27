package com.tntmodders.takumi.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBoard extends ModelBase {

    private final ModelRenderer box;

    public ModelBoard() {
        this.box = new ModelRenderer(this, 0, 0);
        this.box.setTextureSize(1024, 1024);
        this.box.addBox(0, 0, 0, 1024, 1024, 1024);
    }

    public void render() {
        this.box.render(0.0625F / 16);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                       float headPitch, float scale) {
    }
}
