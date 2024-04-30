package com.example.structure.entity.model;

import com.example.structure.entity.trader.EntityAOEArena;
import com.example.structure.entity.trader.EntityControllerLift;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelControllerLift extends AnimatedGeoModel<EntityControllerLift> {
    @Override
    public ResourceLocation getModelLocation(EntityControllerLift object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/geo.arena.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityControllerLift object) {
        if(object.ticksExisted > 21) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/arena/arena_0.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/controller_arena/arena_" + object.ticksExisted + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityControllerLift animatable) {
        return null;
    }
}
