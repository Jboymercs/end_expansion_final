package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEEMusicDisc extends ItemRecord implements IHasModel {



    public ItemEEMusicDisc(String name, SoundEvent soundEventIn) {
        super(name, soundEventIn);
        this.setRegistryName(name);
        this.setTranslationKey(name);
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
