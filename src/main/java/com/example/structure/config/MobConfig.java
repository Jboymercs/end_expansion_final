package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/mob_config")
public class MobConfig {
    @Config.Name("Bosses of Destruction")
    @Config.Comment("When enabled, The Lamentor and the Ashed King will break more blocks while doing there attacks, default false")
    @Config.RequiresMcRestart
    public static boolean bosses_of_mass_destruction = false;

    @Config.Name("Lamentor Boss Health")
    @Config.Comment("Change the Health of the Lamentor")
    @Config.RequiresMcRestart
    public static float health = 375;

    @Config.Name("Lamentor Base Armor")
    @Config.Comment("Change the Armor Value of the Lamentor")
    @Config.RequiresMcRestart
    public static double lamentor_armor = 18;


    @Config.Name("Lamentor Base Armor Toughness")
    @Config.Comment("Change the Armor Toughness Value of the Lamentor")
    @Config.RequiresMcRestart
    public static double lamentor_toughness_armor = 2;
    @Config.Name("Lamentor Attack Speed")
    @Config.Comment("Change the speed at which the Lamentor attacks in seconds, smaller number means quicker, larger number means slower, warning 0 might be buggy")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int boss_speed = 1;

    @Config.Name("Lamentor Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lamentor")
    @Config.RequiresMcRestart
    public static double attack_damage= 18;


    @Config.Name("Lamentor Ranged Crystal Damage")
    @Config.Comment("Change the damage of the flying Crystals")
    @Config.RequiresMcRestart
    public static float crystal_damage= 19;

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
    public static float ground_crystal_damage= 17;

    @Config.Name("Constructor Health")
    @Config.Comment("Change the Health of the Constructor")
    @Config.RequiresMcRestart
    public static float constructor_health = 45;

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

    @Config.Name("Ancient Guilder Health")
    @Config.Comment("Change the Health of the Ancient Guilder")
    @Config.RequiresMcRestart
    public static double guilder_health = 35;

    @Config.Name("Ancient Guilder Attack Damage")
    @Config.Comment("Change the Attack Damage of the Anicent Guilder")
    @Config.RequiresMcRestart
    public static float guilder_attack_damage = 15;


    @Config.Name("Lamentor Legacy texture")
    @Config.Comment("For those that prefer the legacy model and texture of the Lamentor")
    @Config.RequiresMcRestart
    public static boolean lamenter_legacy_texture = false;

    @Config.Name("Lamentor Scaling Attack Damage")
    @Config.Comment("This value raises the attack damage of the Lamentor the lower it's health is, to incentivize further punishment in not dodging the bosses moves later in the fight")
    @Config.RequiresMcRestart
    public static double lamentor_scaled_attack = 0.4;


    @Config.Name("Ashed King Health")
    @Config.Comment("Change the Health of the Ashed King")
    @Config.RequiresMcRestart
    public static float end_king_health = 600;

    @Config.Name("Ashed King Base Armor")
    @Config.Comment("Change the base armor value of the Ashed King")
    @Config.RequiresMcRestart
    public static double end_king_armor = 16;

    @Config.Name("Ashed King Armor Toughness")
    @Config.Comment("Change the Armor Toughness of the Ashed King")
    @Config.RequiresMcRestart
    public static double end_king_armor_toughness = 3;

    @Config.Name("Ashed King Scaled Attack Factor")
    @Config.Comment("This value raises the attack damage of the Ashed King the lower it's health is, to incentivize further punishment in not dodging the bosses moves later in the fight")
    @Config.RequiresMcRestart
    public static double king_scaled_factor = 0.2;

    @Config.Name("Ashed King Base Attack Damage")
    @Config.Comment("Change the base attack damage of the Ashed King")
    @Config.RequiresMcRestart
    public static double end_king_damage = 28;

    @Config.Name("Ashed King Ghost Arm Damage Multiplier")
    @Config.Comment("Change the multiplier of ghost arm attacks * base damage")
    @Config.RequiresMcRestart
    public static double end_king_ghost_damage = 1.5;

    @Config.Name("Ashed King Leap Attack Multiplier")
    @Config.Comment("Change the multiplier of the Ashed Kings Leap attack")
    @Config.RequiresMcRestart
    public static double end_king_leap_attack = 1.3;

    @Config.Name("Ashed King Red Ground Crystal Damage")
    @Config.Comment("Change the damage of the red Crystals the Ashed King summons")
    @Config.RequiresMcRestart
    public static double red_crystal_damage = 19;

    @Config.Name("Ashed King Ground Sword Damage")
    @Config.Comment("Change the damage of the Swords that hit the ground in the Ashed King boss fight")
    @Config.RequiresMcRestart
    public static double ground_sword_damage = 27;

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
    public static float projectile_sword_damage = 12;

    @Config.Name("Knighthouse Base Health")
    @Config.Comment("Base Health for the Ender Knight, Mage, Shielder")
    @Config.RequiresMcRestart
    public static double knighthouse_health = 40;

    @Config.Name("Fell Knight Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Fell Knight")
    @Config.RequiresMcRestart
    public static float end_knight_damage = 12;

    @Config.Name("Fell Shielder Base Attack Damage")
    @Config.Comment("Base Attack Damage for the Fell Shielder")
    @Config.RequiresMcRestart
    public static float end_shielder_damage = 16;

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
    public static double unholy_knight_health = 225;

    @Config.Name("Unholy Knight Base Armor")
    @Config.Comment("Change the base armor value of the Unholy Knight")
    @Config.RequiresMcRestart
    public static double unholy_knight_armor = 12;

    @Config.Name("Unholy Knight Base Armor Toughness")
    @Config.Comment("Change the base armor toughness value of the Unholy Knight")
    @Config.RequiresMcRestart
    public static double unholy_knight_armor_toughness = 1;

    @Config.Name("Unholy Knight Base Attack Damage")
    @Config.Comment("Change the Base attack damage of the Unholy knight")
    @Config.RequiresMcRestart
    public static float unholy_knight_damage = 44;

    @Config.Name("Ashed Parasite Base Health")
    @Config.Comment("Change the base Health of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static double parasite_health = 35;

    @Config.Name("Ashed Parasite Base Attack Damage")
    @Config.Comment("Change the base damage of the Ashed Parasite")
    @Config.RequiresMcRestart
    public static float parasite_damage = 16;

    @Config.Name("End Stalker Base Health")
    @Config.Comment("Change the base health of the End Stalker")
    @Config.RequiresMcRestart
    public static double stalker_health = 40;

    @Config.Name("End Stalker Base Attack Damage")
    @Config.Comment("Change the Base Attack Damage of the End Stalker")
    @Config.RequiresMcRestart
    public static float stalker_damage = 19;

    @Config.Name("End Stalker Hibernation Timer")
    @Config.Comment("After being spotted and hiding, this timer will start in which it will go out of hiding once ended, in seconds")
    @Config.RequiresMcRestart
    public static int stalker_hibernation = 50;

    @Config.Name("End Stalker Trigger Distance")
    @Config.Comment("After reaching within this distance, the End Stalker will enrage and go into attack mode")
    @Config.RequiresMcRestart
    public static double stalker_distance = 2;


    @Config.Name("Depths Chomper Health")
    @Config.Comment("Change the Health of the Depths Chomper")
    @Config.RequiresMcRestart
    public static double chomper_health = 50;

    @Config.Name("Depths Chomper Attack Damage")
    @Config.Comment("Change the Chomper Attack Damage NOM NOM NOM!")
    @Config.RequiresMcRestart
    public static float chomper_attack_damange = 30;

    @Config.Name("Depths Chomper Spawn Rate")
    @Config.Comment("This is additional spawns that happen below Y 40, if you are wanting them to not spawn entirely, change Cave SpawnRates to 0")
    @Config.RequiresMcRestart
    public static int chomper_spawn_rate = 2;


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

    @Config.Name("Avalon Health")
    @Config.Comment("Change the Health of the Avalon Trader")
    @Config.RequiresMcRestart
    public static double avalon_health = 400;

    @Config.Name("Avalon Base Armor")
    @Config.Comment("Change the base armor of the Avalon")
    @Config.RequiresMcRestart
    public static double avalon_armor = 18;

    @Config.Name("Avalon Base Armor Toughness")
    @Config.Comment("Change the base armor toughness of the Avalon")
    @Config.RequiresMcRestart
    public static double avalon_toughness = 2;
    @Config.Name("Avalon Attack Damage")
    @Config.Comment("Change the Attack Damage of the Avalan Trader")
    @Config.RequiresMcRestart
    public static double avalon_attack_damage = 28;

    @Config.Name("Avalon Lazer Damage Multiplier")
    @Config.Comment("Change the Lazer Multipler by the Avalon's base attack damage")
    @Config.RequiresMcRestart
    public static double avalon_lazer_multiplier = 1.2;

    @Config.Name("Avalon Particle Reducer")
    @Config.Comment("When set to true Particles will be more relevant for any AOE attacks the Avalon does, default is false")
    @Config.RequiresMcRestart
    public static boolean enableAvalonParticles = false;

    @Config.Name("End Seekers Health")
    @Config.Comment("Change the Health of the End Seekers")
    @Config.RequiresMcRestart
    public static double seeker_health = 40;

    @Config.Name("End Seeker Attack Damage")
    @Config.Comment("Change the Attack Damage of the End Seeker")
    @Config.RequiresMcRestart
    public static double seeker_attack_damage = 15;

    @Config.Name("End Seekers Prime Health")
    @Config.Comment("Change the Health of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_health = 200;

    @Config.Name("End Seeker Prime Armor")
    @Config.Comment("Change the base armor value of the End Seeker")
    @Config.RequiresMcRestart
    public static double seeker_prime_armor = 14;

    @Config.Name("End Seeker Prime Armor Toughness")
    @Config.Comment("Change the base armor toughness value of the End Seeker")
    @Config.RequiresMcRestart
    public static double seeker_prime_armor_toughness = 0;

    @Config.Name("End Seeker Prime Attack Damaage")
    @Config.Comment("Change the Attack Damage of the End Seeker Prime")
    @Config.RequiresMcRestart
    public static double seeker_prime_attack_damage = 24;

    @Config.Name("Barrend Golem Health")
    @Config.Comment("Change the Health of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_health = 100;

    @Config.Name("Barrend Golem Attack Damage")
    @Config.Comment("Change the Attack Damage of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_attack_damage = 60;

    @Config.Name("Barrend Golem Base Armor")
    @Config.Comment("Change the base armor value of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_armor = 4;

    @Config.Name("Barrend Golem Base Armor Toughness")
    @Config.Comment("Change the Armor Toughness of the Barrend Golem")
    @Config.RequiresMcRestart
    public static double barrend_golem_armor_toughness = 0;

    @Config.Name("Barrend Golem Attack Multiplier")
    @Config.Comment("Change X times base Attack Damage for specific abilities of the Barrend Golem, such as it's charge, and Slam Attacks")
    @Config.RequiresMcRestart
    public static double barrend_golem_attack_multiplier = 1.4;

    @Config.Name("Evoled Parasite Health")
    @Config.Comment("Change the Health of the Evolved Parasite")
    @Config.RequiresMcRestart
    public static double evolved_parasite_health = 450;

    @Config.Name("Evolved Parasite Attack Damage")
    @Config.Comment("Change the Attack Damage of the Evolved Parasite")
    @Config.RequiresMcRestart
    public static double evolved_parasite_attack_damage = 48;

    @Config.Name("Evolved Parasite Armor")
    @Config.Comment("Change the Armor value of the Evolved Parasite")
    @Config.RequiresMcRestart
    public static double evolved_parasite_armor = 16;

    @Config.Name("Evolved Parasite Armor Toughness")
    @Config.Comment("Change the Armor Toughness of the Evolved Parasite")
    @Config.RequiresMcRestart
    public static double evolved_parasite_toughness = 4;

    @Config.Name("Mad Spirit Health")
    @Config.Comment("Change the Health of the Mad Spirit")
    @Config.RequiresMcRestart
    public static double mad_spirit_health = 40;

    @Config.Name("Mad Spirit Attack Damage")
    @Config.Comment("Change the Attack Damage of the Mad Spirit")
    @Config.RequiresMcRestart
    public static double mad_spirit_attack_damage = 30;

    @Config.Name("Lidoped Health")
    @Config.Comment("Change the Health of the Lidoped")
    @Config.RequiresMcRestart
    public static double lidoped_health = 20;

    @Config.Name("Lidoped Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lidoped")
    @Config.RequiresMcRestart
    public static double lidoped_attack_damage = 8;

    @Config.Name("Void Walker Health")
    @Config.Comment("Change the Health of the Void Walker")
    @Config.RequiresMcRestart
    public static double void_walker_health = 80;

    @Config.Name("Void Walker Attack Damage")
    @Config.Comment("Change the Attack Damage of the Void Walker")
    @Config.RequiresMcRestart
    public static double void_walker_attack_damage = 18;


}
