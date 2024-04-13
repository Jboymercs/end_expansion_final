package com.example.structure.entity.model;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEndKing extends GeoModelExtended<EntityEndKing> {
    public ModelEndKing(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEndKing entityEndKing) {
        if(entityEndKing.isPhaseMode()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/king_1.png");
        }
     else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/king.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEndKing entityEndKing) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.king.json");
    }

    @Override
    public void setLivingAnimations(EntityEndKing entity, Integer uniqueID, AnimationEvent customPredicate) {
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
