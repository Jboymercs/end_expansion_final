package com.example.structure.items.tools;

import com.example.structure.Main;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.item.ItemPickaxe;

public class ToolPickaxe extends ItemPickaxe implements IHasModel {
    public ToolPickaxe(String name, ToolMaterial p_i45347_1_) {
        super(p_i45347_1_);
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
