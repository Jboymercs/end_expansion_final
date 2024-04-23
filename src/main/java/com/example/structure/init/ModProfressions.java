package com.example.structure.init;


import com.example.structure.util.ModReference;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Random;

/**
 * Based on Jabelar's villager profession tutorial https://jabelarminecraft.blogspot.com/p/minecraft-forge-modding-villagers.html
 */

@ObjectHolder(ModReference.MOD_ID)
public class ModProfressions {

    private static VillagerRegistry.VillagerProfession AVALON_NULL_PROFESSION = null;

    public static VillagerRegistry.VillagerCareer AVALON_TRADER;

    public static void associateCareersAndTrades() {
        //Input Textures later
        AVALON_NULL_PROFESSION = new VillagerRegistry.VillagerProfession("avalon", ModReference.MOD_ID + "", ModReference.MOD_ID + "");

        AVALON_TRADER = new VillagerRegistry.VillagerCareer(AVALON_NULL_PROFESSION, "avalon_trader");

        ItemStack sharpness3Book = new ItemStack(Items.ENCHANTED_BOOK);
        ItemStack mendingBook = new ItemStack(Items.ENCHANTED_BOOK);
        ItemStack protectionBook = new ItemStack(Items.ENCHANTED_BOOK);
        ItemStack featherFallingBook = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantedBook.addEnchantment(sharpness3Book, new EnchantmentData(Enchantments.SHARPNESS, 3));
        ItemEnchantedBook.addEnchantment(mendingBook, new EnchantmentData(Enchantments.MENDING, 1));
        ItemEnchantedBook.addEnchantment(protectionBook, new EnchantmentData(Enchantments.PROTECTION, 3));
        ItemEnchantedBook.addEnchantment(featherFallingBook, new EnchantmentData(Enchantments.FEATHER_FALLING, 3));
        //Level 1 Trades
        AVALON_TRADER.addTrade(1, new GeneralTrade(ModItems.RED_CRYSTAL_CHUNK, 4, ModItems.CHOMPER_ARROW, 4, ModItems.UNHOLY_ARROW, 4));
        AVALON_TRADER.addTrade(1, new StackTrade(new ItemStack(ModItems.INFUSED_CRYSTAL, 6), protectionBook));
        AVALON_TRADER.addTrade(1, new GeneralTrade(ModItems.INFUSED_CRYSTAL, 1, null, 0, ModItems.HEAL_FOOD, 2));
        //Level 2 Trades
        AVALON_TRADER.addTrade(1, new GeneralTrade(ModItems.AMBER_DAGGER, 1, ModItems.CHOMPER_TOOTH, 7, ModItems.AMBER_SWORD, 1 ));
        AVALON_TRADER.addTrade(1, new StackTrade(new ItemStack(ModItems.INFUSED_CRYSTAL, 8), featherFallingBook));
        AVALON_TRADER.addTrade(1, new GeneralTrade(ModItems.INFUSED_CRYSTAL, 5, ModItems.STALKER_HIDE, 5, Items.ELYTRA, 1));
        //Level 3 Trades
        AVALON_TRADER.addTrade(1, new GeneralTrade(ModItems.INFUSED_CRYSTAL, 4, null, 0, ModItems.UNHOLY_AXE, 1));
        AVALON_TRADER.addTrade(1, new StackTrade(new ItemStack(ModItems.INFUSED_CRYSTAL, 9), sharpness3Book));
        AVALON_TRADER.addTrade(1, new StackTrade(new ItemStack(ModItems.INFUSED_CRYSTAL, 10), mendingBook));

    }


    public static class GeneralTrade implements EntityVillager.ITradeList {
        private final ItemStack base;
        private final ItemStack cost;
        private final ItemStack reward;

        public GeneralTrade(Item cost, int amount, Item cost2, int amount2, Item reward, int amount3) {
            base = new ItemStack(cost, amount);
            if (cost2 != null) {
                this.cost = new ItemStack(cost2, amount2);
            } else {
                this.cost = null;
            }
            this.reward = new ItemStack(reward, amount3);
        }

        public GeneralTrade(ItemStack stack1, ItemStack stack2, ItemStack stack3) {
            base = stack1;
            this.cost = stack2;
            this.reward = stack3;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            if (cost != null) {
                recipeList.add(new MerchantRecipe(base, cost, reward));
            } else {
                recipeList.add(new MerchantRecipe(base, reward));
            }
        }
    }

    public static class StackTrade implements EntityVillager.ITradeList {
        private final ItemStack base;
        private final ItemStack cost;
        private final ItemStack reward;

        public StackTrade(ItemStack base, ItemStack cost, ItemStack reward) {
            this.base = base;
            this.cost = cost;
            this.reward = reward;
        }

        public StackTrade(ItemStack base, ItemStack reward) {
            this(base, null, reward);
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            if (cost != null) {
                recipeList.add(new MerchantRecipe(base, cost, reward));
            } else {
                recipeList.add(new MerchantRecipe(base, reward));
            }
        }
    }
}
