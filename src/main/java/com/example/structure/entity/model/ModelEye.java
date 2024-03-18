package com.example.structure.entity.model;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityEye;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEye extends AnimatedGeoModel<EntityEye> {
    @Override
    public ResourceLocation getModelLocation(EntityEye object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/ring/geo.ring.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEye object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/ring.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEye animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ring.json");
    }

    @Override
    public void setLivingAnimations(EntityEye entity, Integer uniqueID, AnimationEvent customPredicate) {
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
