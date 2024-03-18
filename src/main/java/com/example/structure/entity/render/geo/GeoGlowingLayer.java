package com.example.structure.entity.render.geo;

import com.example.structure.util.EmissiveLighting;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.function.Function;

public class GeoGlowingLayer<T extends EntityLiving & IAnimatable> extends RenderAbstract<T>{

    public GeoGlowingLayer(GeoEntityRenderer<T> renderer, Function<T, ResourceLocation> funcGetCurrentTexture, Function<T, ResourceLocation> funcGetCurrentModel) {
        super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
    }

    @Override
    public void render(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {

        EmissiveLighting.preEmissiveTextureRendering();

        this.geoRendererInstance.bindTexture(AutoGlowingTexture.get(this.funcGetCurrentTexture.apply(entitylivingbaseIn)));

        this.reRenderCurrentModelInRenderer(entitylivingbaseIn, partialTicks, renderColor);

        EmissiveLighting.postEmissiveTextureRendering();
    }
}
