package com.example.structure.util.handlers;


import com.example.structure.world.api.ashtower.AshTowerTemplate;
import com.example.structure.world.api.ashtower.WorldGenAshTower;
import com.example.structure.world.api.mines.MinesTemplate;
import com.example.structure.world.api.mines.WorldGenMines;
import com.example.structure.world.api.structures.FortressTemplate;
import com.example.structure.world.api.structures.MapGenKingFortress;
import com.example.structure.world.api.vaults.VaultTemplate;
import com.example.structure.world.api.vaults.WorldGenEndVaults;
import net.minecraft.world.gen.structure.MapGenStructureIO;



public class StructureHandler {

    public static void handleStructureRegistries(){

        MapGenStructureIO.registerStructure(MapGenKingFortress.Start.class, "EndKingsFortress");
        MapGenStructureIO.registerStructureComponent(FortressTemplate.class, "EFP");
        MapGenStructureIO.registerStructure(WorldGenEndVaults.Start.class, "EndVaults");
        MapGenStructureIO.registerStructureComponent(VaultTemplate.class, "EVP");
        MapGenStructureIO.registerStructure(WorldGenAshTower.Start.class, "AshTowers");
        MapGenStructureIO.registerStructureComponent(AshTowerTemplate.class, "ATP");
        MapGenStructureIO.registerStructure(WorldGenMines.Start.class, "AshedMines");
        MapGenStructureIO.registerStructureComponent(MinesTemplate.class, "AMP");

    }
}
