package com.example.structure.entity.render;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.model.ModelEnderKnight;
import com.example.structure.entity.model.ModelEnderMage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderEnderMage extends GeoEntityRenderer<EntityEnderMage> {

    public RenderEnderMage(RenderManager renderManager) {
        super(renderManager, new ModelEnderMage());
    }

    @Override
    public void doRender(EntityEnderMage entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
