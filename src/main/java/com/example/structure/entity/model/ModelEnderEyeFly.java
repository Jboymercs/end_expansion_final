package com.example.structure.entity.model;

import com.example.structure.entity.EntityEnderEyeFly;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEnderEyeFly extends AnimatedGeoModel<EntityEnderEyeFly> {
    @Override
    public ResourceLocation getModelLocation(EntityEnderEyeFly object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/eye/geo.eye.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEnderEyeFly object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/eye.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEnderEyeFly animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.eye.json");
    }

    @Override
    public void setLivingAnimations(EntityEnderEyeFly entity, Integer uniqueID, AnimationEvent customPredicate) {
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
