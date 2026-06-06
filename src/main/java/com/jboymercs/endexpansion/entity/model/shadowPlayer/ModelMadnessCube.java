package com.jboymercs.endexpansion.entity.model.shadowPlayer;

import com.jboymercs.endexpansion.entity.model.geo.GeoModelExtended;
import com.jboymercs.endexpansion.entity.shadowPlayer.EntityMadnessCube;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelMadnessCube extends GeoModelExtended<EntityMadnessCube> {

    public ModelMadnessCube(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMadnessCube animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.evil_cube.json");
    }
}
