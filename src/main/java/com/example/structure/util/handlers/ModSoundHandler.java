package com.example.structure.util.handlers;

import com.example.structure.util.ModReference;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
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

    //End Seeker
    public static SoundEvent SEEKER_SHOOT;
    public static SoundEvent SEEKER_HOVER;
    public static SoundEvent SEEKER_HURT;

    //Crystal HUM
    public static SoundEvent RED_CRYSTAL_HUM;

    //Ashed Parasite
    public static SoundEvent PARASITE_IDLE;
    public static SoundEvent PARASITE_HURT;
    public static SoundEvent PARASITE_DEATH;


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

        SEEKER_SHOOT = registerSound("seeker.shoot", "entity");
        SEEKER_HOVER = registerSound("seeker.hover", "entity");
        SEEKER_HURT = registerSound("seeker.hurt", "entity");

        RED_CRYSTAL_HUM = registerSound("crystal.glow", "block");

        PARASITE_IDLE = registerSound("parasite.idle", "entity");
        PARASITE_HURT = registerSound("parasite.hurt", "entity");
        PARASITE_DEATH = registerSound("parasite.death", "entity");
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
