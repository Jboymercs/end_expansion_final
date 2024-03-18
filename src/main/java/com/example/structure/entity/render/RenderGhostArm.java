package com.example.structure.entity.render;

import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityGhostArm;
import com.example.structure.entity.model.ModelGhostArm;
import com.example.structure.entity.render.geo.RenderAbstract;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderGhostArm extends RenderAbstractGeoEntity<EntityGhostArm> {

    public RenderGhostArm(RenderManager renderManager) {
        super(renderManager, new ModelGhostArm());
        this.shadowSize = 0.0f;
    }

    @Override
    public void doRender(EntityGhostArm entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
