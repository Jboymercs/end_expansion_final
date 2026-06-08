package com.jboymercs.endexpansion.items.tools;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.item.ItemAxe;

public class ItemAmberAxe extends ItemAxe implements IHasModel {

    public ItemAmberAxe(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(ModReference.MOD_ID + "." + name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }



    public ItemAmberAxe(String name, ToolMaterial material, float damage, float speed) {
        super(material, damage, speed);
        setTranslationKey(ModReference.MOD_ID + "." + name);
        setRegistryName(name);
        setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
