package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/general_config")
public class ModConfig {
    @Config.Name("Items abilities affect other players")
    @Config.Comment("This setting when enabled makes any of the weapons abilities affect players, Example: the Cordium sword will summon spikes on nearby players except the owner with it's ability")
    @Config.RequiresMcRestart
    public static boolean enable_pvp_abilities = false;

    @Config.Name("Ash Fog Disable/Enable")
    @Config.Comment("When set to true, it will disable the dark fog found in the Ash Wastelands")
    @Config.RequiresMcRestart
    public static boolean isDarkFogDisabled = false;

    @Config.Name("End Sky Box")
    @Config.Comment("When set to false, the sky box inside the End will be disabled")
    @Config.RequiresMcRestart
    public static boolean isSkyBoxEnalbed = true;

    @Config.Name("Ash Fog Variable")
    @Config.Comment("Change the variable of the fog, this basically changes the distance for how far the fog goes. If you wish to disable the fog, the option above does that")
    @Config.RequiresMcRestart
    public static float dark_fog_variable = 0.085F;

    @Config.Name("Ash Biome Difficulty")
    @Config.Comment("A Universal config option that multiplies all base health, attack damage, armor, and abilities by this value to all Ashed Wastelands Entities (Parasites, Stalkers, Knight Household, Ashed King")
    @Config.RequiresMcRestart
    public static double biome_multiplier = 1;

    @Config.Name("Ash Wastelands Biome Weight")
    @Config.Comment("Change the weight of the Ash Wastelands Biome spawn")
    @Config.RequiresMcRestart
    public static int biome_weight = 80;

    @Config.Name("Lamented End Difficulty")
    @Config.Comment("A Universal config option that multiplies all base health, attack damage, armor, and abilities for all mobs pertaining to the base End Biome, Lamentor, Constructor, Guilder, Seeker")
    @Config.RequiresMcRestart
    public static double lamented_multiplier = 1;

    @Config.Name("End Ash Particle Turn off")
    @Config.Comment("Change to turn on or off the particles from the End Ash, use this to improve performance")
    @Config.RequiresMcRestart
    public static boolean disable_end_ash_particles = false;

    @Config.Name("Red Rage Potion Effect Damage")
    @Config.Comment("Damage the Red Rage deals to entities near you when affected")
    @Config.RequiresMcRestart
    public static float potion_damage = 17;

    @Config.Name("Nuke Base Damage")
    @Config.Comment("If caught inside the nuke, it will deal X damage while in it. Used by the End King")
    @Config.RequiresMcRestart
    public static float nuke_damage = 200;

    @Config.Name("Mod Scaling Factor")
    @Config.Comment("When more than 1 players are near, this factor will add on how much Health all bosses/mini-bosses has. Default Per more than one player is 50% more HP or 0.5. Example: There is " +
            "2 Players around a boss that has a health of 200 by default. With there being a second player the bosses health is now 300")
    @Config.RequiresMcRestart
    public static double scale_mod_bosses = 0.4;

    @Config.Name("Mod Scaling Attack Damage Factor")
    @Config.Comment("When more than 1 players are near, this factor will be multiplied by how many players are near and then by this bosses attack damage")
    @Config.RequiresMcRestart
    public static double scale_attack_damge = 0.18;

    @Config.Name("Disable Mod Scaling")
    @Config.Comment("Disable scaling for Health of bosses when more than one players is nearby, set to true to disable")
    @Config.RequiresMcRestart
    public static boolean disable_scaling_mod = false;


    //Armor
    @Config.Name("Scale Cordium Armor set")
    @Config.Comment("Scale the Cordium Armor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double cordium_armor_scale = 1.0;

    @Config.Name("Scale Dark Amor Set")
    @Config.Comment("Scale the Dark Amor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double dark_armor_scale = 1.0;

    @Config.Name("Scale End Fall Armor Set")
    @Config.Comment("Scale the End Fall Armor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double fall_armor_scale = 1.0;

}
