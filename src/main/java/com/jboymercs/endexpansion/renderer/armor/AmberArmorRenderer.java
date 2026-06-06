package com.jboymercs.endexpansion.renderer.armor;

import com.jboymercs.endexpansion.items.gecko.AmberArmorSet;
import com.jboymercs.endexpansion.model.ModelAmberArmorSet;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class AmberArmorRenderer extends GeoArmorRenderer<AmberArmorSet> {


    public AmberArmorRenderer() {
        super(new ModelAmberArmorSet());


        // These values are what each bone name is in blockbench. So if your head bone
        // is named "bone545", make sure to do this.headBone = "bone545";
        // The default values are the ones that come with the default armor template in
        // the geckolib blockbench plugin.
        this.headBone = "bipedHead";
        this.bodyBone = "bipedBody";
        this.rightArmBone = "bipedRightArm";
        this.leftArmBone = "bipedLeftArm";
        this.rightLegBone = "bipedRightLeg";
        this.leftLegBone = "bipedLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
