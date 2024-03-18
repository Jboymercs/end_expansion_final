package com.example.structure.init;

import com.example.structure.config.ModConfig;
import com.example.structure.items.*;
import com.example.structure.items.armor.ModArmorBase;
import com.example.structure.items.tools.*;
import com.example.structure.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {



    private static final Item.ToolMaterial SWORD = EnumHelper.addToolMaterial("rare_sword", 2, 100, 8.0f, ModConfig.sword_damage, 20);
    private static final Item.ToolMaterial RED_SWORD = EnumHelper.addToolMaterial("unholy", 2, 800, 8.0f, 6.0F, 30);

    private static final Item.ToolMaterial END_FALL = EnumHelper.addToolMaterial("end_fall", 2, 600, 8.0f, ModConfig.endfall_sword_damage, 40);

    private static final Item.ToolMaterial ENDFALL_PICKAXE = EnumHelper.addToolMaterial("endfall_pickaxe", 5, 1000, 100, 4, 25);

    private static final ItemArmor.ArmorMaterial DARK_ARMOR = EnumHelper.addArmorMaterial("dark", ModReference.MOD_ID + ":dark", 800, new int[]{3, 6, 8, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);

    private static final ItemArmor.ArmorMaterial ENDFALL_ARMOR = EnumHelper.addArmorMaterial("endfall", ModReference.MOD_ID + ":endfallarmor", 800, new int[]{4, 7, 9, 4}, 80, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 6);

    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Guide Book
    public static final Item EE_BOOK = new ItemModBook("info_book", "guide", ModCreativeTabs.ITEMS);
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
    public static final Item BUG_FOOD = new ItemFoodBase("bug_food", 8, 2, true);
    public static final Item LOST_SOUL = new ItemBase("soul", ModCreativeTabs.ITEMS);
    public static final Item GUILDER_PLATE = new ItemBase("plate", ModCreativeTabs.ITEMS);
    public static final Item MEDALLION = new ItemMedallion("medal", "medal_desc", ModCreativeTabs.ITEMS);
    public static final Item DURABLE_SHIELD = new DurableShield("durable_shield", ModCreativeTabs.ITEMS, "shield_desc");
    public static final Item ENDFALL_SWORD = new ToolEndFallSword("endfall_sword", "efsword_desc", END_FALL);
    public static final Item ENDFALL_BOW = new ToolBowModded("bow", "bow_desc");
    public static final Item INFUSED_PICKAXE = new ToolPickaxe("endfall_pickaxe", ENDFALL_PICKAXE);
    public static final Item ENDFALL_STAFF = new ItemEndfallStaff("efstaff", ModCreativeTabs.ITEMS, "efstaff_desc");
    public static final Item PROJECTILE_PURPLE = new CrystalBallItem("projpurp", null);


    //Misc.

   // public static Item ALTAR;

    public static final Item INVISIBLE = new ItemBase("invisible", null);
    public static final Item CRYSTAL_BALL = new CrystalBallItem("crystalball", null);

    public static final Item SPIN_SWORD_ITEM = new SpinSwordItem("spinsword", null);



    public ModItems() {


    }
}
