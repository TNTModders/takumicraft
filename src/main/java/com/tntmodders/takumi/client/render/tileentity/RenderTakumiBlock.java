package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class RenderTakumiBlock <T extends TileEntityTakumiBlock> extends TileEntitySpecialRenderer <T> {
    
    private static final ResourceLocation TEXTURE_ARMOR = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final ArrayList <ModelTakumiBlock> takumiBlocks = new ArrayList <>();
    
    public RenderTakumiBlock() {
        for (int i = 0; i < 64; i++) {
            takumiBlocks.add(new ModelTakumiBlock(i));
        }
    }
    
    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.getBlock() != null && te.getBlock() != Blocks.AIR) {
            GlStateManager.pushMatrix();
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState state = te.state != null ? te.state : te.getBlock().getStateFromMeta(te.getMeta());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.draw(te, x, y, z, state, pos, buffer, tessellator);
            GlStateManager.popMatrix();
        }
        if (te.getBlock() == null || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(TakumiBlockCore
                .TAKUMI_BLOCK) || Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(TakumiBlockCore
                .TAKUMI_BLOCK)) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            this.bindTexture(TEXTURE_ARMOR);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            float f = Minecraft.getMinecraft().world.getWorldTime();
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.enableBlend();
            float f4 = 0.5F;
            GlStateManager.color(f4, f4, f4, 1.0F);
            GlStateManager.enableLighting();
            GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            
            GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_ARMOR);
            takumiBlocks.get(Minecraft.getMinecraft().player.ticksExisted % 64).render();
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    private void draw(T te, double x, double y, double z, IBlockState state, BlockPos pos, BufferBuilder buffer, Tessellator tessellator) {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        } else {
            GlStateManager.shadeModel(7424);
        }
        buffer.begin(7, DefaultVertexFormats.BLOCK);
        buffer.setTranslation((float) x - pos.getX(), (float) y - pos.getY(), (float) z - pos.getZ());
        World world = this.getWorld();
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        state = state.getBlock().getExtendedState(state, world, pos);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, model, state, pos, buffer, true);
        buffer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }
    
    private class ModelTakumiBlock extends ModelBase {
        
        private final ModelRenderer box;
        
        public ModelTakumiBlock(int i) {
            this.box = new ModelRenderer(this, i, i);
            this.box.addBox(-8, -8, -8, 16, 16, 16);
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
