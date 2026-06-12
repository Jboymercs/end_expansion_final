package com.example.structure.config;

import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/compat_config")
public class CompatConfig {

    @Config.Name("Ashed Parasite Allowed Biome Spawns BE")
    @Config.Comment("Add spawns for where the Ashed Parasite can spawn if Better End is installed, these weights are decreased compared to default spawning in the Ash Wastelands")
    @Config.RequiresMcRestart
    public static String[] ashed_parasite_spawn_biomes = {
            "betterendforge:shadow_forest","betterendforge:dragon_graveyards","betterendforge:chorus_forest"
    };

    @Config.Name("End Stalker Allowed Biome Spawns BE")
    @Config.Comment("Add spawns for where the End Stalker can spawn if Better End is installed, these weights are decreased compared to default spawning in the Ash Wastelands")
    @Config.RequiresMcRestart
    public static String[] end_stalker_spawn_biomes = {
            "betterendforge:shadow_forest"
    };

    @Config.Name("Ancient Guilder Allowed Biome Spawns BE")
    @Config.Comment("Add spawns for where the Ancient Guilder can spawn if Better End is installed, these weights are decreased compared to default spawning in the regular End Biome")
    @Config.RequiresMcRestart
    public static String[] ancient_guilder_spawn_biomes = {
            "betterendforge:neon_oasis","betterendforge:dry_shrubland","betterendforge:lantern_woods"
    };

    @Config.Name("Lidoped Allowed Biome Spawns BE")
    @Config.Comment("Add spawns for where the Lidoped can spawn if Better End is installed, these weights are decreased compared to default spawning in the Barrend Bogs")
    @Config.RequiresMcRestart
    public static String[] lidoped_spawn_biomes = {
            "betterendforge:amber_land","betterendforge:chorus_forest","betterendforge:lantern_woods"
    };

    @Config.Name("Depths Chomper Allowed Biome Spawns BE")
    @Config.Comment("Add spawns for where the Depths Chomper can spawn if Better End is installed, these weights are decreased compared to default spawning in the Ash Wastelands. They will only spawn underground")
    @Config.RequiresMcRestart
    public static String[] depths_chomper_spawn_biomes = {
            "betterendforge:shadow_forest"
    };

    @Config.Name("Better End Support")
    @Config.Comment("If Better End Forge Backport is installed, End Expansion add mob spawns in Better End's Biomes. NOTE that the sky box from End Expansion will automatically be disabled with Better End installed")
    @Config.RequiresMcRestart
    public static boolean is_better_end_compat = true;


    @Config.Name("UDA Compat Support")
    @Config.Comment("If Unseens Dungeon Additions is loaded in, End Expansion will higher its difficulty to accommodate for the gear that UDA has to offer. Basically default stats will go up for everything, mobs, weapons, armor. default : true")
    @Config.RequiresMcRestart
    public static boolean is_uda_compat = true;
}
