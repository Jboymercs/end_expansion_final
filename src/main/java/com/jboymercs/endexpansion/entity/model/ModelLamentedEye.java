package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.EntityLamentedEye;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelLamentedEye extends AnimatedGeoModel<EntityLamentedEye> {
    @Override
    public ResourceLocation getModelLocation(EntityLamentedEye object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/geo.lame.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLamentedEye object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/lame.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLamentedEye animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lame.json");
    }
}
