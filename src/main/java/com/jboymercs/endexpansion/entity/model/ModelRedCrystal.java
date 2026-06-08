package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.endking.EntityRedCrystal;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelRedCrystal extends AnimatedGeoModel<EntityRedCrystal> {
    @Override
    public ResourceLocation getModelLocation(EntityRedCrystal entityRedCrystal) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/groundcrystal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRedCrystal entityRedCrystal) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/crystal_red.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRedCrystal entityRedCrystal) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.groundcrystal.json");
    }
}
