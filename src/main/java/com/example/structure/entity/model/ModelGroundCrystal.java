package com.example.structure.entity.model;

import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGroundCrystal extends AnimatedGeoModel<EntityGroundCrystal> {

    @Override
    public ResourceLocation getModelLocation(EntityGroundCrystal entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/groundcrystal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGroundCrystal entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/crystal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGroundCrystal entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.groundcrystal.json");
    }
}
