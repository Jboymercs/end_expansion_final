package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.EntityMiniNuke;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMiniNuke extends AnimatedGeoModel<EntityMiniNuke> {
    @Override
    public ResourceLocation getModelLocation(EntityMiniNuke object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/effects/geo.mini_nuke.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMiniNuke object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/mini_nuke.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMiniNuke animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.mini_nuke.json");
    }
}
