package com.example.structure.items.gecko;

import net.minecraft.inventory.EntityEquipmentSlot;
import software.bernie.geckolib3.core.IAnimatable;

public class AmberArmorSet extends GeckoArmorBase implements IAnimatable {


    public AmberArmorSet(String name, ArmorMaterial materialIn, int renderIdx, EntityEquipmentSlot slotIn, String textureName, String info_loc) {
        super(name, materialIn, renderIdx, slotIn, textureName, info_loc);
    }
}
