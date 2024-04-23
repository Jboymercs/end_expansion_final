package com.example.structure.entity.render;


import com.example.structure.entity.endking.EntityLargeAOEEffect;
import com.example.structure.entity.model.ModelLargeAOE;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderLargeAOEEffect extends GeoEntityRenderer<EntityLargeAOEEffect> {
    public RenderLargeAOEEffect(RenderManager renderManager) {
        super(renderManager, new ModelLargeAOE());
    }

    @Override
    public void doRender(EntityLargeAOEEffect entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
