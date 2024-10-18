package com.example.structure.init;

import com.example.structure.Main;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.*;
import com.example.structure.entity.arrow.EntityChomperArrow;
import com.example.structure.entity.arrow.EntityGreenArrow;
import com.example.structure.entity.arrow.EntityUnholyArrow;
import com.example.structure.entity.barrend.*;
import com.example.structure.entity.barrend.ultraparasite.EntityMoveTile;
import com.example.structure.entity.barrend.ultraparasite.EntityParasiteBombAOE;
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

    //Main Boss or Secret Class
    public static Vec3i lamentor_boss = new Vec3i(0x3eaccc,0xeadc2d,0);
    public static Vec3i ashed_king_boss = new Vec3i(0xd31a4e,0xeadc2d,0);
    public static Vec3i avalon_boss = new Vec3i(0x8a017f,0xeadc2d,0);

    //Mini Boss Class
    public static Vec3i elder_seeker = new Vec3i(0xc5d455,0xd8d295,0);
    public static Vec3i unholy_knight = new Vec3i(0xff0000,0xd8d295,0);
    public static Vec3i barrend_golem = new Vec3i(0x9108ab,0xd8d295,0);
    public static Vec3i evoled_parasite = new Vec3i(0xc60587,0xd8d295,0);

    //Combat mobs Class
    public static Vec3i end_seeker = new Vec3i(0xc5d455,0x696969,0);
    public static Vec3i contructor = new Vec3i(0xef6bf5,0x696969,0);
    public static Vec3i fell_knight = new Vec3i(0xe23131,0x696969,0);
    public static Vec3i fell_shield = new Vec3i(0xb50202,0x696969,0);
    public static Vec3i fell_mage = new Vec3i(0xf66a6a,0x696969,0);
    public static Vec3i mad_spirit = new Vec3i(0x63c53d,0x696969,0);

    //Biome Ambience Mobs
    public static Vec3i ender_eye = new Vec3i(0x3d7cc5,0xc8c8c8,0);
    public static Vec3i guilder = new Vec3i(0x08cbc1,0xc8c8c8,0);
    public static Vec3i ashed_parasite = new Vec3i(0xa63e78,0xc8c8c8,0);
    public static Vec3i end_stalker = new Vec3i(0x3e3b3d,0xc8c8c8,0);
    public static Vec3i depths_chomper = new Vec3i(0x775803,0xc8c8c8,0);
    public static Vec3i lidoped = new Vec3i(0xd19c0b,0xc8c8c8,0);
    public static Vec3i barrend_walker = new Vec3i(0x543257,0xc8c8c8,0);


    public static void registerEntities() {
        //Crystal Boss
        registerEntityWithID("crystal_boss", EntityCrystalKnight.class, ENTITY_START_ID++, 50, lamentor_boss);
        //Shulker Constructor
        registerEntityWithID("buffker", EntityBuffker.class, ENTITY_START_ID++, 50, contructor);
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
        registerEntityWithID("end_king", EntityEndKing.class, ENTITY_START_ID++, 50, ashed_king_boss);
        //Red Crystal
        registerEntity("red_crystal", EntityRedCrystal.class, ENTITY_START_ID++, 50);
        //SpinSword Projectile
        registerEntity("red_sword", ProjectileSpinSword.class, ENTITY_START_ID++, 60);
        //Fireball Entity
        registerEntity("fire_ball_red", EntityFireBall.class, ENTITY_START_ID++, 60);
        //Nuclear Explosion
        registerEntity("nuke", EntityNuclearExplosion.class, ENTITY_START_ID++, 60);
        //End Bug
        registerEntityWithID("end_bug", EntityEndBug.class, ENTITY_START_ID++, 60, ashed_parasite);
        //Ender Knight
        registerEntityWithID("end_knight", EntityEnderKnight.class, ENTITY_START_ID++, 50, fell_knight);
        //Ender Shield
        registerEntityWithID("end_shield", EntityEnderShield.class, ENTITY_START_ID++, 60, fell_shield);
        //Ender Mage
        registerEntityWithID("end_mage", EntityEnderMage.class, ENTITY_START_ID++, 60, fell_mage);
        //Ender Sword Ultra - Hidden Bosses
        registerEntityWithID("end_lord", EntityKnightLord.class, ENTITY_START_ID++, 60, unholy_knight);
        //Snatcher
        registerEntityWithID("snatcher", EntitySnatcher.class, ENTITY_START_ID++, 70, end_stalker);
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
        registerEntityWithID("controller", EntityController.class, ENTITY_START_ID++, 70, guilder);
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
        registerEntityWithID("end_seeker", EndSeeker.class, ENTITY_START_ID++, 80, end_seeker);
        //Ender Eye
        registerEntityWithID("end_eye", EntityEnderEyeFly.class, ENTITY_START_ID++, 80, ender_eye);
        //End Seeker - Hidden Bosses
        registerEntityWithID("end_seeker_prime", EndSeekerPrime.class, ENTITY_START_ID++, 80, elder_seeker);
        //Barrend Golem - Hidden Bosses
        registerEntityWithID("golem_b", EntityBarrendGolem.class, ENTITY_START_ID++, 80, barrend_golem);
        //End King Friendly - Item Summon
        registerEntity("friend_king", EntityFriendKing.class, ENTITY_START_ID++, 80);

        registerEntityWithID("depths_chomper", EntityChomper.class, ENTITY_START_ID++, 80, depths_chomper);
        registerEntity("large_aoe", EntityLargeAOEEffect.class, ENTITY_START_ID++, 80);
        registerEntity("unholy_arrow", EntityUnholyArrow.class, ENTITY_START_ID++, 80);
        registerEntity("chomper_arrow", EntityChomperArrow.class, ENTITY_START_ID++, 90);
        registerEntity("green_arrow", EntityGreenArrow.class, ENTITY_START_ID++, 90);
        registerEntity("sword_spike", EntitySwordSpike.class, ENTITY_START_ID++, 90);
        //Avalon Trader
        registerEntityWithID("avalon", EntityAvalon.class, ENTITY_START_ID++, 90, avalon_boss);
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
        registerEntityWithID("lidoped", EntityLidoped.class, ENTITY_START_ID++, 100, lidoped);
        //Mad Spirit
        registerEntityWithID("mad_spirit", EntityMadSpirit.class, ENTITY_START_ID++, 100, mad_spirit);
        //Void Tripod
        registerEntityWithID("void_tripod", EntityVoidTripod.class, ENTITY_START_ID++, 110, barrend_walker);
        //Big Rick
        registerEntityWithID("big_rick", EntityUltraParasite.class, ENTITY_START_ID++, 110, evoled_parasite);
        //Move TIle
        registerEntity("move_tile_aoe", EntityMoveTile.class, ENTITY_START_ID++, 110);
        //Parasite Projectile Bomb
        registerEntity("parasite_bomb", ProjectileParasiteBomb.class, ENTITY_START_ID++, 110);
        //Parasite Bomb AOE
        registerEntity("parasite_bomb_aoe", EntityParasiteBombAOE.class, ENTITY_START_ID++, 110);


    }

    public static void RegisterEntitySpawns() {
        spawnRate(EntityController.class, EnumCreatureType.MONSTER, MobConfig.guilder_spawn_rate, 1, 2, BiomeDictionary.Type.END);
        spawnRateBiomeSpecific(EntityEndBug.class, EnumCreatureType.MONSTER, MobConfig.parasite_spawn_rate, 1, 4, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntitySnatcher.class, EnumCreatureType.MONSTER, MobConfig.stalker_spawn_rate, 1, 1, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntityChomper.class, EnumCreatureType.MONSTER, MobConfig.chomper_spawn_rate, 1, 3, BiomeRegister.END_ASH_WASTELANDS);
        spawnRateBiomeSpecific(EntityLidoped.class, EnumCreatureType.MONSTER, 3, 1, 2, BiomeRegister.BARREND_LOWLANDS);
        //spawnRateBiomeSpecific(EntityEndBug.class, EnumCreatureType.MONSTER, 2, 1, 2, BiomeRegister.BARREND_LOWLANDS);
        spawnRateBiomeSpecific(EntityMadSpirit.class, EnumCreatureType.MONSTER, 2, 1, 2, BiomeRegister.BARREND_LOWLANDS);
        spawnRateBiomeSpecific(EntityVoidTripod.class, EnumCreatureType.MONSTER, 2, 1, 1, BiomeRegister.BARREND_LOWLANDS);
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
