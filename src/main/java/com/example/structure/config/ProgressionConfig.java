package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/progression_config")
public class ProgressionConfig {

    @Config.Name("Barrend Arena Unlock Stage Requirements")
    @Config.Comment("What advancements are required for players to use and unlock the Barrend Arena entries")
    @Config.RequiresMcRestart
    public static String[] arena_progress_stages = {
            "ee:kill_seeker_prime"
    };
    @Config.Name("Lamentor Unlock Stage Requirements")
    @Config.Comment("What advancements are required to unlock Ashed Kings Fortress, and several Guidebook entries. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] lamentor_progress_stages = {
            "ee:kill_lamentor",
            "ee:find_purple"
    };

    @Config.Name("Barrend Arena Locked Message")
    @Config.Comment("Change the message that is displayed for a player if they do not meet requirements for the Barrend Arena")
    @Config.RequiresMcRestart
    public static String arena_locked_message = "You have not defeated an Elder End Seeker yet";

    @Config.Name("Ashed King Locked Message")
    @Config.Comment("Change the message that is displayed for a player if they do not meet requirements for the Ashed King's summoning block")
    @Config.RequiresMcRestart
    public static String ashed_king_locked_message = "You have not defeated the Lamentor yet";

    @Config.Name("Barrend Crypt Locked Message")
    @Config.Comment("Change the message that is displayed for a player if they do not meet requirements for the Barrend Crypt door block")
    @Config.RequiresMcRestart
    public static String barrend_crypt_locked_message = "You have not defeated the Ashed King yet";
    @Config.Name("Lamentor Locked Message")
    @Config.Comment("Change the message that is displayed for a player if they do not meet requirements for the Lamentors summoning block, for Modpack creators to add custom requirements for the Lamentor")
    @Config.RequiresMcRestart
    public static String lamentor_locked_message = "You have not entered an End Gateway yet";

    @Config.Name("Lamentor Key Block Stage Requirements")
    @Config.Comment("What advancements are required to unlock the Lamentors Key Block, this is for modpack creators who wish to lock. By default you just need to enter an End Gateway")
    @Config.RequiresMcRestart
    public static String[] lamentor_key_progress_stages = {
            "minecraft:end/enter_end_gateway"
    };

    @Config.Name("Cordium Unlock Stage Requirements")
    @Config.Comment("What advancements are required to unlock Cordium Tools and Armor in the Guidebook. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] cordium_progress_stages = {
            "ee:kill_lamentor",
            "ee:find_purple",
            "ee:find_cordium"
    };

    @Config.Name("Infused Crystals Stage Requirements")
    @Config.Comment("What advancements are required to unlock Infused Crystal Guidebook Entries. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] infused_crystal_progress_stage = {
            "ee:kill_lamentor",
            "ee:find_purple",
            "ee:find_red",
            "ee:find_core"
    };

    @Config.Name("Dark Armor Unlock Stage Requirements")
    @Config.Comment("What advancements are required to unlock Dark Armor Entries in the Guidebook. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] dark_armor_progress_stage = {
            "ee:kill_lamentor",
            "ee:kill_knights",
            "ee:kill_end_bug",
            "ee:kill_stalker"
    };

    @Config.Name("EndFall Armor Unlock Stage Requirements")
    @Config.Comment("What advancements are required to unlock Endfall entries, Barrend Crypt, and other Barrend Entries. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] end_fall_progress_stage = {
            "ee:kill_lamentor",
            "ee:kill_king",
            "ee:find_soul",
    };

    @Config.Name("Memorium Stone Unlock Stage Requirements")
    @Config.Comment("What advancements are required to unlock Barrend Crypts, and other Barrend Entries. ModId : advancementName")
    @Config.RequiresMcRestart
    public static String[] barrend_crypt_progress_stage = {
            "ee:kill_lamentor",
            "ee:kill_king",
            "ee:find_soul",
            "ee:craft_memory_stone"
    };
}
