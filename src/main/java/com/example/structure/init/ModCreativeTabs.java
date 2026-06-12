package com.example.structure.init;

import com.example.structure.tab.EndExpansionCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModCreativeTabs {
    public static CreativeTabs ITEMS = new EndExpansionCreativeTab(CreativeTabs.getNextID(), "expansion_items", () -> ModItems.LAMENTED_EYE);
    public static CreativeTabs BLOCKS = new EndExpansionCreativeTab(CreativeTabs.getNextID(), "expansion_blocks", () -> Item.getItemFromBlock(ModBlocks.STAR_SHARD_ORE));
}
