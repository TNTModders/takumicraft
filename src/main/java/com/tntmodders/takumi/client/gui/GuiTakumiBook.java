package com.tntmodders.takumi.client.gui;

import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.mobs.EntitySlimeCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTakumiBook extends GuiScreen {

    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
    private final EntityPlayer player;
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    private int time = 0;
    private GuiTakumiBook.NextPageButton buttonNextPage;
    private GuiTakumiBook.NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    private int currPage;
    private int bookTotalPages = 1;

    public GuiTakumiBook(EntityPlayer player) {
        this.player = player;
        this.bookTotalPages = TakumiEntityCore.entityList.size();

    }


    public void initGui() {
        this.buttonList.clear();
        this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));

        int i = (this.width - 192) / 2;
        int j = 2;
        this.buttonNextPage = this.addButton(new NextPageButton(1, i + 120, 156, true));
        this.buttonPreviousPage = this.addButton(new NextPageButton(2, i + 38, 156, false));
        this.updateButtons();
    }

    public void updateScreen() {
        super.updateScreen();
        this.time++;
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonDone.visible = true;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(null);
            } else if (button.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                }
            } else if (button.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            }
            this.updateButtons();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
        String s5 = "";

        if (this.currPage >= 0 && this.currPage < TakumiEntityCore.entityList.size()) {
            s5 = TakumiUtils.takumiTranslate("entity." + TakumiEntityCore.entityList.get(this.currPage).getRegisterName() + ".name");
        }

        int j1 = this.fontRenderer.getStringWidth(s4);
        this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);

        {
            this.fontRenderer.drawSplitString(s5, i + 80, 34, 116, 0);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        EntityLivingBase base = this.getEntity(TakumiEntityCore.entityList.get(this.currPage));
        if (base != null) {
            this.renderEntity(k + 51, b0 + 75, 30, (float) (k + 51) - this.bookImageWidth, (float) (b0 + 75 - 50) - this.bookImageHeight, base);

        }
    }

    protected EntityLivingBase getEntity(ITakumiEntity entity) {
        if (entity instanceof EntitySlimeCreeper) {
            ((EntitySlimeCreeper) entity).setSlimeSize(2, false);
        }
        return ((EntityLivingBase) entity);
    }

    public void renderEntity(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_147046_0_, (float) p_147046_1_, 50.0F);
        GL11.glScalef((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p_147046_5_.renderYawOffset;
        float f3 = p_147046_5_.rotationYaw;
        float f4 = p_147046_5_.rotationPitch;
        float f5 = p_147046_5_.prevRotationYawHead;
        float f6 = p_147046_5_.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = /*(float) Math.atan((double) (p_147046_3_ / 40.0F)) * 20.0F*/-10 + (time * 0.5f);
        p_147046_5_.rotationYaw = /*(float) Math.atan((double) (p_147046_3_ / 40.0F)) * 40.0F*/-10 + (time * 0.5f);
        p_147046_5_.rotationPitch = /*(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F)*/ 0;
        p_147046_5_.rotationYawHead = -10f + (time * 0.5f);
        p_147046_5_.prevRotationYawHead = -10f + (time * 0.5f);
        GL11.glTranslatef(0.0F, ((float) p_147046_5_.getYOffset()), 0.0F);
        Minecraft.getMinecraft().getRenderManager().playerViewY = 180.0F;

        this.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0f, false);
        p_147046_5_.renderYawOffset = f2;
        p_147046_5_.rotationYaw = f3;
        p_147046_5_.rotationPitch = f4;
        p_147046_5_.prevRotationYawHead = f5;
        p_147046_5_.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    void renderEntityWithPosYaw(EntityLivingBase p_147939_1_, double p_147939_2_, double p_147939_4_, double p_147939_6_, float p_147939_8_, float p_147939_9_, boolean p_147939_10_) {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        Render render = null;

        try {
            render = manager.getEntityRenderObject(p_147939_1_);

            if (render != null && manager.renderEngine != null) {
                try {
                    p_147939_1_.world = this.player.world;
                    render.doRender(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                } catch (Throwable throwable2) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }

                try {
                    render.doRenderShadowAndFire(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            throwable3.printStackTrace();
        }
    }

    /**
     * Executes the click event specified by the given chat component
     */
    public boolean handleComponentClick(ITextComponent component) {
        ClickEvent clickevent = component.getStyle().getClickEvent();

        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();

            try {
                int i = Integer.parseInt(s) - 1;

                if (i >= 0 && i < this.bookTotalPages && i != this.currPage) {
                    this.currPage = i;
                    this.updateButtons();
                    return true;
                }
            } catch (Throwable var5) {
                var5.printStackTrace();
            }

            return false;
        } else {
            boolean flag = super.handleComponentClick(component);

            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.mc.displayGuiScreen(null);
            }

            return flag;
        }
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {
        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn) {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GuiTakumiBook.BOOK_GUI_TEXTURES);
                int i = 0;
                int j = 192;

                if (flag) {
                    i += 23;
                }

                if (!this.isForward) {
                    j += 13;
                }

                this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
            }
        }
    }
}
