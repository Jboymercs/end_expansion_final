package com.example.structure.gui;

import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.server.container.ContainterAltar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAltar extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("ee:textures/gui/altar.png");
    private TileEntityAltar altar;
    public GuiAltar(EntityPlayer inventory, TileEntityAltar altar) {
        super(new ContainterAltar(inventory, altar));
        this.altar = altar;
        ySize = 183;
        xSize = 176;
    }
    //COME BACK TOO

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String s = altar.hasCustomName() ? altar.getName() : I18n.format(altar.getName(), new Object[0]);
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 18, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 90, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
