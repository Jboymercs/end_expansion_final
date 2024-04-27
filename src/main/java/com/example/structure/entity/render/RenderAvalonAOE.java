package com.example.structure.entity.render;

import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.model.ModelAvalonAOE;
import com.example.structure.entity.trader.EntityAOEArena;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderAvalonAOE extends RenderAbstractGeoEntity<EntityAOEArena>{

    public RenderAvalonAOE(RenderManager renderManager) {
        super(renderManager, new ModelAvalonAOE());
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(EntityAOEArena entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
