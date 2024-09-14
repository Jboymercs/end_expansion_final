package com.example.structure.config;

import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "End Expansion/item_config")
public class ItemConfig {

    @Config.Name("Lamented Sword Damage")
    @Config.Comment("Change the damage done by the Lamented Sword")
    @Config.RequiresMcRestart
    public static float sword_damage = 7;

    @Config.Name("Lamented Sword Dash Damage")
    @Config.Comment("Change the damage done by the dash ability upon initially dashing to nearby entities")
    @Config.RequiresMcRestart
    public static float sword_dash_damage = 4;

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
    public static float dagger_damage = 6;

    @Config.Name("Cordium Sword Cooldown")
    @Config.Comment("Change the cooldown of the Cordium Sword, note this cooldown extends per entity that a spike is summoned on. Each entity adding a second onto cooldown")
    @Config.RequiresMcRestart
    public static int cordium_sword_cooldown = 3;

    @Config.Name("Cordium Sword Damage")
    @Config.Comment("Change the base damage of the Cordium Sword")
    @Config.RequiresMcRestart
    public static float cordium_sword_damage = 6;

    @Config.Name("Unholy Sword Damage")
    @Config.Comment("Change the Damage of the Unholy Sword")
    @Config.RequiresMcRestart
    public static float unholy_sword_damage = 7;

    @Config.Name("Unholy Sword Potion Effect Duration")
    @Config.Comment("Change the duration that attacks dealt by this weapon that the entity is given weakness")
    @Config.RequiresMcRestart
    public static int unholy_sword_potion = 2;

    @Config.Name("Cordium Axe Damage")
    @Config.Comment("Change the Damage of the Cordium Axe")
    @Config.RequiresMcRestart
    public static float cordium_axe_damage = 9;

    @Config.Name("End's Revolt Axe Cooldown")
    @Config.Comment("Change the End's Revolt (pure_axe) cooldown")
    @Config.RequiresMcRestart
    public static int pure_axe_cooldown = 13;

    @Config.Name("End's Revolt Axe Damage")
    @Config.Comment("Change the End's Revolt (pure_axe) damage")
    @Config.RequiresMcRestart
    public static float pure_axe_damage = 7;

    @Config.Name("Lamented Eye Cooldown")
    @Config.Comment("Change the cooldown Period of the Lamented Eye, in seconds")
    @Config.RequiresMcRestart
    public static int eye_cooldown = 8;

    @Config.Name("EndFall Sword Damage")
    @Config.Comment("Change the base damage of the EndFall Sword, this will also affect the Ghost arm as it is 1.5x Base Damage of the Sword")
    @Config.RequiresMcRestart
    public static float endfall_sword_damage = 9;

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

    @Config.Name("Crown of A Past Era Durability")
    @Config.Comment("Change the Durability of the Crown of a Past Era")
    @Config.RequiresMcRestart
    public static int crown_durability = 1;

    @Config.Name("Crown of A Past Era Summon")
    @Config.Comment("Change if the Ashed King summoned by the crown is friendly to the user who summoned it, default: false")
    @Config.RequiresMcRestart
    public static boolean crown_is_friendly = false;

    @Config.Name("Crown of A Past Era Minion Lifetime")
    @Config.Comment("Change the life time of the End King summoned by the Crown of A Past Era in seconds")
    @Config.RequiresMcRestart
    public static int minion_lifeTime = 90;

    @Config.Name("Crown of A Past Era Minion Attack Damage")
    @Config.Comment("Change the Attack Damage of the Ashed King summoned by the Crown of A Past Era")
    @Config.RequiresMcRestart
    public static double minion_attack_damage = 28;

    @Config.Name("Guilded Shield Cooldown")
    @Config.Comment("Change the cooldown for the Guilded shield, in seconds")
    @Config.RequiresMcRestart
    public static int shield_cooldown = 5;

    @Config.Name("End Staff Projectile Damage")
    @Config.Comment("Change the Damage of the Projectile that End Fall staff shoots")
    @Config.RequiresMcRestart
    public static float purp_projectile = 14;

    @Config.Name("End Staff Projectile Cooldown")
    @Config.Comment("Change the Cooldown of the projectile the End Fall staff shoots, in seconds")
    @Config.RequiresMcRestart
    public static int purp_cooldown = 2;

    @Config.Name("End Staff Secondary Attack Cooldown")
    @Config.Comment("Change the Cooldown of the blow away plus Red Rage Attack, in seconds")
    @Config.RequiresMcRestart
    public static int staff_cooldown = 10;

    @Config.Name("Seeker Gun Cooldown")
    @Config.Comment("Change the cooldown of the seeker gun in seconds")
    @Config.RequiresMcRestart
    public static int seeker_gun_cooldown = 3;
}
