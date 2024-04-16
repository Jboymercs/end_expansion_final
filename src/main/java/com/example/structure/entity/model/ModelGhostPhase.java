package com.example.structure.entity.model;

import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelGhostPhase extends AnimatedGeoModel<EntityGhostPhase> {
    @Override
    public ResourceLocation getModelLocation(EntityGhostPhase object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/king/geo.ghost_king.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGhostPhase object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/ghost_king.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGhostPhase animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ghostking.json");
    }


    @Override
    public void setLivingAnimations(EntityGhostPhase entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadStart");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }


    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
