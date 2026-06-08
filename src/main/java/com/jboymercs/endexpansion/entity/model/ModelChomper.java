package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.EntityChomper;
import com.jboymercs.endexpansion.entity.model.geo.GeoModelExtended;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelChomper extends GeoModelExtended<EntityChomper> {


    public ModelChomper(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityChomper animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.chomper.json");
    }
}
