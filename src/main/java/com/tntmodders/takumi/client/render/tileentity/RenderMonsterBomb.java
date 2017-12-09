package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.tileentity.TileEntityMonsterBomb;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;

public class RenderMonsterBomb <T extends TileEntityMonsterBomb> extends TileEntitySpecialRenderer <T> {
    
    private final ModelMonsterBomb modelBase = new ModelMonsterBomb();
    
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix(); GlStateManager.enableDepth(); GlStateManager.depthFunc(515); GlStateManager.depthMask(true);
        GlStateManager.disableCull(); GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5); GlStateManager.scale(2, 2, 2);
        GlStateManager.rotate(180, 1, 0, 0); GlStateManager.translate(-0.25, -0.25, -0.25); this.bindTexture(te.location); this.modelBase.render();
        GlStateManager.popMatrix();
    }
    
    
    private class ModelMonsterBomb extends ModelBase {
        
        private final ModelRenderer box;
        
        public ModelMonsterBomb() {
            this.box = new ModelRenderer(this, 0, 0); this.box.addBox(0, 0, 0, 8, 8, 8);
        }
        
        public void render() {
            this.box.render(0.0625F);
        }
        
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float
                scale) {
        }
    }
}
