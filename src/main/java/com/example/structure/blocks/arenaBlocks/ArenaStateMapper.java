package com.example.structure.blocks.arenaBlocks;

import com.deeperdepths.common.Constants;
import com.deeperdepths.common.blocks.BlockTrialSpawner;
import com.example.structure.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class ArenaStateMapper extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("ee:" + (((BlockUnEndingArena)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockUnEndingArena.STATE).getName());
    }
}
