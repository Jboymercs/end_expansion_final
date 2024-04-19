package com.example.structure.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
    public static void init() {
        GameRegistry.addSmelting(ModItems.AMBER_RAW_ORE, new ItemStack(ModItems.AMBER_INGOT), 3);
    }
}
