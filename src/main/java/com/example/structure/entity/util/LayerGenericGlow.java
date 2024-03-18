package com.example.structure.entity.util;

import com.example.structure.entity.render.geo.RenderAbstract;
import com.example.structure.util.EmissiveLighting;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.function.Function;

public class LayerGenericGlow <T extends EntityLiving & IAnimatable> extends RenderAbstract<T> {

    public LayerGenericGlow(GeoEntityRenderer<T> entityRendererIn, Function<T, ResourceLocation> resouceLocation, Function<T, ResourceLocation> modelLocation) {
        super(entityRendererIn, resouceLocation, modelLocation);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }


    @Override
    public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
        EmissiveLighting.preEmissiveTextureRendering();

        this.geoRendererInstance.bindTexture((this.funcGetCurrentTexture.apply(entitylivingbaseIn)));

        this.reRenderCurrentModelInRenderer(entitylivingbaseIn, partialTicks, renderColor);

        EmissiveLighting.postEmissiveTextureRendering();
    }
}
