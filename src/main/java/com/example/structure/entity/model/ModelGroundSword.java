package com.example.structure.entity.model;

import com.example.structure.entity.endking.EntityGroundSword;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
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
