package com.jboymercs.endexpansion.items.tools;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.item.ItemPickaxe;

public class ToolPickaxe extends ItemPickaxe implements IHasModel {
    public ToolPickaxe(String name, ToolMaterial p_i45347_1_) {
        super(p_i45347_1_);
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
