package com.example.structure.entity.model;

import com.example.structure.entity.endking.friendly.EntityFriendKing;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelFriendKing  extends AnimatedGeoModel<EntityFriendKing> {

    @Override
    public ResourceLocation getModelLocation(EntityFriendKing object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/king/geo.king.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFriendKing object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/king_1.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFriendKing animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.king.json");
    }

    @Override
    public void setLivingAnimations(EntityFriendKing entity, Integer uniqueID , AnimationEvent customPredicate) {
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
