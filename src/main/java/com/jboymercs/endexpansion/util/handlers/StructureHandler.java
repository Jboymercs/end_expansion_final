package com.jboymercs.endexpansion.util.handlers;


import com.jboymercs.endexpansion.event_handler.EventStronghold;
import com.jboymercs.endexpansion.world.api.ashtower.AshTowerTemplate;
import com.jboymercs.endexpansion.world.api.ashtower.WorldGenAshTower;
import com.jboymercs.endexpansion.world.api.barrend_crypts.BarrendCryptTemplate;
import com.jboymercs.endexpansion.world.api.barrend_crypts.WorldGenBarrendCrypt;
import com.jboymercs.endexpansion.world.api.lamentedIslands.LamentedIslandsTemplate;
import com.jboymercs.endexpansion.world.api.lamentedIslands.WorldGenLamentedIslands;
import com.jboymercs.endexpansion.world.api.mines.MinesTemplate;
import com.jboymercs.endexpansion.world.api.mines.WorldGenMines;
import com.jboymercs.endexpansion.world.api.structures.FortressTemplate;
import com.jboymercs.endexpansion.world.api.structures.MapGenKingFortress;
import com.jboymercs.endexpansion.world.api.vaults.VaultTemplate;
import com.jboymercs.endexpansion.world.api.vaults.WorldGenEndVaults;
import com.jboymercs.endexpansion.world.stronghold.BetterStrongholdTemplate;
import com.jboymercs.endexpansion.world.stronghold.MapGenBetterStronghold;
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

        MapGenStructureIO.registerStructure(WorldGenBarrendCrypt.Start.class, "BarrendCrypts");
        MapGenStructureIO.registerStructureComponent(BarrendCryptTemplate.class, "BCP");

        //Replaced Stronghold
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventStronghold());
        MapGenStructureIO.registerStructure(MapGenBetterStronghold.Start.class, "BetterStronghold");
        MapGenStructureIO.registerStructureComponent(BetterStrongholdTemplate.class, "BSP");

    }
}
