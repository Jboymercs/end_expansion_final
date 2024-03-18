package com.example.structure.entity.render;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.model.ModelEndKing;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModUtils;
import com.example.structure.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderEntityKing extends GeoEntityRenderer<EntityEndKing> {
    public RenderEntityKing(RenderManager renderManager) {
        super(renderManager, new ModelEndKing());
    }
    @Override
    public void doRender(EntityEndKing entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.RED, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    //Used for Lazer and V Rendering
    @Override
    public boolean shouldRender(@Nonnull EntityEndKing livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
        {
            return true;
        }

        Optional<Vec3d> optional = ((ITarget) livingEntity).getTarget();
        if(optional.isPresent()) {
            Vec3d end = optional.get();
            Vec3d start = livingEntity.getPositionEyes(1);
            return camera.isBoundingBoxInFrustum(ModUtils.makeBox(start, end));
        }

        return false;
    }
}
