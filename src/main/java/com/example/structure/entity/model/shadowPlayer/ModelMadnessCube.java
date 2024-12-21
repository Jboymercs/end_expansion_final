package com.example.structure.entity.model.shadowPlayer;

import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.entity.shadowPlayer.EntityMadnessCube;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModReference;
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
