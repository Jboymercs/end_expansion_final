package com.example.structure.entity.model;


import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEnderShield extends GeoModelExtended<EntityEnderShield> {
    public ModelEnderShield(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityEnderShield entityEnderShield) {
        if(entityEnderShield.getSkin() == 1) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/shield/endshield_2.png");
        }
        else if(entityEnderShield.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/shield/endshield_3.png");
        } else if(entityEnderShield.getSkin() == 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/shield/endshield_4.png");
        } else if(entityEnderShield.getSkin() == 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/shield/endshield_5.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/shield/endshield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEnderShield entityEnderShield) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.endshield.json");
    }

    @Override
    public void setLivingAnimations(EntityEnderShield entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJoint");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));


    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
