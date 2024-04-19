package com.example.structure.items.tools;

import com.example.structure.Main;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.item.ItemAxe;

public class ItemAmberAxe extends ItemAxe implements IHasModel {

    public ItemAmberAxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }



    public ItemAmberAxe(String name, ToolMaterial material, float damage, float speed) {
        super(material, damage, speed);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
