package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.barrend.EntityVoidTripod;
import com.jboymercs.endexpansion.entity.model.geo.GeoModelExtended;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;

public class ModelVoidTripod extends GeoModelExtended<EntityVoidTripod> {

    public ModelVoidTripod(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidTripod animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.tripod.json");
    }

    @Override
    public void setLivingAnimations(EntityVoidTripod entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
