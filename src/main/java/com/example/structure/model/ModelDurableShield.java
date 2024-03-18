package com.example.structure.model;

import com.example.structure.items.DurableShield;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDurableShield extends AnimatedGeoModel<DurableShield> {
    @Override
    public ResourceLocation getModelLocation(DurableShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.durable_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DurableShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/durable_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DurableShield animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.durable_shield.json");
    }
}
