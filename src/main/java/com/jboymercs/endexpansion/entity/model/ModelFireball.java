package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.endking.EntityFireBall;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelFireball extends AnimatedGeoModel<EntityFireBall> {
    @Override
    public ResourceLocation getModelLocation(EntityFireBall entityFireBall) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/king/geo.fireball.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFireBall entityFireBall) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/fireball_red.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFireBall entityFireBall) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.fireball.json");
    }
}
