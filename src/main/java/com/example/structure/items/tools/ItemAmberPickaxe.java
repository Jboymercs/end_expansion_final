package com.example.structure.items.tools;

import com.example.structure.Main;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.item.ItemPickaxe;

public class ItemAmberPickaxe extends ItemPickaxe implements IHasModel {
    public ItemAmberPickaxe(String name, ToolMaterial material) {
        super(material);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);

    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
