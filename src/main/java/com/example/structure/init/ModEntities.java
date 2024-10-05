package com.example.structure.init;

import com.example.structure.Main;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.*;
import com.example.structure.entity.arrow.EntityChomperArrow;
import com.example.structure.entity.arrow.EntityGreenArrow;
import com.example.structure.entity.arrow.EntityUnholyArrow;
import com.example.structure.entity.barrend.EntityBarrendGolem;
import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.barrend.EntityMadSpirit;
import com.example.structure.entity.endking.*;
import com.example.structure.entity.endking.friendly.EntityFriendKing;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.endking.ghosts.EntityPermanantGhost;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityHealAura;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.lamentorUtil.EntityLamentorWave;
import com.example.structure.entity.painting.EntityEEPainting;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.entity.tileentity.*;
import com.example.structure.entity.tileentity.source.TileEntityNoSource;
import com.example.structure.entity.tileentity.source.TileEntityPowerSource;
import com.example.structure.entity.trader.*;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {

    private static final Map<Class<? extends Entity>, String> ID_MAP = new HashMap<>();

    private static int ENTITY_START_ID = 123;
    private static int PROJECTILE_START_ID = 230;
    private static int PARTICLE_START_ID = 500;

    public static Vec3i end_mobs = new Vec3i(0x833d91, 0xd8d295, 0);
    public static Vec3i knight_mobs = new Vec3i(0x6B0103, 0xd8d295, 0);

    public static Vec3i ash_mobs = new Vec3i(0x260B2C, 0xd8d295, 0);

    public static void registerEntities() {
        //Crystal Boss
        registerEntityWithID("crystal_boss", EntityCrystalKnight.class, ENTITY_START_ID++, 50, end_mobs);
        //Shulker Constructor
        registerEntityWithID("buffker", EntityBuffker.class, ENTITY_START_ID++, 50, end_mobs);
        //Crystal Ball - Utility
        registerEntity("crystal_ball", EntityCrystalSpikeSmall.class, ENTITY_START_ID++, 50);
        //Ground Crystal - Utility
        registerEntity("crytsal_ground", EntityGroundCrystal.class, ENTITY_START_ID++, 50);
        //IdleEntity
        registerEntity("entity_idle", EntityExplosion.class, ENTITY_START_ID++, 50);
        //Tile Entity
        registerTileEntity(TileEntityUpdater.class, "updater");
        //Quake - Utility
        registerEntity("projectile_quake", ProjectileQuake.class, ENTITY_START_ID++, 50);
        //Tile Entity - Utility
        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        //Tile Entity - Utility
        registerTileEntity(TileEntityDoorStart.class, "door");
        //Tile Entity - Utility
        registerTileEntity(TileEntityDeactivate.class, "door_deactivate");
        //Tile Entity - Utility
        registerTileEntity(TileEntityActivate.class, "door_activate");
        //Tile Entity - Utility
        registerTileEntity(TileEntityTrap.class, "floor_trap");
        //Reverse On
        registerTileEntity(TileEntityReverse.class, "reverse_on");
        //Reverse Off
        registerTileEntity(TileEntityReverseOff.class, "reverse_off");
        //Source On
        registerTileEntity(TileEntityPowerSource.class, "source_on");
        //Source OFf
        registerTileEntity(TileEntityNoSource.class, "source_off");
        //Compulsor
        registerTileEntity(TileEntityCompulsor.class, "compulsor_entity");
        //Ash Chute - Utility
        registerTileEntity(TileEntityAshChute.class, "ash_chute_entity");
        //Arena Entity
        registerTileEntity(TileEntityUnEndingArena.class, "entity_unending_arena");
        //Small Barrend Pot
        registerTileEntity(TileEntityBarePot.class, "small_pot");
        //End King
        registerEntityWithID("end_king", EntityEndKing.class, ENTITY_START_ID++, 50, knight_mobs);
        //Red Crystal
        registerEntity("red_crystal", EntityRedCrystal.class, ENTITY_START_ID++, 50);
        //SpinSword Projectile
        registerEntity("red_sword", ProjectileSpinSword.class, ENTITY_START_ID++, 60);
        //Fireball Entity
        registerEntity("fire_ball_red", EntityFireBall.class, ENTITY_START_ID++, 60);
        //Nuclear Explosion
        registerEntity("nuke", EntityNuclearExplosion.class, ENTITY_START_ID++, 60);
        //End Bug
        registerEntityWithID("end_bug", EntityEndBug.class, ENTITY_START_ID++, 60, ash_mobs);
        //Ender Knight
        registerEntityWithID("end_knight", EntityEnderKnight.class, ENTITY_START_ID++, 50, knight_mobs);
        //Ender Shield
        registerEntityWithID("end_shield", EntityEnderShield.class, ENTITY_START_ID++, 60, knight_mobs);
        //Ender Mage
        registerEntityWithID("end_mage", EntityEnderMage.class, ENTITY_START_ID++, 60, knight_mobs);
        //Ender Sword Ultra - Hidden Bosses
        registerEntityWithID("end_lord", EntityKnightLord.class, ENTITY_START_ID++, 60, knight_mobs);
        //Snatcher
        registerEntityWithID("snatcher", EntitySnatcher.class, ENTITY_START_ID++, 70, ash_mobs);
        //Heal Aura
        registerEntity("heal_aura", EntityHealAura.class, ENTITY_START_ID++, 60);
        //Ghost King Phase 2 Attack
        registerEntity("ghost_king_phase", EntityGhostPhase.class, ENTITY_START_ID++, 70);
        //The Ring
        registerEntity("ring_eye", EntityEye.class, ENTITY_START_ID, 70);
        //Fireballs
        registerEntity("fire_ball_red", ProjectileFireBalls.class, ENTITY_START_ID++, 70);
        //Wall
        registerEntity("wall", EntityWall.class, ENTITY_START_ID++, 70);
        //Mega Structure Block
        registerTileEntity(TileEntityMegaStructure.class, "mega_structure");
        //Alien Controller
        registerEntityWithID("controller", EntityController.class, ENTITY_START_ID++, 70, end_mobs);
        //Lamented Eye - Utility
        registerEntity("lame_eye", EntityLamentedEye.class, ENTITY_START_ID++, 70);
        //Ghost Arm - Utility
        registerEntity("ghost_arm", EntityGhostArm.class, ENTITY_START_ID++, 70);
        //Mini Nuke Ignition
        registerEntity("mini_nuke", EntityMiniNuke.class, ENTITY_START_ID++, 70);
        //Projectile Purple - Utility
        registerEntity("purple_ball", ProjectilePurple.class, ENTITY_START_ID++, 70);
        //Permanant End King Ghost - Phase 3 - Utility
        registerEntity("pghost_king", EntityPermanantGhost.class, ENTITY_START_ID++, 70);
        //Ground Sword Projectile - Utitlity
        registerEntity("sword_attack", EntityGroundSword.class, ENTITY_START_ID++, 80);
        //End Seeker
        registerEntityWithID("end_seeker", EndSeeker.class, ENTITY_START_ID++, 80, end_mobs);
        //Ender Eye
        registerEntityWithID("end_eye", EntityEnderEyeFly.class, ENTITY_START_ID++, 80, end_mobs);
        //End Seeker - Hidden Bosses
        registerEntityWithID("end_seeker_prime", EndSeekerPrime.class, ENTITY_START_ID++, 80, end_mobs);
        //Barrend Golem - Hidden Bosses
        registerEntityWithID("golem_b", EntityBarrendGolem.class, ENTITY_START_ID++, 80, ash_mobs);
        //End King Friendly - Item Summon
        registerEntity("friend_king", EntityFriendKing.class, ENTITY_START_ID++, 80);

        registerEntityWithID("depths_chomper", EntityChomper.class, ENTITY_START_ID++, 80, ash_mobs);
        registerEntity("large_aoe", EntityLargeAOEEffect.class, ENTITY_START_ID++, 80);
        registerEntity("unholy_arrow", EntityUnholyArrow.class, ENTITY_START_ID++, 80);
        registerEntity("chomper_arrow", EntityChomperArrow.class, ENTITY_START_ID++, 90);
        registerEntity("green_arrow", EntityGreenArrow.class, ENTITY_START_ID++, 90);
        registerEntity("sword_spike", EntitySwordSpike.class, ENTITY_START_ID++, 90);
        //Avalon Trader
        registerEntityWithID("avalon", EntityAvalon.class, ENTITY_START_ID++, 90, end_mobs);
        //Mini Avalon
        registerEntity("mini_avalon", EntityMiniValon.class, ENTITY_START_ID++, 100);
        //AOE Entity
        registerEntity("aoe_avalon", EntityAOEArena.class, ENTITY_START_ID++, 100);
        //Bomb Projectile used by the Avalon
        registerEntity("bomb_proj", ProjectileBomb.class, ENTITY_START_ID++, 100);
        //Controller Lift
        registerEntity("controller_lift", EntityControllerLift.class, ENTITY_START_ID++, 100);
        //Projectile Acid - Barrend Golem
        registerEntity("acid_proj", ProjectileAcid.class, ENTITY_START_ID++, 100);
        //Barrend Parasite
        registerEntity("barrend_parasite", EntityBarrendParasite.class, ENTITY_START_ID++, 100);
        //Lamentor Wave
        registerEntity("lamentor_wave", EntityLamentorWave.class, ENTITY_START_ID++, 100);
        //End Expansion Painting
        registerEntity("lamented_islands_painting", EntityEEPainting.class, ENTITY_START_ID++, 100);
        //Barrend Lidoped
        registerEntityWithID("lidoped", EntityLidoped.class, ENTITY_START_ID++, 100, ash_mobs);
        //Mad Spirit
        registerEntityWithID("mad_spirit", EntityMadSpirit.class, ENTITY_START_ID++, 100, ash_mobs);


    }

    public static void RegisterEntitySpawns() {
        spawnRate(EntityController.class, EnumCreatureType.MONSTER, MobConfig.guilder_spawn_rate, 1, 2, BiomeDictionary.Type.END);
        spawnRateBiomeSpecific(EntityEndBug.class, EnumCreatureType.MONSTER, MobConfig.parasite_spawn_rate, 1, 4, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntitySnatcher.class, EnumCreatureType.MONSTER, MobConfig.stalker_spawn_rate, 1, 1, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntityChomper.class, EnumCreatureType.MONSTER, MobConfig.chomper_spawn_rate, 1, 3, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntityLidoped.class, EnumCreatureType.MONSTER, 3, 1, 2, BiomeRegister.BARREND_LOWLANDS);
        spawnRateBiomeSpecific(EntityEndBug.class, EnumCreatureType.MONSTER, 2, 1, 2, BiomeRegister.BARREND_LOWLANDS);
        spawnRateBiomeSpecific(EntityMadSpirit.class, EnumCreatureType.MONSTER, 2, 1, 2, BiomeRegister.BARREND_LOWLANDS);
    }



    public static String getID(Class<? extends Entity> entity) {
        if (ID_MAP.containsKey(entity)) {
            return ModReference.MOD_ID + ":" + ID_MAP.get(entity);
        }
        throw new IllegalArgumentException("Mapping of an entity has not be registered for the end expansion mob spawner system.");
    }

    private static void registerEntityWithID(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
        ID_MAP.put(entity, name);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true);
    }

    private static void registerFastProjectile(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, false);
    }

    private static void registerTileEntity(Class<? extends TileEntity> entity, String name) {
        GameRegistry.registerTileEntity(entity, new ResourceLocation(ModReference.MOD_ID + ":" + name));
    }

    private static void spawnRate(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, BiomeDictionary.Type biomesAllowed) {
        for(Biome biome: BiomeDictionary.getBiomes(biomesAllowed)) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }
        }
    }

    private static void spawnRateBiomeSpecific(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, Biome biome) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }

    }

}
