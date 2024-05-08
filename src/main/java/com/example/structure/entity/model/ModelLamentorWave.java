package com.example.structure.entity.model;

import com.example.structure.entity.lamentorUtil.EntityLamentorWave;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelLamentorWave extends AnimatedGeoModel<EntityLamentorWave> {
    @Override
    public ResourceLocation getModelLocation(EntityLamentorWave object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/geo.arena.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLamentorWave object) {
        if(object.ticksExisted > 42) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lam_arena/arena_0.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lam_arena/arena_" + object.ticksExisted + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLamentorWave animatable) {
        return null;
    }
}
