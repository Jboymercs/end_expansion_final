package com.jboymercs.endexpansion.items;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemEEMusicDisc extends ItemRecord implements IHasModel {



    public ItemEEMusicDisc(String name, SoundEvent soundEventIn) {
        super(name, soundEventIn);
        this.setRegistryName(name);
        this.setTranslationKey(ModReference.MOD_ID + "." + name);
        ModItems.ITEMS.add(this);

    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
