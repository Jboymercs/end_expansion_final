package com.example.structure.init;

import com.example.structure.blocks.*;
import com.example.structure.blocks.arenaBlocks.BlockUnEndingArena;
import com.example.structure.blocks.barrend_dungeon.BlockBarrendDoor;
import com.example.structure.blocks.barrend_dungeon.BlockBarrendDungeonTile;
import com.example.structure.blocks.fluid.BlockBareAcid;
import com.example.structure.blocks.slab.BlockDoubleSlab;
import com.example.structure.blocks.slab.BlockHalfSlab;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityExplosion;
import com.example.structure.util.handlers.EESoundTypes;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.integration.ModIntegration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final float STONE_HARDNESS = 1.7f;
    public static final float STONE_RESISTANCE = 10f;
    public static final float BRICK_HARDNESS = 2.0f;
    public static final float WOOD_HARDNESS = 1.5f;
    public static final float WOOD_RESISTANCE = 5.0f;
    public static final float PLANTS_HARDNESS = 0.2f;
    public static final float PLANTS_RESISTANCE = 2.0f;
    public static final float ORE_HARDNESS = 3.0F;
    public static final float OBSIDIAN_HARDNESS = 50;
    public static final float OBSIDIAN_RESISTANCE = 2000;

//    public static final SoundType ASH = new SoundType(1.0F, 0.8F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GRAVEL_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
    public static final SoundType DOOR_CREATOR = new SoundType(1.0f, 0.9F, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, ModSoundHandler.DOOR_POWER_UP, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);

    //Lametend Islands Blocks
    public static final Block LAMENTED_END_STONE = new BlockBase("lamented_end_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_KEY_BLOCK = new BlockKey("key_block", ModItems.END_KEY, ((world, pos) -> new EntityExplosion(world, pos.getX(), pos.getY(), pos.getZ(), null, 1)));
    public static final Block ASH_KEY_BLOCK = new BlockAshKeyBlock("ash_key_block", ModItems.END_ASH_KEY,((world, pos) -> new EntityExplosion(world, pos.getX(), pos.getY(), pos.getZ(), null, 2)));
    public static final Block SHADOW_PLAYER = new BlockShadowKey("shadow_key_block", ModItems.END_ASH_KEY);
    public static final Block PURPLE_CRYSTAL = new BlockCrystal("purple_crystal", Material.ROCK, ModItems.PURPLE_CRYSTAL_CHUNK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block PURPLE_CRYSTAL_TOP = new BlockCrystalTopBase("purple_crystal_top", Material.ROCK, ModItems.PURPLE_CRYSTAL_CHUNK, EESoundTypes.CRYSTAL_PURPLE).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block GREEN_CRYSTAL = new BlockGreenCrystal("green_crystal", Material.ROCK, ModItems.GREEN_CRYSTAL_CHUNK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(1.0F);
    public static final Block GREEN_CRYSTAL_TOP = new BlockCrystalTopBase("green_crystal_top", Material.ROCK, ModItems.GREEN_CRYSTAL_CHUNK, EESoundTypes.CRYSTAL_GREEN).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(1.0F);
    //Ash Wastelands Blocks
    public static final Block RED_CRYSTAL = new BlockRedCrystal("red_crystal", Material.ROCK, ModItems.RED_CRYSTAL_CHUNK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(0.5f);
    public static final Block RED_CRYSTAL_TOP = new BlockCrystalTopBase("red_crystal_top", Material.ROCK, ModItems.RED_CRYSTAL_CHUNK, EESoundTypes.CRYSTAL_RED).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(0.5F);
    public static final Block END_ASH = new BlockAsh("end_ash", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block ASH_BRICK = new BlockBase("ash_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block ASH_BRICK_STAIRS = new BlockStairBase("ash_brick_stairs", ASH_BRICK.getDefaultState(), STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_STONE_STAIRS = new BlockStairBase("end_stone_stairs", Blocks.END_STONE.getDefaultState(), STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final BlockSlab ASH_BRICK_DOUBLE = new BlockDoubleSlab("ash_brick_double", Material.ROCK, ModCreativeTabs.ITEMS, ModBlocks.ASH_BRICK_HALF, STONE_HARDNESS, STONE_RESISTANCE,EESoundTypes.ASH_BRICK);
    public static final BlockSlab ASH_BRICK_HALF = new BlockHalfSlab("ash_brick_half", Material.ROCK, ModCreativeTabs.ITEMS, ModBlocks.ASH_BRICK_HALF, ModBlocks.ASH_BRICK_DOUBLE, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK);
    public static final BlockSlab END_STONE_DOUBLE = new BlockDoubleSlab("end_brick_double", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.END_STONE_HALF, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final BlockSlab END_STONE_HALF = new BlockHalfSlab("end_brick_half", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.END_STONE_HALF, ModBlocks.END_STONE_DOUBLE, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);

    public static final Block ASH_BRICK_WALL = new BlockModWall("ash_brick_wall", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK);
    public static final Block STONE_BRICK_WALL = new BlockModWall("stone_brick_wall", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final Block END_STONE_WALL = new BlockModWall("end_stone_wall", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final Block ASH_BRICK_PILLAR = new BlockPillarBase("ash_brick_pillar", Material.ROCK, EESoundTypes.ASH_BRICK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_ASH_CHISLE = new BlockBase("ash_chisle", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_ASH_SKULL = new BlockBase("ash_skull", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block RED_LAMP = new BlockLamp("red_lamp", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, EESoundTypes.ASH_BRICK).setLightLevel(1.0f).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block ASH_TRAP_FLOOR = new GroundCrystalTrapBlock("ash_trap_floor", Material.ROCK).setResistance(STONE_RESISTANCE).setHardness(STONE_HARDNESS).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block DOOR_ACTIVATOR = new BlockDungeonDoor("end_door", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_ASH_DOOR = new BlockDoorActivation("ash_door", ModItems.RED_CRYSTAL_ITEM);
    public static final Block DOOR_REVERSAL_OFF = new BlockReverseOff("reverse_off", Material.ROCK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block DOOR_REVERSAL_ON = new BlockReverseBlock("reverse_on", Material.ROCK).setHardness(STONE_HARDNESS);

    public static final Block POWER_SOURCE_OFF = new BlockNoSource("source_off", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block POWER_SOURCE_ON = new BlockPowerSource("source_on", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    //BrownStone - Ash Wastelands
    public static final Block BROWN_END_STONE = new BlockBase("brown_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block AMBER_ORE = new BlockCoriumOre("amber_ore", STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block AMBER_TORCH = new BlockAmberTorch("amber_torch", "desc_amber_torch").setLightLevel(1.0F);
    public static final Block EYED_OBSIDIEAN = new BlockBase("eyed_obi", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block SMOOTH_OBSIDIAN = new BlockBase("smooth_obi", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block OBSIDIAN_HEALTH_BLOCK = new BlockHealthCap("obi_health", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(1.0F);

    public static final Block BROWN_END_BRICK = new BlockBase("brown_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BROWN_SMOOTH_STONE = new BlockBase("brown_smooth", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block SPROUT_PLANT = new BlockPlantSprout("sprout_plant", Material.PLANTS).setLightLevel(0.5F);
    public static final Block SPROUT_VINE = new BlockDepthsVines("sprout_vine", Material.PLANTS).setLightLevel(0.5F);
    public static final Block BARE_VINE = new BlockBareVines("bare_vine", Material.VINE);
    public static final Block SPROUT_STONE = new BlockBase("sprout_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.PLANT).setCreativeTab(ModCreativeTabs.ITEMS);
    //Mini-nuke
    public static final Block BLOCK_MINI_NUKE = new BlockMiniNuke("mini_nuke", "nuke_desc");

    //End + Blocks and Utilities
    public static final Block END_PILLAR = new BlockBase("end_pillar", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_COMPULSOR = new BlockCompulsorOn("compulsor", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE, "compulsor_desc").setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block ASH_CHUTE = new BlockAshChute("ash_chute", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_HEAL_PLANT = new BlockEndPlant("end_h_plant", ModItems.HEAL_FOOD).setCreativeTab(ModCreativeTabs.ITEMS);
    //public static final Block ALTAR = new BlockAltar("altar", Material.ROCK).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block END_BARRIER = new BlockBarrier("end_barrier", Material.BARRIER);


    //Concept Work
    public static final Block BARE_SANS = new BlockBase("bare_sand", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.SAND).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE = new BlockBase("bare_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_COBBLED = new BlockBase("bare_stone_cobble", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_STAIRS = new BlockStairBase("bare_stairs", BARE_STONE.getDefaultState(), STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_EYE = new BlockBase("eye_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_EYE_LIT = new BlockBase("eye_stone_lit", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS).setCreativeTab(ModCreativeTabs.ITEMS).setLightLevel(1.0F);
    public static final Block BARE_WOOD_PLANKS = new BlockBase("bare_planks", Material.WOOD, WOOD_HARDNESS, STONE_RESISTANCE, SoundType.WOOD).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_WOOD_STAIRS = new BlockStairBase("bare_planks_stairs", BARE_WOOD_PLANKS.getDefaultState(), WOOD_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final BlockSlab BARE_WOOD_DOUBLE = new BlockDoubleSlab("bare_planks_double", Material.WOOD, ModCreativeTabs.ITEMS, ModBlocks.BARE_WOOD_HALF, WOOD_HARDNESS, STONE_RESISTANCE, SoundType.WOOD);
    public static final BlockSlab BARE_WOOD_HALF = new BlockHalfSlab("bare_planks_half", Material.WOOD, ModCreativeTabs.ITEMS, ModBlocks.BARE_WOOD_HALF, ModBlocks.BARE_WOOD_DOUBLE, WOOD_HARDNESS, STONE_RESISTANCE, SoundType.WOOD);
    public static final Block BARE_EYE_ARENA = new BlockUnEndingArena("unending_arena", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE, ModItems.ARENA_KEY_ONE, ModItems.ARENA_KEY_TWO, ModItems.ARENA_KEY_THREE, ModItems.ARENA_KEY_FOUR).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_SMOOTH = new BlockBase("bare_stone_smooth", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_STONE_WALL = new BlockModWall("bare_stone_wall", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BRICKS = new BlockBarrendDungeonTile("bare_bricks", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BRICKS_HOLE = new BlockBarrendDungeonTile("bare_bricks_hole", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BRICKS_CRACK = new BlockBarrendDungeonTile("bare_bricks_crack", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_SMALL_POT = new BlockSmallPot("bare_small_pot", Material.GRASS, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_DOOR = new BlockBarrendDoor("bare_door", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE, ModItems.MEMORIUM_STONE).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_DOOR_CONNECTOR = new BlockBase("bare_door_connect", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);


    public static final BlockSlab BARE_STONE_DOUBLE = new BlockDoubleSlab("bare_stone_double", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.BARE_STONE_HALF, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final BlockSlab BARE_STONE_HALF = new BlockHalfSlab("bare_stone_half", Material.ROCK, ModCreativeTabs.ITEMS, ModBlocks.BARE_STONE_HALF, ModBlocks.BARE_STONE_DOUBLE, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final Block BARE_PLANT = new BlockBarrendPlant("bare_plant", Material.GRASS).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_GRASS = new BlockBarrendPlant("bare_grass", Material.GRASS).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_ACID = new BlockBareAcid("bare_acid", ModFluid.ACID, Material.WATER).setLightLevel(0.75F);
    public static final Block BARE_LEAVES = new BlockBareLeaves("bare_leaves", PLANTS_HARDNESS, PLANTS_RESISTANCE, SoundType.PLANT).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BARK = new BlockLogBase("bare_bark", WOOD_HARDNESS, WOOD_RESISTANCE, EESoundTypes.BARREND_LOG).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BARK_HOLE = new BlockLogBase("bare_bark_hole", WOOD_HARDNESS, WOOD_RESISTANCE, EESoundTypes.BARREND_LOG).setCreativeTab(ModCreativeTabs.ITEMS);
    public static final Block BARE_BARK_HOLE_FILLED = new BlockFilledBarrendLog("bark_hole_filled", WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.SLIME, ModItems.BLOODWEED_REFINED).setCreativeTab(ModCreativeTabs.ITEMS);

    //Misc
    public static final Block MEGA_STRUCTURE_BLOCK = new BlockMegaStructure("mega_structure_block");
    public static final Block END_ASH_DOOR_ACTIVATE = new BlockDoorDeactivation("door_on", Material.ROCK, ModItems.RED_CRYSTAL_ITEM).setResistance(OBSIDIAN_RESISTANCE).setHardness(OBSIDIAN_HARDNESS);
    public static final Block LIGHTING_UPDATER = new BlockLightingUpdater("lighting_updater", Material.AIR).setLightLevel(0.1f);
    public static final Block DISAPPEARING_SPAWNER = new BlockDisappearingSpawner("disappearing_spawner", Material.ROCK);
    public static final Block DISAPPEARING_SPAWNER_ASH = new BlockDisappearingSpawner("ash_spawner", Material.ROCK);


}
