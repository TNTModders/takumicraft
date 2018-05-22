package com.tntmodders.takumi.client.gui;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiTakumiBook extends GuiScreen {

    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
    private static final ResourceLocation BOOK_GUI_TEXTURES_SP =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/book/book.png");
    private final EntityPlayer player;
    private final int bookImage = 192;
    private int time;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    private int currPage;
    private int bookTotalPages;

    public GuiTakumiBook(EntityPlayer player) {
        this.player = player;
        this.bookTotalPages = TakumiEntityCore.getEntityList().size();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (this.currPage != Integer.MAX_VALUE) {
            GL11.glPushMatrix();
            ITakumiEntity takumiEntity = TakumiEntityCore.getEntityList().get(this.currPage);
            boolean flg = false;
            if (TakumiUtils.getAdvancementUnlocked(
                    new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_" + takumiEntity.getRegisterName()))) {
                flg = true;
            }
            EntityLivingBase base = this.getEntity(takumiEntity, flg);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.width - 192) / 2;
            int j = 2;
            this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
            this.drawTexturedModalRect(i, 2, 0, 0, this.bookImage, this.bookImage);

            ResourceLocation location = flg && takumiEntity.takumiType().getId() < 8 ?
                    new ResourceLocation(TakumiCraftCore.MODID,
                            "textures/book/" + takumiEntity.takumiType().getName() + ".png") :
                    new ResourceLocation(TakumiCraftCore.MODID, "textures/book/underfound.png");
            this.mc.getTextureManager().bindTexture(location);
            this.drawTexturedModalRect(i, 2, 0, 0, this.bookImage, this.bookImage);

            if (flg) {
                if (takumiEntity.takumiType().isMagic()) {
                    ResourceLocation location2 = new ResourceLocation(TakumiCraftCore.MODID, "textures/book/magic.png");
                    this.mc.getTextureManager().bindTexture(location2);
                    this.drawTexturedModalRect(i, 2, 0, 0, this.bookImage, this.bookImage);
                }
                if (takumiEntity.takumiType().isDest()) {
                    ResourceLocation location2 = new ResourceLocation(TakumiCraftCore.MODID, "textures/book/dest.png");
                    this.mc.getTextureManager().bindTexture(location2);
                    this.drawTexturedModalRect(i, 2, 0, 0, this.bookImage, this.bookImage);
                }
            }

            String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
            String s5 = "???";
            String s6 = "???";
            String s7 = "";

            if (flg && this.currPage >= 0 && this.currPage < TakumiEntityCore.getEntityList().size()) {
                s5 = TakumiUtils.takumiTranslate(
                        "entity." + TakumiEntityCore.getEntityList().get(this.currPage).getRegisterName() + ".name");
                s7 = TakumiUtils.takumiTranslate(
                        "entity." + TakumiEntityCore.getEntityList().get(this.currPage).getRegisterName() + ".read");
                s6 = TakumiUtils.takumiTranslate(
                        "entity." + TakumiEntityCore.getEntityList().get(this.currPage).getRegisterName() + ".desc");
                if (TakumiEntityCore.getEntityList().get(this.currPage) instanceof EntityShootingCreeper &&
                        EntityShootingCreeper.useChoco(Minecraft.getMinecraft().world)) {
                    s6 = TakumiUtils.takumiTranslate(
                            "entity." + TakumiEntityCore.getEntityList().get(this.currPage).getRegisterName() +
                                    ".desc" + ".choco");
                }
            }

            int j1 = this.fontRenderer.getStringWidth(s4);
            this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);
            this.fontRenderer.drawSplitString(s5, i + 80, 34, 70, 0);
            if (!s7.isEmpty() && !s7.endsWith(".read")) {
                this.fontRenderer.drawSplitString(s7, i + 70, 25, 100, 0x111111);
            }
            this.fontRenderer.drawSplitString(s6, i + 40, 100, 116, 0);
            super.drawScreen(mouseX, mouseY, partialTicks);
            int k = (this.width - this.bookImage) / 2;
            byte b0 = 2;
            if (base != null) {
                this.renderEntity(k + 51, b0 + 75, 30, (float) (k + 51) - this.bookImage,
                        (float) (b0 + 75 - 50) - this.bookImage, base, flg);
            }
            GL11.glPopMatrix();
        } else {
            this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES_SP);
            GL11.glPushMatrix();
            GL11.glScaled(1.9, 1, 1);
            this.drawTexturedModalRect(2, 2, 0, 0, 256, 256);
            GL11.glPopMatrix();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    private EntityLivingBase getEntity(ITakumiEntity entity, boolean flg) {
        if (entity instanceof EntityGiantCreeper) {
            return new EntityZombieCreeper(((Entity) entity).world);
        }
        if (entity instanceof EntitySilentCreeper && !flg) {
            return new EntityCreeper(((Entity) entity).world);
        }
        if (entity instanceof EntitySlimeCreeper) {
            ((EntitySlimeCreeper) entity).setSlimeSize(2, false);
        } else if (entity instanceof EntitySkeletonCreeper) {
            if (entity instanceof EntityStrayCreeper) {
                if (!flg) {
                    entity = new EntitySkeletonCreeper(((Entity) entity).world);
                }
                ItemStack stack = new ItemStack(Items.POTIONITEM);
                PotionUtils.addPotionToItemStack(stack, PotionType.getPotionTypeForName("water"));
                ((EntitySkeletonCreeper) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack, false);
            }
            ((EntitySkeletonCreeper) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND,
                    entity instanceof EntityWitherSkeletonCreeper ? new ItemStack(TakumiItemCore.TAKUMI_SWORD) :
                            new ItemStack(TakumiItemCore.TAKUMI_BOW), false);
        } else if (entity instanceof EntitySquidCreeper) {
            ((EntitySquidCreeper) entity).tentacleAngle = 45;
            ((EntitySquidCreeper) entity).squidPitch = 60;
        } else if (entity instanceof EntitySheepCreeper && !flg) {
            ((EntitySheepCreeper) entity).setSheared(true);
        } else if (entity instanceof EntityVindicatorCreeper) {
            ((EntityVindicatorCreeper) entity).setAggressive(true);
            ((Entity) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND,
                    new ItemStack(TakumiItemCore.TAKUMI_SHIELD));
            ((Entity) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND,
                    new ItemStack(TakumiItemCore.TAKUMI_SWORD));
        } else if (entity instanceof EntityIllusionerCreeper) {
            ((EntityIllusionerCreeper) entity).setAggressive(true);
            ((Entity) entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND,
                    new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA));
            ((Entity) entity).setItemStackToSlot(EntityEquipmentSlot.MAINHAND,
                    new ItemStack(TakumiItemCore.TAKUMI_BOW));
        }
        return (EntityLivingBase) entity;
    }

    public void renderEntity(int x, int y, int z, float yaw, float pitch, EntityLivingBase base, boolean flg) {
        GL11.glPushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glTranslatef(x, y, 50.0F);
        GL11.glScalef(-z, z, z);
        this.renderSize(base);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = base.renderYawOffset;
        float f3 = base.rotationYaw;
        float f4 = base.rotationPitch;
        float f5 = base.prevRotationYawHead;
        float f6 = base.rotationYawHead;
        RenderHelper.disableStandardItemLighting();
        //RenderHelper.enableGUIStandardItemLighting();
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan(pitch / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        base.renderYawOffset = -10 + time * 0.5f;
        base.rotationYaw = -10 + time * 0.5f;
        base.rotationPitch = 0;
        base.rotationYawHead = -10f + time * 0.5f;
        base.prevRotationYawHead = -10f + time * 0.5f;
        GL11.glTranslatef(0.0F, (float) base.getYOffset(), 0.0F);
        Minecraft.getMinecraft().getRenderManager().playerViewY = 180.0F;
        if (!flg) {
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        }
        this.renderEntityWithPosYaw(base);
        base.renderYawOffset = f2;
        base.rotationYaw = f3;
        base.rotationPitch = f4;
        base.prevRotationYawHead = f5;
        base.rotationYawHead = f6;
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        ////GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glPopMatrix();
    }

    private void renderSize(EntityLivingBase entity) {
        if (this.currPage == Integer.MAX_VALUE) {
            GL11.glScaled(0.3, 0.3, 0.3);
        }
        if (entity instanceof EntityTakumiAbstractCreeper && ((EntityTakumiAbstractCreeper) entity).getSizeAmp() != 1) {
            double d = ((EntityTakumiAbstractCreeper) entity).getSizeAmp();
            GL11.glScaled(1 / d, 1 / d, 1 / d);
        }
        if (entity instanceof EntitySquidCreeper) {
            GL11.glScaled(0.65, 0.65, 0.65);
            GL11.glTranslated(-1, -1, 0);
        } else if (entity instanceof EntityGhastCreeper) {
            GL11.glScaled(0.2, 0.2, 0.2);
        } else if (entity instanceof EntityDarkCreeper) {
            GL11.glScaled(0.7, 0.7, 0.7);
        }
    }

    void renderEntityWithPosYaw(EntityLivingBase p_147939_1_) {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        Render<EntityLivingBase> render;

        try {
            render = manager.getEntityRenderObject(p_147939_1_);

            if (render != null && manager.renderEngine != null) {
                try {
                    p_147939_1_.world = this.player.world;
                    render.doRender(p_147939_1_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0f);
                } catch (Throwable throwable2) {
                    throw new ReportedException(
                            CrashReport.makeCrashReport(throwable2, "Rendering entity in takumibook"));
                }

                try {
                    render.doRenderShadowAndFire(p_147939_1_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0f);
                } catch (Throwable throwable1) {
                    throw new ReportedException(
                            CrashReport.makeCrashReport(throwable1, "Post-rendering entity in takumibook"));
                }
            }
        } catch (Throwable throwable3) {
            throwable3.printStackTrace();
        }
    }

    /**
     * Executes the click event specified by the given chat component
     */
    @Override
    public boolean handleComponentClick(ITextComponent component) {
        ClickEvent clickevent = component.getStyle().getClickEvent();

        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == Action.CHANGE_PAGE) {
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

            if (flag && clickevent.getAction() == Action.RUN_COMMAND) {
                this.mc.displayGuiScreen(null);
            }

            return flag;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 900) {
                this.mc.displayGuiScreen(null);
            } else if (button.id == 901) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                }
            } else if (button.id == 902) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            } else if (button.id == 903) {
                this.currPage = this.currPage == Integer.MAX_VALUE ? 0 : Integer.MAX_VALUE;
                this.buttonNextPage.enabled = !this.buttonNextPage.enabled;
                this.buttonPreviousPage.enabled = !this.buttonPreviousPage.enabled;
            } else if (button.id < TakumiEntityCore.getEntityList().size() && this.currPage == Integer.MAX_VALUE) {
                this.currPage = button.id;
                this.buttonNextPage.enabled = !this.buttonNextPage.enabled;
                this.buttonPreviousPage.enabled = !this.buttonPreviousPage.enabled;
            }
            this.updateButtons();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonDone =
                this.addButton(new GuiButton(900, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
        int i = (this.width - this.bookImage) / 2;
        int j = 2;
        this.buttonNextPage = this.addButton(new NextPageButton(901, i + 120, 156, true));
        this.buttonPreviousPage = this.addButton(new NextPageButton(902, i + 38, 156, false));
        SearchButton searchButton = this.addButton(new SearchButton(903, i + 71, 146));

        for (int t = 0; t < TakumiEntityCore.getEntityList().size(); t++) {
            this.addButton(new CreeperButton(t, 11 * (t % 40 + 2), j + 15 + 25 * (int) Math.floor(t / 40)));
        }
        this.updateButtons();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.time++;
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1 && this.currPage != Integer.MAX_VALUE;
        this.buttonPreviousPage.visible = this.currPage > 0 && this.currPage != Integer.MAX_VALUE;
        this.buttonDone.visible = true;
    }

    @SideOnly(Side.CLIENT)
    class NextPageButton extends GuiButton {

        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn) {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width &&
                        mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
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

    @SideOnly(Side.CLIENT)
    class SearchButton extends GuiButton {

        public SearchButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 40, 20, "□凸□");
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.enabled) {
                GL11.glPushMatrix();
                FontRenderer fontrenderer = mc.fontRenderer;
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width &&
                        mouseY < this.y + this.height;
                int i = this.getHoverState(this.hovered);
                //GlStateManager.enableBlend();
                //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                // GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                // GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20,
                        this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                int j = 14737632;

                if (packedFGColour != 0) {
                    j = packedFGColour;
                } else if (!this.enabled) {
                    j = 10526880;
                } else if (this.hovered) {
                    j = 16777120;
                }

                this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2,
                        this.y + (this.height - 8) / 2, j);
                GL11.glPopMatrix();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    class CreeperButton extends GuiButton {

        public CreeperButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 8, 16, TakumiUtils.takumiTranslate(
                    "entity." + TakumiEntityCore.getEntityList().get(buttonId).getRegisterName() + ".name"));
            this.x = x;
            this.y = y;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            this.visible = GuiTakumiBook.this.currPage == Integer.MAX_VALUE;
            this.enabled = GuiTakumiBook.this.currPage == Integer.MAX_VALUE;
            if (this.visible) {
                ITakumiEntity takumiEntity = TakumiEntityCore.getEntityList().get(this.id);
                boolean flg = TakumiUtils.getAdvancementUnlocked(
                        new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_" + takumiEntity.getRegisterName()));
                EntityLivingBase base = GuiTakumiBook.this.getEntity(takumiEntity, flg);
                if (base != null) {
                    GL11.glPushMatrix();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GuiTakumiBook.this.renderEntity(this.x + 5, this.y + 15, 30, 0, 0, base, flg);
                    GL11.glPopMatrix();
                }
                if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width &&
                        mouseY < this.y + this.height) {
                    String s = flg ? this.displayString : "???";
                    this.drawHoveringText(this.x + 5, this.y + 15, s);
                }
            }
        }


        private void drawHoveringText(int x, int y, String... text) {
            List<String> textLines = Arrays.asList(text);
            if (!textLines.isEmpty()) {
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int i = 0;

                for (String s : textLines) {
                    int j = mc.fontRenderer.getStringWidth(s);

                    if (j > i) {
                        i = j;
                    }
                }

                int l1 = x + 12;
                int i2 = y - 12;
                int k = 8;

                if (textLines.size() > 1) {
                    k += 2 + (textLines.size() - 1) * 10;
                }

                if (l1 + i > this.width) {
                    l1 -= 28 + i;
                }

/*                if (i2 + k + 6 > this.height) {
                    i2 = this.height - k - 6;
                }*/

                this.zLevel = 300.0F;
                int l = -267386864;
                this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
                this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
                this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
                this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
                this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
                int i1 = 1347420415;
                int j1 = 1344798847;
                this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
                this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
                this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
                this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

                for (int k1 = 0; k1 < textLines.size(); ++k1) {
                    String s1 = textLines.get(k1);
                    mc.fontRenderer.drawStringWithShadow(s1, l1, i2, -1);

                    if (k1 == 0) {
                        i2 += 2;
                    }

                    i2 += 10;
                }

                this.zLevel = 0.0F;
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();
            }
        }
    }
}
