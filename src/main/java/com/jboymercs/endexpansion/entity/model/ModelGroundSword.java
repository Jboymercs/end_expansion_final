package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.endking.EntityGroundSword;
import com.jboymercs.endexpansion.entity.model.geo.GeoModelExtended;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelGroundSword extends GeoModelExtended<EntityGroundSword> {


    public ModelGroundSword(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGroundSword animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.swordattack.json");
    }

}
