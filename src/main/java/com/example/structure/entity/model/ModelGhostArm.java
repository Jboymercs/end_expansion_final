package com.example.structure.entity.model;

import com.example.structure.entity.EntityGhostArm;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelGhostArm extends AnimatedGeoModel<EntityGhostArm> {
    @Override
    public ResourceLocation getModelLocation(EntityGhostArm object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/geo.ghost_arm.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGhostArm object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/king_top.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGhostArm animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ghost_arm.json");
    }

    @Override
    public void setLivingAnimations(EntityGhostArm entity, Integer uniqueID , AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Torso");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
