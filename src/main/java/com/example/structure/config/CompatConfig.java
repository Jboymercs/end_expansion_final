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
}
