package com.example.structure.init;

import com.example.structure.tab.EndExpansionCreativeTab;
import net.minecraft.creativetab.CreativeTabs;

public class ModCreativeTabs {
    public static CreativeTabs ITEMS = new EndExpansionCreativeTab(CreativeTabs.getNextID(), "expansion", () -> ModItems.LAMENTED_EYE);
}
