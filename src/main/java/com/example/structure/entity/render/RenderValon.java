package com.example.structure.entity.render;

import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.model.ModelMiniValon;
import com.example.structure.entity.trader.EntityMiniValon;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModReference;
import com.example.structure.util.RenderUtil;
import net.minecraft.block.BlockBeacon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderValon extends GeoEntityRenderer<EntityMiniValon> {
    public RenderValon(RenderManager renderManager) {
        super(renderManager, new ModelMiniValon());
    }

    public ResourceLocation beam_textures = new ResourceLocation(ModReference.MOD_ID, "textures/entity/beam_texture.png");

    @Override
    public void doRender(EntityMiniValon entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(beam_textures);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.MAELSTROM, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }


}
