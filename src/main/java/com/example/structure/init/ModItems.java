package com.example.structure.init;

import com.example.structure.config.ItemConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.painting.EntityEEPainting;
import com.example.structure.items.*;
import com.example.structure.items.armor.ModArmorBase;
import com.example.structure.items.arrow.ItemArrowBase;
import com.example.structure.items.arrow.ItemUnholyArrow;
import com.example.structure.items.tools.*;
import com.example.structure.util.ModReference;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {



    private static final Item.ToolMaterial SWORD = EnumHelper.addToolMaterial("rare_sword", 2, 450, 8.0f, ItemConfig.sword_damage, 20);

    private static final Item.ToolMaterial DAGGER_MATERIAL = EnumHelper.addToolMaterial("dagger_materual", 5, 300, 7.0F, ItemConfig.dagger_damage, 25);
    private static final Item.ToolMaterial RED_SWORD = EnumHelper.addToolMaterial("unholy", 2, 974, 8.0f, ItemConfig.unholy_sword_damage, 20);

    private static final Item.ToolMaterial PURE_AXE_MATERIAL = EnumHelper.addToolMaterial("pure", 3, 625, 7.0F, ItemConfig.pure_axe_damage, 40);
    private static final Item.ToolMaterial END_FALL = EnumHelper.addToolMaterial("end_fall", 2, 1800, 8.0f, ItemConfig.endfall_sword_damage, 50);

    private static final Item.ToolMaterial ENDFALL_PICKAXE = EnumHelper.addToolMaterial("endfall_pickaxe", 6, 1781, 10.0F, 4, 25);

    private static final Item.ToolMaterial AMBER_SET = EnumHelper.addToolMaterial("amber_set", 3, 874, 7.0F, ItemConfig.cordium_sword_damage, 40);
    private static final ItemArmor.ArmorMaterial DARK_ARMOR = EnumHelper.addArmorMaterial("dark", ModReference.MOD_ID + ":dark", 385, new int[]{(int) (5 * ModConfig.dark_armor_scale), (int) (8 * ModConfig.dark_armor_scale), (int) (10 * ModConfig.dark_armor_scale), (int) (5 * ModConfig.dark_armor_scale)}, 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, (float) (5F * ModConfig.dark_armor_scale));

    private static final ItemArmor.ArmorMaterial AMBER_ARMOR = EnumHelper.addArmorMaterial("amber", ModReference.MOD_ID + ":amber", 450, new int[]{(int)(4 * ModConfig.cordium_armor_scale), (int)(7 * ModConfig.cordium_armor_scale), (int)(9 * ModConfig.cordium_armor_scale), (int)(4 * ModConfig.cordium_armor_scale)}, 40, SoundEvents.ITEM_ARMOR_EQUIP_IRON, (float)(4F * ModConfig.cordium_armor_scale));
    private static final ItemArmor.ArmorMaterial ENDFALL_ARMOR = EnumHelper.addArmorMaterial("endfall", ModReference.MOD_ID + ":endfallarmor", 650, new int[]{(int) (6 * ModConfig.fall_armor_scale), (int) (9 * ModConfig.fall_armor_scale), (int) (12 * ModConfig.fall_armor_scale), (int) (6 * ModConfig.fall_armor_scale)}, 50, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, (float)(6F * ModConfig.fall_armor_scale));

    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Guide Book
    public static final Item EE_BOOK = new ItemModBook("end_info_book", "guide", ModCreativeTabs.ITEMS);
    //Lamented Islands Items
    public static final Item END_KEY = new ItemKey("key", "dimensional_key", ModCreativeTabs.ITEMS, false);
    public static final Item END_ASH_KEY = new ItemKey("ash_key", "ash_key_desc", ModCreativeTabs.ITEMS, true);
    public static final Item LAMENTED_EYE = new ItemLamentedEye("lamented", "lamented_eye", ModCreativeTabs.ITEMS);
    public static final Item BOSS_SWORD = new ToolBossSword("sword", "sword_desc", SWORD);
    public static final Item PURPLE_CRYSTAL_ITEM = new ItemPurpleCrystal("purple_crystal_item", "purple_crystal", ModCreativeTabs.ITEMS);
    public static final Item PURPLE_CRYSTAL_CHUNK = new ItemBase("purple_chunk", ModCreativeTabs.ITEMS);
    //Ash Wastelands Items
    public static  final Item RED_CRYSTAL_ITEM = new ItemRedCrystal("red_crystal_item", "red_crystal",ModCreativeTabs.ITEMS);
    public static final Item RED_CRYSTAL_CHUNK = new ItemBase("red_chunk", ModCreativeTabs.ITEMS);
    public static final Item GREEN_CRYSTAL_ITEM = new ItemGreenCrystal("green_crystal_item", "green_crystal", ModCreativeTabs.ITEMS);
    public static final Item GREEN_CRYSTAL_CHUNK = new ItemBase("green_chunk", ModCreativeTabs.ITEMS);
    public static final Item INFUSED_CRYSTAL = new ItemInfusedCrystal("infused_crystal", "infuse", ModCreativeTabs.ITEMS);
    public static final Item INFUSION_CORE = new ItemInfusionCore("infusion_core", "core", ModCreativeTabs.ITEMS);
    public static final Item STALKER_HIDE = new ItemBase("stalker_hide", ModCreativeTabs.ITEMS);
    public static final Item PARASITE_CARAPACE = new ItemBase("carapace", ModCreativeTabs.ITEMS);
    public static final Item DARK_INGOT = new ItemBase("dark_ingot", ModCreativeTabs.ITEMS);
    public static final Item KNIGHT_SWORD = new ToolRedSword("red_sword", "unholy_sword_desc", RED_SWORD);
    public static final Item DARK_HELMET = new ModArmorBase("dark_helmet", DARK_ARMOR, 1, EntityEquipmentSlot.HEAD, "dark", "dark_desc");
    public static final Item DARK_CHESTPLATE = new ModArmorBase("dark_chestplate", DARK_ARMOR, 1, EntityEquipmentSlot.CHEST, "dark", "dark_desc");
    public static final Item ENDFALL_HELMET = new ModArmorBase("endfall_helmet", ENDFALL_ARMOR, 1, EntityEquipmentSlot.HEAD, "endfallarmor", "ef_desc");
    public static final Item ENDFALL_CHESTPLATE = new ModArmorBase("endfall_chestplate", ENDFALL_ARMOR, 1, EntityEquipmentSlot.CHEST, "endfallarmor", "ef_desc");
    public static final Item ENDFALL_LEGGINGS = new ModArmorBase("endfall_leggings", ENDFALL_ARMOR, 2, EntityEquipmentSlot.LEGS, "endfallarmor", "ef_desc");
    public static final Item ENDFALL_BOOTS = new ModArmorBase("endfall_boots", ENDFALL_ARMOR, 1, EntityEquipmentSlot.FEET, "endfallarmor", "ef_desc");

    public static final Item AMBER_HELMET = new ModArmorBase("amber_helmet", AMBER_ARMOR, 1, EntityEquipmentSlot.HEAD, "amber", "amber_desc");
    public static final Item AMBER_CHESTPLATE = new ModArmorBase("amber_chestplate", AMBER_ARMOR, 1, EntityEquipmentSlot.CHEST, "amber", "amber_desc");
    public static final Item AMBER_LEGGINGS = new ModArmorBase("amber_leggings", AMBER_ARMOR, 2, EntityEquipmentSlot.LEGS, "amber", "amber_desc");
    public static final Item AMBER_BOOTS = new ModArmorBase("amber_boots", AMBER_ARMOR, 1, EntityEquipmentSlot.FEET, "amber", "amber_desc");

    public static final Item MOD_PAINTING = new ItemPainting(EntityEEPainting.class, "lamented_islands_item", ModCreativeTabs.ITEMS, "painting_desc");

    //redone Amber Armor via Geckolib

    //public static final Item AMBER_REDONE_HELMET = new AmberArmorSet("amber_helmet", AMBER_ARMOR, 0, EntityEquipmentSlot.HEAD, "amber", "amber_desc");
    public static final Item AMBER_RAW_ORE = new ItemBase("amber_raw", ModCreativeTabs.ITEMS);
    public static final ItemAxe AMBER_AXE = new ItemAmberAxe("amber_axe", AMBER_SET, ItemConfig.cordium_axe_damage, -2.8F);
    public static final Item PURE_AXE = new ItemPureAxe("pure_axe", PURE_AXE_MATERIAL, ItemConfig.pure_axe_damage, -2.4F, "pure_axe_desc");
    public static final Item UNHOLY_AXE = new ItemRedAxe("red_axe", RED_SWORD, ItemConfig.cordium_axe_damage, -3.0F, "red_axe_desc");
    public static final Item AMBER_SWORD = new ToolAmberSword("amber_sword", AMBER_SET, "amber_sword_desc");
    public static final Item CHOMPER_TOOTH = new ItemBase("chomper_tooth", ModCreativeTabs.ITEMS);

    public static final Item BRICK_ROD = new ItemBase("brick_rod", ModCreativeTabs.ITEMS);
    public static final Item AMBER_INGOT = new ItemBase("amber_ingot", ModCreativeTabs.ITEMS);
    public static final Item BUG_FOOD = new ItemFoodBase("bug_food", 8, 2, true);
    public static final Item LOST_SOUL = new ItemBase("soul", ModCreativeTabs.ITEMS);
    public static final Item GUILDER_PLATE = new ItemBase("plate", ModCreativeTabs.ITEMS);
    public static final Item MEDALLION = new ItemMedallion("medal", "medal_desc", ModCreativeTabs.ITEMS);
    public static final Item DURABLE_SHIELD = new DurableShield("durable_shield", ModCreativeTabs.ITEMS, "shield_desc");
    public static final Item ENDFALL_SWORD = new ToolEndFallSword("endfall_sword", "efsword_desc", END_FALL);
    public static final Item ENDFALL_BOW = new ToolBowModded("end_bow", "bow_desc");
    public static final Item INFUSED_PICKAXE = new ToolPickaxe("endfall_pickaxe", ENDFALL_PICKAXE);
    public static final Item ENDFALL_STAFF = new ItemEndfallStaff("efstaff", ModCreativeTabs.ITEMS, "efstaff_desc");
    public static final Item PROJECTILE_PURPLE = new CrystalBallItem("projpurp", null);

    public static final Item OBSIDIAN_COIN = new ItemObiCoin("obi_coin", ModCreativeTabs.ITEMS, "coin_desc");
    public static final Item GUN_LAUNCHER = new ItemGunLauncher("gun", "gun_desc");
    public static final Item HEAL_FOOD = new ItemHealFood("heal_food", 2, 1, false, "hf_desc");

    public static final Item KING_CROWN = new ItemKingCrown("king_crown", ModCreativeTabs.ITEMS, "crown_desc");

    public static final Item AMBER_DAGGER = new ItemChomperDagger("amber_dagger", DAGGER_MATERIAL, "dagger_desc");
    public static final Item AMBER_PICKAXE = new ItemAmberPickaxe("amber_pickaxe", AMBER_SET);

    public static final Item ARENA_KEY_ONE = new ItemArenaKey("arena_key_1", ModCreativeTabs.ITEMS, "arena_key_desc");
    public static final Item ARENA_KEY_TWO = new ItemArenaKey("arena_key_2", ModCreativeTabs.ITEMS, "arena_key_desc");
    public static final Item ARENA_KEY_THREE = new ItemArenaKey("arena_key_3", ModCreativeTabs.ITEMS, "arena_key_desc");
    public static final Item ARENA_KEY_FOUR = new ItemArenaKey("arena_key_4", ModCreativeTabs.ITEMS, "arena_key_desc");
    public static final Item ARENA_KEY_FIVE = new ItemArenaKey("arena_key_5", ModCreativeTabs.ITEMS, "arena_key_desc");
    public static final Item BLOODWEED_REFINED = new ItemDescription("bloodweed_refined", ModCreativeTabs.ITEMS, "bloodweed_refined_desc");


    public static final Item UNHOLY_ARROW = new ItemUnholyArrow("mod_arrow_unholy");
    public static final Item CHOMPER_ARROW = new ItemArrowBase("mod_arrow_chomper");

    public static final Item BOMB_PROJECTILE = new ItemBase("bomb_proj", null);

    public static final Item ACID_PROJECTILE = new ItemBase("green_orb", null);


    //Misc.

   // public static Item ALTAR;

    public static final Item INVISIBLE = new ItemBase("invisible", null);
    public static final Item CRYSTAL_BALL = new CrystalBallItem("crystalball", null);

    public static final Item SPIN_SWORD_ITEM = new SpinSwordItem("spinsword", null);



    public ModItems() {


    }
}
