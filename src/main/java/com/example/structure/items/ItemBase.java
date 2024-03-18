package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name, CreativeTabs tab) {
        setTranslationKey(name);
        setRegistryName(name);
        if (tab != null) {
            setCreativeTab(tab);
        }

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name) {
        this(name, CreativeTabs.MISC);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
