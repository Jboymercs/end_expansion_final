package com.example.structure.entity.model;

import com.example.structure.entity.barrend.EntityVoidTripod;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
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
