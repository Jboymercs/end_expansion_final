package com.example.structure.world.stronghold;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.world.WorldGenStructure;
import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class BetterStrongholdTemplate extends ModStructureTemplate {


    private WorldGenStructure[] structures = {new WorldGenStructure("stronghold/tile_1"), new WorldGenStructure("stronghold/tile_2"),
    new WorldGenStructure("stronghold/tile_3"), new WorldGenStructure("stronghold/tile_4"), new WorldGenStructure("stronghold/tile_5")};
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "stronghold_c");
    private static final ResourceLocation LOOT_LIBRARY = new ResourceLocation(ModReference.MOD_ID, "stronghold_l");
    private static final ResourceLocation LOOT_CROSSING = new ResourceLocation(ModReference.MOD_ID, "stronghold");

    private static final ResourceLocation BOOK = new ResourceLocation(ModReference.MOD_ID, "library_book");

    public BetterStrongholdTemplate() {

    }

    @Override
    public String templateLocation() {
        return "stronghold";
    }

    public BetterStrongholdTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {

        if(function.startsWith("book")) {
            BlockPos blockPos = pos.down();
            if(sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(BOOK, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }
        //chest spawns
        else if(function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn()  && sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }

        else if(function.startsWith("chest_cross")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn() && sbb.isVecInside(blockPos)) {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if(tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT_CROSSING, rand.nextLong());

                }
            }
        }
        //mini-structure spawns
        else if(function.startsWith("structure")) {
            WorldGenStructure piece = ModRand.choice(structures);
            piece.generate(world, rand, pos.add(-2, 0, -2));

        } //Custom Mob Spawners
        else if(function.startsWith("mob")) {
            if(!WorldConfig.mob_additions_stronghold) {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            } else if (world.rand.nextInt(3) == 0) {
                //Random chance for the spawner to not be in that location to vary up the Stronghold even more
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            } else {
                world.setBlockToAir(pos);
            }
        }
        //Misc setting blocks to air
        else if(function.startsWith("")) {
            world.setBlockToAir(pos);
        }
    }



    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= 2) {
            return false;
        }
        return true;
    }
}
