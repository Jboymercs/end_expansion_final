package com.example.structure.world.api.barrend_crypts;

import com.example.structure.config.WorldConfig;
import com.example.structure.util.ModRand;
import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class BarrendCryptTemplate extends ModStructureTemplate {


    public BarrendCryptTemplate() {

    }

    public BarrendCryptTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {

    }



    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.vault_mob_chance) {
            return false;
        }
        return true;
    }
    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= WorldConfig.vault_loot_chance) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "barrendcrypt";
    }
}
