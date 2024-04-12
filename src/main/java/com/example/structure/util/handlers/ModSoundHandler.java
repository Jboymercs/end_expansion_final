package com.example.structure.util.handlers;

import com.example.structure.util.ModReference;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Keeps track of Sounds and used to call from here
 */
public class ModSoundHandler {
    //Living Sounds
    public static SoundEvent BOSS_HURT;
    public static SoundEvent BOSS_IDLE;
    public static SoundEvent BOSS_SUMMON;
    public static SoundEvent BOSS_DEATH;

    //Action Sounds
    public static SoundEvent BOSS_CAST_AMBIENT;
    public static SoundEvent BOSS_DASH;
    public static SoundEvent BOSS_DRAW_HAMMER;
    public static SoundEvent BOSS_DRAW_SWORD;

    //King Action Sounds
    public static SoundEvent KING_DASH;

    //NuclearBomb Souns
    public static SoundEvent BOMB_EXPLODE;

    //Knight Household
    public static SoundEvent KNIGHT_STEP;
    public static SoundEvent KNIGHT_HURT;
    public static SoundEvent KNIGHT_DEATH;
    public static SoundEvent KNIGHT_IDLE;
    public static SoundEvent KNIGHT_DASH;
    public static SoundEvent KNIGHT_CAST_HEAL;
    public static SoundEvent KNIGHT_CAST_ATTACK;

    //End Seeker
    public static SoundEvent SEEKER_SHOOT;
    public static SoundEvent SEEKER_HOVER;
    public static SoundEvent SEEKER_HURT;
    public static SoundEvent SEEKER_ELDER_HURT;
    public static SoundEvent SEEKER_DASH;

    //Crystal HUM
    public static SoundEvent RED_CRYSTAL_HUM;
    //Compulsor On
    public static SoundEvent COMPULSOR_HUM;

    //Ashed Parasite
    public static SoundEvent PARASITE_IDLE;
    public static SoundEvent PARASITE_HURT;
    public static SoundEvent PARASITE_DEATH;
    public static SoundEvent PARASITE_STEP;

    //Stalker
    public static SoundEvent STALKER_HURT;
    public static SoundEvent STALKER_ATTACK_1;
    public static SoundEvent STALKER_SPOTTED;
    public static SoundEvent STALKER_SWING;

    public static SoundEvent STALKER_STEP;
    public static void registerSounds() {
        BOSS_IDLE = registerSound("boss.idle", "entity");
        BOSS_HURT = registerSound("boss.hurt", "entity");
        BOSS_DEATH = registerSound("boss.death", "entity");
        BOSS_SUMMON = registerSound("boss.summon", "entity");

        BOSS_DRAW_HAMMER = registerSound("boss.hammer", "entity");
        BOSS_DRAW_SWORD = registerSound("boss.sword", "entity");
        BOSS_DASH = registerSound("boss.dash", "entity");
        BOSS_CAST_AMBIENT = registerSound("boss.cast", "entity");

        KING_DASH = registerSound("king.dash", "entity");

        BOMB_EXPLODE = registerSound("king.explode", "entity");

        KNIGHT_STEP = registerSound("knight.step", "entity");
        KNIGHT_HURT = registerSound("knight.hurt", "entity");
        KNIGHT_DEATH = registerSound("knight.death", "entity");
        KNIGHT_IDLE = registerSound("knight.idle", "entity");
        KNIGHT_DASH = registerSound("knight.dash", "entity");
        KNIGHT_CAST_HEAL = registerSound("knight.cast_heal", "entity");
        KNIGHT_CAST_ATTACK = registerSound("knight.cast", "entity");

        SEEKER_SHOOT = registerSound("seeker.shoot", "entity");
        SEEKER_HOVER = registerSound("seeker.hover", "entity");
        SEEKER_HURT = registerSound("seeker.hurt", "entity");
        SEEKER_ELDER_HURT = registerSound("seeker.hurt_elder", "entity");
        SEEKER_DASH = registerSound("seeker.dash", "entity");

        RED_CRYSTAL_HUM = registerSound("crystal.glow", "block");
        COMPULSOR_HUM = registerSound("compulsor.sound", "block");

        PARASITE_IDLE = registerSound("parasite.idle", "entity");
        PARASITE_HURT = registerSound("parasite.hurt", "entity");
        PARASITE_DEATH = registerSound("parasite.death", "entity");
        PARASITE_STEP = registerSound("parasite.step", "entity");

        STALKER_HURT = registerSound("stalker.hurt", "entity");
        STALKER_ATTACK_1 = registerSound("stalker.attack", "entity");
        STALKER_SPOTTED = registerSound("stalker.spotted", "entity");
        STALKER_SWING = registerSound("stalker.swing", "entity");
        STALKER_STEP = registerSound("stalker.step", "entity");

    }


    private static SoundEvent registerSound(String name, String category) {
        String fullName = category + "." + name;
        ResourceLocation location = new ResourceLocation(ModReference.MOD_ID, fullName);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(fullName);
        ForgeRegistries.SOUND_EVENTS.register(event);

        return event;
    }
}
