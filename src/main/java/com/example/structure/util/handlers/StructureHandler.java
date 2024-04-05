package com.example.structure.util.handlers;

import com.example.structure.Main;
import com.example.structure.world.Biome.altardungeon.AltarTemplate;
import com.example.structure.world.Biome.altardungeon.MapGenAltarDungeon;
import com.example.structure.world.Biome.bridgestructure.BridgeStructureTemplate;
import com.example.structure.world.Biome.bridgestructure.MapGenBridgeStructure;
import com.example.structure.world.api.ashtower.AshTowerTemplate;
import com.example.structure.world.api.ashtower.WorldGenAshTower;
import com.example.structure.world.api.structures.FortressTemplate;
import com.example.structure.world.api.structures.MapGenKingFortress;
import com.example.structure.world.api.vaults.EndVaults;
import com.example.structure.world.api.vaults.MapGenEndVaults;
import com.example.structure.world.api.vaults.VaultTemplate;
import com.example.structure.world.api.vaults.WorldGenEndVaults;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.structure.INetherAPIStructureEntry;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import git.jbredwards.nether_api.mod.common.registry.NetherAPIRegistry;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import javax.annotation.Nonnull;

public class StructureHandler {

    public static void handleStructureRegistries(){

        MapGenStructureIO.registerStructure(MapGenKingFortress.Start.class, "EndKingsFortress");
        MapGenStructureIO.registerStructureComponent(FortressTemplate.class, "EFP");
        MapGenStructureIO.registerStructure(WorldGenEndVaults.Start.class, "EndVaults");
        MapGenStructureIO.registerStructureComponent(VaultTemplate.class, "EVP");
        MapGenStructureIO.registerStructure(WorldGenAshTower.Start.class, "AshTowers");
        MapGenStructureIO.registerStructureComponent(AshTowerTemplate.class, "ATP");

    }
}
