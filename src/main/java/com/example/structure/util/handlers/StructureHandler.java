package com.example.structure.util.handlers;


import com.example.structure.config.ModConfig;
import com.example.structure.event_handler.EventStronghold;
import com.example.structure.world.api.ashtower.AshTowerTemplate;
import com.example.structure.world.api.ashtower.WorldGenAshTower;
import com.example.structure.world.api.barrend_crypts.BarrendCryptTemplate;
import com.example.structure.world.api.barrend_crypts.WorldGenBarrendCrypt;
import com.example.structure.world.api.lamentedIslands.LamentedIslandsTemplate;
import com.example.structure.world.api.lamentedIslands.WorldGenLamentedIslands;
import com.example.structure.world.api.mines.MinesTemplate;
import com.example.structure.world.api.mines.WorldGenMines;
import com.example.structure.world.api.structures.FortressTemplate;
import com.example.structure.world.api.structures.MapGenKingFortress;
import com.example.structure.world.api.vaults.VaultTemplate;
import com.example.structure.world.api.vaults.WorldGenEndVaults;
import com.example.structure.world.stronghold.BetterStrongholdTemplate;
import com.example.structure.world.stronghold.MapGenBetterStronghold;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;


public class StructureHandler {

    public static void handleStructureRegistries(){
        //End King Fortress
        MapGenStructureIO.registerStructure(MapGenKingFortress.Start.class, "EndKingsFortress");
        MapGenStructureIO.registerStructureComponent(FortressTemplate.class, "EFP");
        //End Vaults
        MapGenStructureIO.registerStructure(WorldGenEndVaults.Start.class, "EndVaults");
        MapGenStructureIO.registerStructureComponent(VaultTemplate.class, "EVP");
        //Ashed Towers
        MapGenStructureIO.registerStructure(WorldGenAshTower.Start.class, "AshTowers");
        MapGenStructureIO.registerStructureComponent(AshTowerTemplate.class, "ATP");
        //Ashed Mines
        MapGenStructureIO.registerStructure(WorldGenMines.Start.class, "AshedMines");
        MapGenStructureIO.registerStructureComponent(MinesTemplate.class, "AMP");
        //Lamented Islands
        MapGenStructureIO.registerStructure(WorldGenLamentedIslands.Start.class, "LamentedIslands");
        MapGenStructureIO.registerStructureComponent(LamentedIslandsTemplate.class, "LIP");
        //Barrend Crypts
        if(ModConfig.dev_stuff_enabled) {
            MapGenStructureIO.registerStructure(WorldGenBarrendCrypt.Start.class, "BarrendCrypts");
            MapGenStructureIO.registerStructureComponent(BarrendCryptTemplate.class, "BCP");
        }
        //Replaced Stronghold
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventStronghold());
        MapGenStructureIO.registerStructure(MapGenBetterStronghold.Start.class, "BetterStronghold");
        MapGenStructureIO.registerStructureComponent(BetterStrongholdTemplate.class, "BSP");

    }
}
