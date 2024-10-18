package com.example.structure.entity.model;

import com.example.structure.entity.barrend.EntityUltraParasite;
import com.example.structure.entity.model.geo.GeoModelExtended;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelUltraParasite extends GeoModelExtended<EntityUltraParasite> {


    public ModelUltraParasite(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityUltraParasite animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ultra_parasite.json");
    }

    @Override
    public void setLivingAnimations(EntityUltraParasite entity, Integer uniqueID, AnimationEvent customPredicate) {

        if(!entity.isPhaseTransition()) {
            IBone head = this.getAnimationProcessor().getBone("HeadJ");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
