package com.jboymercs.endexpansion.entity.model;

import com.jboymercs.endexpansion.entity.model.geo.GeoModelExtended;
import com.jboymercs.endexpansion.entity.seekers.EndSeeker;
import com.jboymercs.endexpansion.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEndSeeker extends GeoModelExtended<EndSeeker> {

    public ModelEndSeeker(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EndSeeker animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.seeker.json");
    }

    @Override
    public void setLivingAnimations(EndSeeker entity, Integer uniqueID , AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
