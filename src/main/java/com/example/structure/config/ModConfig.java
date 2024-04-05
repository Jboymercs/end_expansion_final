package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = ModReference.NAME)
public class ModConfig {
    //77 Configurable Items
    @Config.Name("Lamented Islands Structure Frequency")
    @Config.Comment("Raises and Lowers Frequency of Structure Spawns, Higher means more frequent")
    @Config.RangeInt(min = 0, max = 48)
    @Config.RequiresMcRestart
    public static int structureFrequency = 25;

    @Config.Name("Lamentor Boss Health")
    @Config.Comment("Change the Health of the Lamentor")
    @Config.RequiresMcRestart
    public static float health = 300f;

    @Config.Name("Lamentor Attack Speed")
    @Config.Comment("Change the speed at which the Lamentor attacks in seconds, smaller number means quicker, larger number means slower, warning 0 might be buggy")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int boss_speed = 1;

    @Config.Name("Lamentor Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lamentor")
    @Config.RequiresMcRestart
    public static double attack_damage= 9D;

    @Config.Name("Lamentor Ranged Crystal Damage")
    @Config.Comment("Change the damage of the flying Crystals")
    @Config.RequiresMcRestart
    public static float crystal_damage= 4.0f;

    @Config.Name("Lamentor Pierce Damage")
    @Config.Comment("Change the multiplier of the Lamentor's Pierce attacks, base damage * pierce multiplier")
    @Config.RequiresMcRestart
    public static float pierce_multiplier = 1.8f;

    @Config.Name("Lamentor Circle Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentor's Circle Attack, base damage * circle attack multiplier")
    @Config.RequiresMcRestart
    public static float circle_multiplier = 2.0f;

    @Config.Name("Lamentor Hammer Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentors Hammer Attack, base damage * hammer attack multiplier")
    @Config.RequiresMcRestart
    public static float hammer_multiplier = 1.4f;

    @Config.Name("Lamentor Explosion Size")
    @Config.Comment("Change the size of explosion for the Lamentors Hammer Attack")
    @Config.RangeInt(min = 0, max = 3)
    @Config.RequiresMcRestart
    public static float explosion_size = 2;

    @Config.Name("Ground Crystal Damage")
    @Config.Comment("Change the damage done by Ground Crystals")
    @Config.RequiresMcRestart
    public static float ground_crystal_damage= 4f;

    @Config.Name("Constructor Health")
    @Config.Comment("Change the Health of the Constructor")
    @Config.RequiresMcRestart
    public static float constructor_health = 40f;

    @Config.Name("Constructor Attack Speed Value 1")
    @Config.Comment("Constructor Speed #1, this value in seconds, smaller means quicker, larger means slower speed of attacks")
    @Config.RequiresMcRestart
    public static int constructor_speed_one = 2;

    @Config.Name("Constructor Attack Speed Value 2")
    @Config.Comment("Constructor Speed #2, this value in seconds, smaller means quicker, larger means slower speed of attacks. This second value is used for how long after a ranged attack")
    @Config.RequiresMcRestart
    public static int constructor_speed_two = 10;

    @Config.Name("Constructor ShockWave Damage")
    @Config.Comment("damage dealt by the Constructor when doing it's ground shock wave attack as a mutliplier, base damage * shockwave damage")
    @Config.RequiresMcRestart
    public static float constructor_shockwave_damage= 0.5f;

    @Config.Name("Constructor Structure Spawn Chance")
    @Config.Comment("Change the chance of how many Constructors will spawn on the Lamented Islands dungeon")
    @Config.RangeInt(min = 1, max = 10)
    public static int structure_spawns = 5;

    @Config.Name("Constructor Natural Spawn Weight")
    @Config.Comment("Change the weights at which the Constructors naturally spawn in the End, or disable it entirely, lower value means less spawns, higher means more common")
    @Config.RequiresMcRestart
    public static int constructor_weights = 1;

    @Config.Name("End Biome Structure Spawning Enabled/Disabled")
    @Config.Comment("Change the value to enable or disable structure spawning in the regular End Biome, this includes plants, End Vaults, Lamented islands, and other ruins")
    @Config.RequiresMcRestart
    public static boolean does_structure_spawn = true;

    @Config.Name("Lamented Sword Damage")
    @Config.Comment("Change the damage done by the Lamented Sword")
    @Config.RequiresMcRestart
    public static float sword_damage = 7.0f;

    @Config.Name("Lamented Sword Dash Damage")
    @Config.Comment("Change the damage done by the dash ability upon initially dashing to nearby entities")
    @Config.RequiresMcRestart
    public static float sword_dash_damage = 4.0f;

    @Config.Name("Lamented Sword Cooldown")
    @Config.Comment("Change the cooldown time for using the dash ability on the Lamented Sword, in seconds")
    @Config.RequiresMcRestart
    public static int sword_cooldown = 3;

    @Config.Name("Lamented Sword Dash Velocity")
    @Config.Comment("Change the Velocity of the player when using the dash ability")
    @Config.RequiresMcRestart
    public static float sword_velocity = 2.4f;

    @Config.Name("Lamented Eye Cooldown")
    @Config.Comment("Change the cooldown Period of the Lamented Eye, in seconds")
    @Config.RequiresMcRestart
    public static int eye_cooldown = 8;

    @Config.Name("Lamentor Legacy texture")
    @Config.Comment("For those that prefer the legacy model and texture of the Lamentor")
    @Config.RequiresMcRestart
    public static boolean lamenter_legacy_texture = false;

    @Config.Name("Constructor Center Island Spawns Enabled/Disabled")
    @Config.Comment("Allow Constructors to spawn in the center island during generation of the Lamented Islands")
    @Config.RequiresMcRestart
    public static boolean constructor_center_spawn = true;

    @Config.Name("Lamented Islands Chest Spawn Chance")
    @Config.Comment("Change the chance of loot spawns for the Lamented Islands, higher number means lesser chance")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int lamentedIslandsLootChance = 2;

    @Config.Name("Lamentor Scaling Attack Damage")
    @Config.Comment("This value raises the attack damage of the Lamentor the lower it's health is, to incentivize further punishment in not dodging the bosses moves later in the fight")
    @Config.RequiresMcRestart
    public static double lamentor_scaled_attack = 0.4f;

    @Config.Name("Ash Biome Difficulty")
    @Config.Comment("A Universal config option that multiplies all base health, attack damage, and abilities by this value to all Ashed Wastelands Entities (Parasites, Stalkers, Knight Household, End King")
    @Config.RequiresMcRestart
    public static double biome_multiplier = 1D;

    @Config.Name("Lamented End Difficulty")
    @Config.Comment("A Universal config option that multiplies all base health, attack damage, and abilities for all mobs pertaining to the base End Biome, Lamentor, Constructor, Guilder, Seeker")
    @Config.RequiresMcRestart
    public static double lamented_multiplier = 1D;

    @Config.Name("End Ash Particle Turn off")
    @Config.Comment("Change to turn on or off the particles from the End Ash, use this to improve performance")
    @Config.RequiresMcRestart
    public static boolean disable_end_ash_particles = false;
    @Config.Name("End King Health")
    @Config.Comment("Change the Health of the Ender King")
    @Config.RequiresMcRestart
    public static float end_king_health = 500f;

    @Config.Name("End King Base Attack Damage")
    @Config.Comment("Change the base attack damage of the Ender King")
    @Config.RequiresMcRestart
    public static double end_king_damage = 14D;

    @Config.Name("End King Ghost Arm Damage Multiplier")
    @Config.Comment("Change the multiplier of ghost arm attacks * base damage")
    @Config.RequiresMcRestart
    public static double end_king_ghost_damage = 1.5D;

    @Config.Name("End King Leap Attack Multiplier")
    @Config.Comment("Change the multiplier of the End Kings Leap attack")
    @Config.RequiresMcRestart
    public static double end_king_leap_attack = 1.5D;

    @Config.Name("End King Red Ground Crystal Damage")
    @Config.Comment("Change the damage of the red Crystals the End King summons")
    @Config.RequiresMcRestart
    public static double red_crystal_damage = 9D;

    @Config.Name("End King Ground Sword Damage")
    @Config.Comment("Change the damage of the Swords that hit the ground in the End King boss fight")
    @Config.RequiresMcRestart
    public static double ground_sword_damage = 12D;

    @Config.Name("End King lazer Damage")
    @Config.Comment("Change the Damage Multiplier of the Lazer Beam shot by the End King")
    @Config.RequiresMcRestart
    public static double lazer_damage_multiplier = 1.7;

    @Config.Name("End King Lazer Explosion Size")
    @Config.Comment("Change the Explosion size of the Lazer Beam")
    @Config.RequiresMcRestart
    public static float lazer_explosion_size = 0;

    @Config.Name("End King Lazer Max Distance")
    @Config.Comment("Change the distance in which the lazer is shot")
    @Config.RequiresMcRestart
    public static double lazer_distance = 40;

    @Config.Name("End King Lazer Beam Lag")
    @Config.Comment("Change the Beam Lag of the lazer to catch up with the player")
    @Config.RequiresMcRestart
    public static int lazer_beam_lag = 9;

    @Config.Name("Knighthouse Projectile Swords Base Damage")
    @Config.Comment("Base damage of the projectile swords used by the Knights and End King")
    @Config.RequiresMcRestart
    public static float projectile_sword_damage = 6.0f;

    @Config.Name("Knighthouse Base Health")
    @Config.Comment("Base Health for the Ender Knight, Mage, Shielder")
    @Config.RequiresMcRestart
    public static double knighthouse_health = 35.0D;

    @Config.Name("Ender Knight Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Ender Knight")
    @Config.RequiresMcRestart
    public static float end_knight_damage = 7.0f;

    @Config.Name("Ender Shielder Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Ender Shielder")
    @Config.RequiresMcRestart
    public static float end_shielder_damage = 7.0f;

    @Config.Name("Ender Mage Heal Cooldown")
    @Config.Comment("Cooldown before an Ender Mage can cast a heal to a friendly knight, in seconds")
    @Config.RequiresMcRestart
    public static int end_mage_cooldown = 6;

    @Config.Name("Ender Mage Unholy Ritual Timer")
    @Config.Comment("After existing in the world for this long in seconds, there is a chance the Ender Mage will perform an Unholy ritual on a nearby knight")
    @Config.RequiresMcRestart
    public static int end_mage_ritual = 250;

    @Config.Name("Unholy End Knight Base Health")
    @Config.Comment("Change the Base Health of the Unholy End Knight")
    @Config.RequiresMcRestart
    public static double unholy_knight_health = 175D;

    @Config.Name("Unholy Knight Base Attack Damage")
    @Config.Comment("Change the Base attack damage of the Unholy knight")
    @Config.RequiresMcRestart
    public static float unholy_knight_damage = 8f;

    @Config.Name("Ashed Parasite Base Health")
    @Config.Comment("Change the base Health of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static double parasite_health = 30D;

    @Config.Name("Ashed Parasite Base Attack Damage")
    @Config.Comment("Change the base damage of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static float parasite_damage = 8.0f;

    @Config.Name("End Stalker Base Health")
    @Config.Comment("Change the base health of the End Stalker")
    @Config.RequiresMcRestart
    public static double stalker_health = 40D;

    @Config.Name("End Stalker Base Attack Damage")
    @Config.Comment("Change the Base Attack Damage of the End Stalker")
    @Config.RequiresMcRestart
    public static float stalker_damage = 8.0f;

    @Config.Name("End Stalker Hibernation Timer")
    @Config.Comment("After being spotted and hiding, this timer will start in which it will go out of hiding once ended, in seconds")
    @Config.RequiresMcRestart
    public static int stalker_hibernation = 50;

    @Config.Name("End Stalker Trigger Distance")
    @Config.Comment("After reaching within this distance, the End Stalker will enrage and go into attack mode")
    @Config.RequiresMcRestart
    public static double stalker_distance = 2D;

    @Config.Name("Red Rage Potion Effect Damage")
    @Config.Comment("Damage the Red Rage deals to entities near you when affected")
    @Config.RequiresMcRestart
    public static float potion_damage = 13.0f;

    @Config.Name("Nuke Base Damage")
    @Config.Comment("If caught inside the nuke, it will deal X damage while in it. Used by the End King")
    @Config.RequiresMcRestart
    public static float nuke_damage = 200f;

    @Config.Name("EndFall Sword Damage")
    @Config.Comment("Change the base damage of the EndFall Sword, this will also affect the Ghost arm as it is 1.5x Base Damage of the Sword")
    @Config.RequiresMcRestart
    public static float endfall_sword_damage = 8.0f;

    @Config.Name("EndFall Sword Cooldown")
    @Config.Comment("Change the cooldown after using the ghost Arm, in seconds")
    @Config.RangeInt(min = 2, max = 1000)
    @Config.RequiresMcRestart
    public static int endfall_sword_cooldown = 6;

    @Config.Name("Forgotten Medallion Cooldown")
    @Config.Comment("Change the cooldown after using the Forgotten Medallion")
    @Config.RangeInt(min = 2, max = 1000)
    @Config.RequiresMcRestart
    public static int medal_cooldown = 30;

    @Config.Name("Guilded Shield Cooldown")
    @Config.Comment("Change the cooldown for the Guilded shield, in seconds")
    @Config.RequiresMcRestart
    public static int shield_cooldown = 5;

    @Config.Name("End Staff Projectile Damage")
    @Config.Comment("Change the Damage of the Projectile that End Fall staff shoots")
    @Config.RequiresMcRestart
    public static float purp_projectile = 7.0f;

    @Config.Name("End Staff Projectile Cooldown")
    @Config.Comment("Change the Cooldown of the projectile the End Fall staff shoots, in seconds")
    @Config.RequiresMcRestart
    public static int purp_cooldown = 2;

    @Config.Name("End Staff Secondary Attack Cooldown")
    @Config.Comment("Change the Cooldown of the blow away plus Red Rage Attack, in seconds")
    @Config.RequiresMcRestart
    public static int staff_cooldown = 10;

    @Config.Name("Mobs Spawning at Middle End Islands")
    @Config.Comment("Change if mobs spawns at the middle island")
    @Config.RequiresMcRestart
    public static boolean does_spawn_middle = false;

    @Config.Name("Guilder Spawn Rate")
    @Config.Comment("Change spawnrate of Guilder")
    @Config.RequiresMcRestart
    public static int guilder_spawn_rate = 1;

    @Config.Name("End Stalker Spawn Rate")
    @Config.Comment("Change spawnrate of End Stalker")
    @Config.RequiresMcRestart
    public static int stalker_spawn_rate = 1;

    @Config.Name("Ashed Parasite Spawn Rate")
    @Config.Comment("Change spawnrate of Ashed Parasite")
    public static int parasite_spawn_rate = 3;

    @Config.Name("End King's Fortress Chest Spawn Chance")
    @Config.Comment("Chance for chests to spawn throughout the dungeon, Lower value is higher chances")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int dungeon_chest_chance = 3;

    @Config.Name("End King's Fortress Mob Spawn Chance")
    @Config.Comment("Chance for mobs to spawn throughout the dungeon, Lower value is higher chances")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int dungeon_mob_chance = 7;

    @Config.Name("End King Fortress")
    @Config.Comment("Change the spacing of the End King Fortress in chunks away from another, this is applied in chunks that are Ash Wastelands not every chunk")
    @Config.RequiresMcRestart
    public static int fortress_spacing = 50;

    @Config.Name("End King Fortress Odds")
    @Config.Comment("Change the odds of this structure spawning, take in mind the fortress has few structure to compete with for odds of spawning")
    @Config.RequiresMcRestart
    public static int fortress_odds = 1;

    @Config.Name("End King Fortress Size")
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
    public static int vault_mob_chance = 4;

    @Config.Name("End Vault Size")
    @Config.Comment("Change the size of the End Vaults, any bigger may result in buggy appliances")
    @Config.RequiresMcRestart
    public static int vault_size = 4;

    @Config.Name("End Vault Chance to Spawn")
    @Config.Comment("Change the chance to spawn the End Vaults, WARNING putting it low will result in buggy and or over spawning, as this is not determined in chunks apart, it's by each time the surface is above 55")
    @Config.RequiresMcRestart
    public static int vault_distance = 200;

    @Config.Name("End Seekers Health")
    @Config.Comment("Change the Health of the End Seekers")
    @Config.RequiresMcRestart
    public static double seeker_health = 30D;

    @Config.Name("End Seeker Attack Damaage")
    @Config.Comment("Change the Attack Damage of the End Seeker")
    @Config.RequiresMcRestart
    public static double seeker_attack_damage = 7D;



    @Config.Name("End Seekers Prime Health")
    @Config.Comment("Change the Health of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_health = 140D;

    @Config.Name("End Seeker Prime Attack Damaage")
    @Config.Comment("Change the Attack Damage of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_attack_damage = 9D;

    @Config.Name("Seeker Gun Cooldown")
    @Config.Comment("Change the cooldown of the seeker gun in seconds")
    @Config.RequiresMcRestart
    public static int seeker_gun_cooldown = 3;

}
