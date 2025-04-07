package com.example.structure.config;

import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/structure_config")
public class WorldConfig {

    @Config.Name("End Biome Structure Spawning Enabled/Disabled")
    @Config.Comment("Change the value to enable or disable structure spawning in the regular End Biome, this includes plants, End Vaults, Lamented islands, and other ruins")
    @Config.RequiresMcRestart
    public static boolean does_structure_spawn = true;

    @Config.Name("Lamented Islands Structure Frequency")
    @Config.Comment("Change the spacing between Lamented Islands, lower means more frequent, higher means less, in Chunks")
    @Config.RequiresMcRestart
    public static int structureFrequency = 100;

    @Config.Name("Lamented Islands Blacklisted Biome Types")
    @Config.Comment("Add Biome types that DISALLOW the Lamented Islands from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] biome_types_lamented_islands = {"WASTELAND"
    };

    @Config.Name("Lamented Islands Mob Spawn Chance")
    @Config.Comment("Change the chance of mob spawns on the Lamented Islands")
    @Config.RangeInt(min = 1, max = 10)
    public static int structure_spawns = 8;

    @Config.Name("Lamented Islands Size")
    @Config.Comment("Changes the size of the Lamented Islands")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int islands_size = 3;

    @Config.Name("Lamented Islands Chest Spawn Chance")
    @Config.Comment("Change the chance of loot spawns for the Lamented Islands, higher number means lesser chance")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int lamentedIslandsLootChance = 2;

    @Config.Name("Lamented Islands Search Distance")
    @Config.Comment("Change the search distance of the locate command and the locator item for the Lamented Islands, in Chunks")
    @Config.RequiresMcRestart
    public static int lamented_islands_search_distance = 150;

    @Config.Name("Lamented Islands Enabled/Disabled")
    @Config.Comment("Change if the Lamented Islands should generate or not")
    @Config.RequiresMcRestart
    public static boolean lamented_islands_enabled = true;

    @Config.Name("Mobs Spawning at Middle End Islands")
    @Config.Comment("Change if mobs spawns at the middle island, aka the Ender Dragon Island")
    @Config.RequiresMcRestart
    public static boolean does_spawn_middle = false;

    @Config.Name("Stronghold Replacement")
    @Config.Comment("Replaces the vanilla Stronghold with my own")
    @Config.RequiresMcRestart
    public static boolean replace_stronghold = true;

    @Config.Name("Stronghold Mob Additions")
    @Config.Comment("Set to false if you don't wish for the beefed up zombie, skeleton, and cave spider spawners to be in the Better Stronghold")
    @Config.RequiresMcRestart
    public static boolean mob_additions_stronghold = true;

    @Config.Name("Stronghold Size")
    @Config.Comment("Change the size of the Stronghold in tiles, minimum of 2 and maximum of 12")
    @Config.RangeInt(min = 2, max = 12)
    @Config.RequiresMcRestart
    public static int stronghold_size = 5;

    @Config.Name("Ashed King's Fortress Chest Spawn Chance")
    @Config.Comment("Chance for chests to spawn throughout the dungeon, Lower value is higher chances")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int dungeon_chest_chance = 3;

    @Config.Name("Ashed King's Fortress Mob Spawn Chance")
    @Config.Comment("Chance for mobs to spawn throughout the dungeon, Lower value is higher chances")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int dungeon_mob_chance = 5;

    @Config.Name("Ashed King Fortress")
    @Config.Comment("Change the spacing of the End King Fortress in chunks away from another, this is applied in chunks that are Ash Wastelands not every chunk")
    @Config.RequiresMcRestart
    public static int fortress_spacing = 70;

    @Config.Name("Ashed King Fortress Odds")
    @Config.Comment("Change the odds of this structure spawning, take in mind the fortress has few structure to compete with for odds of spawning")
    @Config.RequiresMcRestart
    public static int fortress_odds = 1;

    @Config.Name("Ashed King Fortress Size")
    @Config.Comment("Change the size of the fortress that generates")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int fortress_size = 5;


    @Config.Name("End Vaults Loot Chance")
    @Config.Comment("Change the chance of loot in the End Vaults, Higher is better chance")
    @Config.RangeInt(min = 0, max = 5)
    public static int vault_loot_chance = 2;

    @Config.Name("End Vaults Mob Chance")
    @Config.Comment("Change the chance of mob spawns in the End Vaults, Higher is better chance")
    @Config.RangeInt(min = 0, max = 10)
    public static int vault_mob_chance = 7;


    @Config.Name("End Vault Size")
    @Config.Comment("Change the size of the End Vaults, any bigger may result in buggy appliances")
    @Config.RequiresMcRestart
    public static int vault_size = 4;

    @Config.Name("End Vault Chance to Spawn")
    @Config.Comment("Change the chance to spawn the End Vaults, WARNING putting it low will result in buggy and or over spawning, as this is not determined in chunks apart, it's by each time the surface is above 55")
    @Config.RequiresMcRestart
    public static int vault_distance = 125;

    @Config.Name("Avalon Trader Spacing")
    @Config.Comment("Change the spacing of the Avalon Trader, lower is more frequent, higher is less")
    @Config.RequiresMcRestart
    public static int avalon_trader_spacing = 400;


    @Config.Name("Ashed Mines Chance to Spawn")
    @Config.Comment("Change the chance of the Ashed Mines, these are the mineshafts that run throughout the Ashed Wastelands, modify Carefully")
    @Config.RequiresMcRestart
    public static int ashed_mines_distance = 50;

    @Config.Name("Ashed Mines Size")
    @Config.Comment("The size of the Ashed Mines")
    @Config.RequiresMcRestart
    public static int ashed_mines_size = 7;

    @Config.Name("Ashed Mines Mob Spawns")
    @Config.Comment("Change the Mob Spawn Rate in the Ashed Mines, spawns include Ender Knights/Ender Shielders")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int ashed_mines_mob_spawns = 3;

    @Config.Name("Ashed Mines Chest Spawns")
    @Config.Comment("Change the Chest Spawns in the Ashed Mines")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int ashed_mines_chest_spawns = 3;

    @Config.Name("Caves Disable")
    @Config.Comment("Disable End Expansions Caves that spawn in the Ash Wastelands, NOTE caves will still spawn there just won't be any small scenic structures/ruins/large caves, set to true to disable caves")
    @Config.RequiresMcRestart
    public static boolean disable_large_caves = false;

    @Config.Name("Caves set Spawns")
    @Config.Comment("This setting is allowing set spawns too occur in the caves, think one time spawners to make immediate entrace of caves more dangerous, what spawns are End Stalkers and Depths Chompers, set to 0 to disable")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int cave_spawn_rate = 6;

    @Config.Name("Caves Chest Spawns")
    @Config.Comment("Change the chance of Chest spawns that occur in small ruins and caves")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int cave_chest_chance = 2;

    @Config.Name("Ashed Towers Chance to Spawn")
    @Config.Comment("Change the chance to spawn the Ashed Towers, This is done by per chunk checking if the ground is atleast 58, modify carefully")
    @Config.RequiresMcRestart
    public static int ash_tower_distance = 160;

    @Config.Name("Ashed Towers Mob Chance")
    @Config.Comment("Change the chance of mob spawns in the Ashed Towers, Higher is better chance")
    @Config.RangeInt(min = 0, max = 10)
    public static int ashed_towers_mob_spawn = 7;

    @Config.Name("Ashed Towers Chest Chance")
    @Config.Comment("Change the chance of Chest Spawns in the Ashed Towers")
    @Config.RangeInt(min = 0, max = 5)
    public static int ashed_tower_chest_spawn = 3;

    @Config.Name("Barrend Arena Chance to Spawn")
    @Config.Comment("Change the chance to spawn the Barrend Arena, This is done by per chunk checking if the ground is atleast 58, modify carefully")
    @Config.RequiresMcRestart
    public static int bare_arena_spacing = 150;

    @Config.Name("Barrend Bogs Arches")
    @Config.Comment("Change the chance to spawn Barrend Bog Arches, these are the arches going above the fog.")
    @Config.RequiresMcRestart
    public static int bare_arches_spacing = 60;

    @Config.Name("Barrend Crypts Chance to Spawn")
    @Config.Comment("Change the chance to spawn Barrend Crypts (Only works if dev stuff is activated in general_config)")
    @Config.RequiresMcRestart
    public static int bare_crypts_spacing = 75;

    @Config.Name("Barrend Crypts Chest Spawn Chance")
    @Config.Comment("Change the spawn chance of chests in the Barrend Crypts (Only works if dev stuff is activated in general_config")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 5)
    public static int bare_crypt_chest_chance = 3;

    @Config.Name("Barrend Crypts Pot Spawn Chance")
    @Config.Comment("Change the spawn chance of pots in the Barrend Crypts (Only works if dev stuff is activated in general_config")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 5)
    public static int bare_crypt_pot_chance = 4;

    @Config.Name("Barrend Crypts Mob Spawn Chance")
    @Config.Comment("Change the spawn chance of mobs in the Barrend Crypts (Only works if dev stuff is activated in general_config")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 10)
    public static int bare_crypt_mob_chance = 6;

}
