package com.example.structure.entity.model;


import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEnderMage extends GeoModelExtended<EntityEnderMage> {
    public ModelEnderMage(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }


    @Override
    public ResourceLocation getTextureLocation(EntityEnderMage entityEnderMage) {
        if(entityEnderMage.getSkin() == 1) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/mage/endmage_2.png");
        }
        else if(entityEnderMage.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/mage/endmage_3.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/mage/endmage.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEnderMage entityEnderMage) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.endmage.json");
    }

    @Override
    public void setLivingAnimations(EntityEnderMage entity, Integer uniqueID, AnimationEvent customPredicate) {
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
