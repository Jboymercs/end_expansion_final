package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

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
    public static float health = 400f;

    @Config.Name("Lamentor Attack Speed")
    @Config.Comment("Change the speed at which the Lamentor attacks in seconds, smaller number means quicker, larger number means slower, warning 0 might be buggy")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int boss_speed = 1;

    @Config.Name("Lamentor Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lamentor")
    @Config.RequiresMcRestart
    public static double attack_damage= 15D;

    @Config.Name("Lamentor Ranged Crystal Damage")
    @Config.Comment("Change the damage of the flying Crystals")
    @Config.RequiresMcRestart
    public static float crystal_damage= 19.0f;

    @Config.Name("Lamentor Pierce Damage")
    @Config.Comment("Change the multiplier of the Lamentor's Pierce attacks, base damage * pierce multiplier")
    @Config.RequiresMcRestart
    public static float pierce_multiplier = 1.7f;

    @Config.Name("Lamentor Circle Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentor's Circle Attack, base damage * circle attack multiplier")
    @Config.RequiresMcRestart
    public static float circle_multiplier = 1.6f;

    @Config.Name("Lamentor Hammer Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentors Hammer Attack, base damage * hammer attack multiplier")
    @Config.RequiresMcRestart
    public static float hammer_multiplier = 1.8f;

    @Config.Name("Lamentor Explosion Size")
    @Config.Comment("Change the size of explosion for the Lamentors Hammer Attack")
    @Config.RangeInt(min = 0, max = 3)
    @Config.RequiresMcRestart
    public static float explosion_size = 2;

    @Config.Name("Ground Crystal Damage")
    @Config.Comment("Change the damage done by Ground Crystals")
    @Config.RequiresMcRestart
    public static float ground_crystal_damage= 17f;

    @Config.Name("Constructor Health")
    @Config.Comment("Change the Health of the Constructor")
    @Config.RequiresMcRestart
    public static float constructor_health = 55f;

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
    public static float constructor_shockwave_damage= 2.5f;

    @Config.Name("Constructor Structure Spawn Chance")
    @Config.Comment("Change the chance of how many Constructors will spawn on the Lamented Islands dungeon")
    @Config.RangeInt(min = 1, max = 10)
    public static int structure_spawns = 5;

    @Config.Name("Constructor Natural Spawn Weight")
    @Config.Comment("Change the weights at which the Constructors naturally spawn in the End, or disable it entirely, lower value means less spawns, higher means more common")
    @Config.RequiresMcRestart
    public static int constructor_weights = 1;

    @Config.Name("Ancient Guilder Health")
    @Config.Comment("Change the Health of the Ancient Guilder")
    @Config.RequiresMcRestart
    public static double guilder_health = 35D;

    @Config.Name("Ancient Guilder Attack Damage")
    @Config.Comment("Change the Attack Damage of the Anicent Guilder")
    @Config.RequiresMcRestart
    public static float guilder_attack_damage = 15F;

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

    @Config.Name("Cordium Dagger")
    @Config.Comment("Change the cooldown of the Cordium Dagger's ability, in seconds")
    @Config.RequiresMcRestart
    public static int dagger_cooldown = 3;

    @Config.Name("Cordium Dagger Damage")
    @Config.Comment("Change the Damage the Cordium Dagger does")
    @Config.RequiresMcRestart
    public static float dagger_damage = 6.0F;

    @Config.Name("Unholy Sword Damage")
    @Config.Comment("Change the Damage of the Unholy Sword")
    @Config.RequiresMcRestart
    public static float unholy_sword_damage = 8.0F;

    @Config.Name("Cordium Axe Damage")
    @Config.Comment("Change the Damage of the Cordium Axe")
    @Config.RequiresMcRestart
    public static float cordium_axe_damage = 9.0F;

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

    @Config.Name("Ash Fog Disable/Enable")
    @Config.Comment("When set to true, it will disable the dark fog found in the Ash Wastelands")
    @Config.RequiresMcRestart
    public static boolean isDarkFogDisabled = false;

    @Config.Name("Ash Fog Variable")
    @Config.Comment("Change the variable of the fog, this basically changes the distance for how far the fog goes. If you wish to disable the fog, the option above does that")
    @Config.RequiresMcRestart
    public static float dark_fog_variable = 0.085F;

    @Config.Name("Ash Biome Difficulty")
    @Config.Comment("A Universal config option that multiplies all base health, attack damage, and abilities by this value to all Ashed Wastelands Entities (Parasites, Stalkers, Knight Household, Ashed King")
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
    @Config.Name("Ashed King Health")
    @Config.Comment("Change the Health of the Ashed King")
    @Config.RequiresMcRestart
    public static float end_king_health = 600f;

    @Config.Name("Ashed King Scaled Attack Factor")
    @Config.Comment("This value raises the attack damage of the Ashed King the lower it's health is, to incentivize further punishment in not dodging the bosses moves later in the fight")
    @Config.RequiresMcRestart
    public static double king_scaled_factor = 0.2D;

    @Config.Name("Ashed King Base Attack Damage")
    @Config.Comment("Change the base attack damage of the Ashed King")
    @Config.RequiresMcRestart
    public static double end_king_damage = 28D;

    @Config.Name("Ashed King Ghost Arm Damage Multiplier")
    @Config.Comment("Change the multiplier of ghost arm attacks * base damage")
    @Config.RequiresMcRestart
    public static double end_king_ghost_damage = 1.5D;

    @Config.Name("Ashed King Leap Attack Multiplier")
    @Config.Comment("Change the multiplier of the Ashed Kings Leap attack")
    @Config.RequiresMcRestart
    public static double end_king_leap_attack = 1.3D;

    @Config.Name("Ashed King Red Ground Crystal Damage")
    @Config.Comment("Change the damage of the red Crystals the Ashed King summons")
    @Config.RequiresMcRestart
    public static double red_crystal_damage = 19D;

    @Config.Name("Ashed King Ground Sword Damage")
    @Config.Comment("Change the damage of the Swords that hit the ground in the Ashed King boss fight")
    @Config.RequiresMcRestart
    public static double ground_sword_damage = 27D;

    @Config.Name("Ashed King lazer Damage")
    @Config.Comment("Change the Damage Multiplier of the Lazer Beam shot by the End King")
    @Config.RequiresMcRestart
    public static double lazer_damage_multiplier = 1.3;

    @Config.Name("Ashed King Lazer Explosion Size")
    @Config.Comment("Change the Explosion size of the Lazer Beam")
    @Config.RequiresMcRestart
    public static float lazer_explosion_size = 0;

    @Config.Name("Ashed King Lazer Max Distance")
    @Config.Comment("Change the distance in which the lazer is shot")
    @Config.RequiresMcRestart
    public static double lazer_distance = 40;

    @Config.Name("Ashed King Lazer Beam Lag")
    @Config.Comment("Change the Beam Lag of the lazer to catch up with the player")
    @Config.RequiresMcRestart
    public static int lazer_beam_lag = 8;

    @Config.Name("Knighthouse Projectile Swords Base Damage")
    @Config.Comment("Base damage of the projectile swords used by the Knights and End King")
    @Config.RequiresMcRestart
    public static float projectile_sword_damage = 12.0f;

    @Config.Name("Knighthouse Base Health")
    @Config.Comment("Base Health for the Ender Knight, Mage, Shielder")
    @Config.RequiresMcRestart
    public static double knighthouse_health = 45.0D;

    @Config.Name("Fell Knight Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Fell Knight")
    @Config.RequiresMcRestart
    public static float end_knight_damage = 13.0f;

    @Config.Name("Fell Shielder Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Fell Shielder")
    @Config.RequiresMcRestart
    public static float end_shielder_damage = 17.0f;

    @Config.Name("Fell Mage Heal Cooldown")
    @Config.Comment("Cooldown before an Fell Mage can cast a heal to a friendly knight, in seconds")
    @Config.RequiresMcRestart
    public static int end_mage_cooldown = 6;

    @Config.Name("Fell Mage Unholy Ritual Timer")
    @Config.Comment("After existing in the world for this long in seconds, there is a chance the Fell Mage will perform an Unholy ritual on a nearby knight")
    @Config.RequiresMcRestart
    public static int end_mage_ritual = 250;

    @Config.Name("Unholy Knight Base Health")
    @Config.Comment("Change the Base Health of the Unholy Knight")
    @Config.RequiresMcRestart
    public static double unholy_knight_health = 200D;

    @Config.Name("Unholy Knight Base Attack Damage")
    @Config.Comment("Change the Base attack damage of the Unholy knight")
    @Config.RequiresMcRestart
    public static float unholy_knight_damage = 22.0f;

    @Config.Name("Ashed Parasite Base Health")
    @Config.Comment("Change the base Health of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static double parasite_health = 40D;

    @Config.Name("Ashed Parasite Base Attack Damage")
    @Config.Comment("Change the base damage of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static float parasite_damage = 16.0f;

    @Config.Name("End Stalker Base Health")
    @Config.Comment("Change the base health of the End Stalker")
    @Config.RequiresMcRestart
    public static double stalker_health = 40D;

    @Config.Name("End Stalker Base Attack Damage")
    @Config.Comment("Change the Base Attack Damage of the End Stalker")
    @Config.RequiresMcRestart
    public static float stalker_damage = 19.0f;

    @Config.Name("End Stalker Hibernation Timer")
    @Config.Comment("After being spotted and hiding, this timer will start in which it will go out of hiding once ended, in seconds")
    @Config.RequiresMcRestart
    public static int stalker_hibernation = 50;

    @Config.Name("End Stalker Trigger Distance")
    @Config.Comment("After reaching within this distance, the End Stalker will enrage and go into attack mode")
    @Config.RequiresMcRestart
    public static double stalker_distance = 2D;

    @Config.Name("Depths Chomper Health")
    @Config.Comment("Change the Health of the Depths Chomper")
    @Config.RequiresMcRestart
    public static double chomper_health = 50D;

    @Config.Name("Depths Chomper Attack Damage")
    @Config.Comment("Change the Chomper Attack Damage NOM NOM NOM!")
    @Config.RequiresMcRestart
    public static float chomper_attack_damange = 30F;

    @Config.Name("Depths Chomper Spawn Rate")
    @Config.Comment("This is additional spawns that happen below Y 40, if you are wanting them to not spawn entirely, change Cave SpawnRates to 0")
    @Config.RequiresMcRestart
    public static int chomper_spawn_rate = 2;

    @Config.Name("Red Rage Potion Effect Damage")
    @Config.Comment("Damage the Red Rage deals to entities near you when affected")
    @Config.RequiresMcRestart
    public static float potion_damage = 17.0f;

    @Config.Name("Nuke Base Damage")
    @Config.Comment("If caught inside the nuke, it will deal X damage while in it. Used by the End King")
    @Config.RequiresMcRestart
    public static float nuke_damage = 200f;

    @Config.Name("EndFall Sword Damage")
    @Config.Comment("Change the base damage of the EndFall Sword, this will also affect the Ghost arm as it is 1.5x Base Damage of the Sword")
    @Config.RequiresMcRestart
    public static float endfall_sword_damage = 9.0f;

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

    @Config.Name("Crown of A Past Era Cooldown")
    @Config.Comment("Change the cooldown of the Crown of A Past Era, in seconds")
    @Config.RangeInt(min = 1, max = 9000)
    public static int crown_cooldown = 600;

    @Config.Name("Crown of A Past Era Minion Lifetime")
    @Config.Comment("Change the life time of the End King summoned by the Crown of A Past Era in seconds")
    @Config.RequiresMcRestart
    public static int minion_lifeTime = 240;

    @Config.Name("Crown of A Past Era Minion Attack Damage")
    @Config.Comment("Change the Attack Damage of the Ashed King summoned by the Crown of A Past Era")
    @Config.RequiresMcRestart
    public static double minion_attack_damage = 28D;

    @Config.Name("Guilded Shield Cooldown")
    @Config.Comment("Change the cooldown for the Guilded shield, in seconds")
    @Config.RequiresMcRestart
    public static int shield_cooldown = 5;

    @Config.Name("End Staff Projectile Damage")
    @Config.Comment("Change the Damage of the Projectile that End Fall staff shoots")
    @Config.RequiresMcRestart
    public static float purp_projectile = 14.0f;

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
    public static int fortress_spacing = 70;

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
    public static int vault_mob_chance = 7;


    @Config.Name("End Vault Size")
    @Config.Comment("Change the size of the End Vaults, any bigger may result in buggy appliances")
    @Config.RequiresMcRestart
    public static int vault_size = 4;

    @Config.Name("End Vault Chance to Spawn")
    @Config.Comment("Change the chance to spawn the End Vaults, WARNING putting it low will result in buggy and or over spawning, as this is not determined in chunks apart, it's by each time the surface is above 55")
    @Config.RequiresMcRestart
    public static int vault_distance = 200;

    @Config.Name("Avalon Trader Spacing")
    @Config.Comment("Change the spacing of the Avalon Trader, lower is more frequent, higher is less")
    @Config.RequiresMcRestart
    public static int avalon_trader_spacing = 400;

    @Config.Name("Avalon Health")
    @Config.Comment("Change the Health of the Avalon Trader")
    @Config.RequiresMcRestart
    public static double avalon_health = 400D;

    @Config.Name("Avalon Attack Damage")
    @Config.Comment("Change the Attack Damage of the Avalan Trader")
    @Config.RequiresMcRestart
    public static double avalon_attack_damage = 34D;

    @Config.Name("Avalon Lazer Damage Multiplier")
    @Config.Comment("Change the Lazer Multipler by the Avalon's base attack damage")
    @Config.RequiresMcRestart
    public static double avalon_lazer_multiplier = 1.3;

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

    @Config.Name("End Seekers Health")
    @Config.Comment("Change the Health of the End Seekers")
    @Config.RequiresMcRestart
    public static double seeker_health = 40D;

    @Config.Name("End Seeker Attack Damage")
    @Config.Comment("Change the Attack Damage of the End Seeker")
    @Config.RequiresMcRestart
    public static double seeker_attack_damage = 13D;



    @Config.Name("End Seekers Prime Health")
    @Config.Comment("Change the Health of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_health = 160D;

    @Config.Name("End Seeker Prime Attack Damaage")
    @Config.Comment("Change the Attack Damage of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_attack_damage = 16D;

    @Config.Name("Barrend Golem Health")
    @Config.Comment("Change the Health of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_health = 85D;

    @Config.Name("Barrend Golem Attack Damage")
    @Config.Comment("Change the Attack Damage of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_attack_damage = 32D;

    @Config.Name("Barrend Golem Attack Multiplier")
    @Config.Comment("Change X times base Attack Damage for specific abilities of the Barrend Golem, such as it's charge, and Slam Attacks")
    @Config.RequiresMcRestart
    public static double barrend_golem_attack_multiplier = 1.4D;

    @Config.Name("Seeker Gun Cooldown")
    @Config.Comment("Change the cooldown of the seeker gun in seconds")
    @Config.RequiresMcRestart
    public static int seeker_gun_cooldown = 3;

    @Config.Name("Mod Scaling Factor")
    @Config.Comment("When more than 1 players are near, this factor will add on how much Health all bosses/mini-bosses has. Default Per more than one player is 50% more HP or 0.5. Example: There is " +
            "2 Players around a boss that has a health of 200 by default. With there being a second player the bosses health is now 300")
    @Config.RequiresMcRestart
    public static double scale_mod_bosses = 0.5D;

    @Config.Name("Mod Scaling Attack Damage Factor")
    @Config.Comment("When more than 1 players are near, this factor will be multiplied by how many players are near and then by this bosses attack damage")
    @Config.RequiresMcRestart
    public static double scale_attack_damge = 0.23D;

    @Config.Name("Disable Mod Scaling")
    @Config.Comment("Disable scaling for Health of bosses when more than one players is nearby, set to true to disable")
    @Config.RequiresMcRestart
    public static boolean disable_scaling_mod = false;


    //Armor
    @Config.Name("Scale Cordium Armor set")
    @Config.Comment("Scale the Cordium Armor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double cordium_armor_scale = 1.0D;

    @Config.Name("Scale Dark Amor Set")
    @Config.Comment("Scale the Dark Amor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double dark_armor_scale = 1.0D;

    @Config.Name("Scale End Fall Armor Set")
    @Config.Comment("Scale the End Fall Armor set, putting anything above 1 will times the base values of the armor by this amount, useful for modpack creators")
    @Config.RequiresMcRestart
    public static double fall_armor_scale = 1.0D;

}
