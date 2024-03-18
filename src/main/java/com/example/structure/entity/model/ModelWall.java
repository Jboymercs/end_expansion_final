package com.example.structure.entity.model;

import com.example.structure.entity.EntityWall;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelWall extends AnimatedGeoModel<EntityWall> {
    @Override
    public ResourceLocation getModelLocation(EntityWall object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/geo.wall.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWall object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wall.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityWall animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wall.json");
    }
}
