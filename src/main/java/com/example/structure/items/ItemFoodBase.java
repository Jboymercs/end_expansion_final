package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.item.ItemFood;

public class ItemFoodBase extends ItemFood implements IHasModel {
    public ItemFoodBase(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setCreativeTab(ModCreativeTabs.ITEMS);
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
