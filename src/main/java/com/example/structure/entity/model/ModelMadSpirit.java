package com.example.structure.entity.model;

import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.barrend.EntityMadSpirit;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelMadSpirit extends GeoModelExtended<EntityMadSpirit> {
    public ModelMadSpirit(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getModelLocation(EntityMadSpirit object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/madspirit/geo.madspirit.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMadSpirit object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/madspirit/madspirit.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMadSpirit animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.mad_spirit.json");
    }

    @Override
    public void setLivingAnimations(EntityMadSpirit entity, Integer uniqueID, AnimationEvent customPredicate) {
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
