package com.example.structure.sky;

import com.example.structure.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCustomEndSky {
    private final Minecraft mc;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient world;

    private boolean vboEnabled;

    private ChunkRenderContainer renderContainer;
    IRenderChunkFactory renderChunkFactory;
    private final VertexFormat vertexBufferFormat;

    private VertexBuffer skyVBO;

    private int glSkyList2 = -1;

    private int glSkyList = -1;

    private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky.png");

    private static final ResourceLocation END_SKY_LEFT = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky_left.png");
    private static final ResourceLocation END_SKY_RIGHT = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky_right.png");
    private static final ResourceLocation END_SKY_UP = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky_up.png");
    private static final ResourceLocation END_SKY_DOWN = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky_down.png");
    private static final ResourceLocation END_SKY_BACK = new ResourceLocation(ModReference.MOD_ID, "textures/sky/new_sky_back.png");

    public RenderCustomEndSky(Minecraft mcIn, WorldClient world) {
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        this.world = world;
        this.mc = mcIn;
        GlStateManager.glTexParameteri(3553, 10242, 10497);
        GlStateManager.glTexParameteri(3553, 10243, 10497);
        GlStateManager.bindTexture(0);
        this.vboEnabled = OpenGlHelper.useVbo();

        if (this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        } else {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }

        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
      //      this.generateSky();
    }


    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSkyEnd(mc);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyVBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GlStateManager.glNewList(this.glSkyList, 4864);
            this.renderSkyEnd(mc);
            tessellator.draw();
            GlStateManager.glEndList();
        }
    }

    public void renderSkyEnd(Minecraft mc)
    {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        mc.renderEngine.bindTexture(END_SKY_TEXTURES);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int k1 = 0; k1 < 6; ++k1)
        {
            GlStateManager.pushMatrix();

            if (k1 == 1)
            {
                mc.renderEngine.bindTexture(END_SKY_BACK);

                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 2)
            {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 3)
            {
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (k1 == 4)
            {
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (k1 == 5)
            {
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    public void renderFlatSky( Minecraft mc, boolean renderClipPlane) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableFog();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();

        GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
        //UP
        mc.renderEngine.bindTexture(END_SKY_UP);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        //DOWN
        GlStateManager.pushMatrix();
        GlStateManager.rotate(0F, 1.0F, 0.0F, 0.0F);
        mc.renderEngine.bindTexture(END_SKY_DOWN);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        //FRONT
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
        mc.renderEngine.bindTexture(END_SKY_TEXTURES);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        //RIGHT
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90F, 0.0F, 0.0F, 1.0F);
        mc.renderEngine.bindTexture(END_SKY_RIGHT);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
        //LEFT
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
        mc.renderEngine.bindTexture(END_SKY_LEFT);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        //BACK
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90F, 1.0F, 0.0F, 0.0F);
        mc.renderEngine.bindTexture(END_SKY_BACK);
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).endVertex();
        vertexBuffer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, 100.0D).tex(1.0D, 1.0D).endVertex();
        vertexBuffer.pos(100.0D, -100.0D, -100.0D).tex(1.0D, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        System.out.println("Rendered SkyBox!");
    }
}
