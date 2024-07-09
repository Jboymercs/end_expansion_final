package com.example.structure.entity.painting;

import com.example.structure.util.ModReference;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderEEPainting extends Render<EntityEEPainting> {


    public RenderEEPainting(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityEEPainting entity) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/painting/lamented_islands.png");
    }


    @Override
    public void doRender(EntityEEPainting entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180 - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);



        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        float relWidth = 64;
        float relHeight = 32;

        float width = relWidth / 16 / 2;
        float height = relHeight / 16 / 2;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        buffer.pos(width, -height, 0).tex(0, 1).normal(0, 0, 1).endVertex();
        buffer.pos(-width, -height, 0).tex(1, 1).normal(0, 0, 1).endVertex();
        buffer.pos(-width, height, 0).tex(1, 0).normal(0, 0, 1).endVertex();
        buffer.pos(width, height, 0).tex(0, 0).normal(0, 0, 1).endVertex();

        tessellator.draw();



        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
