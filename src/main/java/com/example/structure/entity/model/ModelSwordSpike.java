package com.example.structure.entity.model;

import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.entity.EntitySwordSpike;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSwordSpike extends AnimatedGeoModel<EntitySwordSpike> {
    @Override
    public ResourceLocation getModelLocation(EntitySwordSpike object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/geo.sword_spike.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySwordSpike object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/sword_spike.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySwordSpike animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sword_spike.json");
    }
}
