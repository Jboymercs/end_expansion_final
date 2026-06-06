package com.jboymercs.endexpansion.items.tools;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.item.ItemPickaxe;

public class ItemAmberPickaxe extends ItemPickaxe implements IHasModel {
    public ItemAmberPickaxe(String name, ToolMaterial material) {
        super(material);
        this.setTranslationKey(ModReference.MOD_ID + "." + name);
        this.setRegistryName(name);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);

    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
