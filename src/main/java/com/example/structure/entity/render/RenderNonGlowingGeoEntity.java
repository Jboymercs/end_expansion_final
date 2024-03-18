package com.example.structure.entity.render;

import com.example.structure.entity.EntityModBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.function.Function;

public class RenderNonGlowingGeoEntity <T extends EntityModBase & IAnimatable> extends GeoEntityRenderer<T> {
    public final Function<T, ResourceLocation> TEXTURE_LOCATION;
    public final Function<T, ResourceLocation> MODEL_LOCATION;

    public RenderNonGlowingGeoEntity(RenderManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);

        this.MODEL_LOCATION = modelProvider::getModelLocation;
        this.TEXTURE_LOCATION = modelProvider::getTextureLocation;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.TEXTURE_LOCATION.apply(entity);
    }
}
