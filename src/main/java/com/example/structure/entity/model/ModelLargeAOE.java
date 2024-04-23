package com.example.structure.entity.model;

import com.example.structure.entity.endking.EntityLargeAOEEffect;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelLargeAOE extends AnimatedGeoModel<EntityLargeAOEEffect> {
    @Override
    public ResourceLocation getModelLocation(EntityLargeAOEEffect object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/geo.large_aoe.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLargeAOEEffect object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/large_aoe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLargeAOEEffect animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.large_aoe.json");
    }
}
