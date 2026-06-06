package com.jboymercs.endexpansion.blocks.arenaBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class ArenaStateMapper extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("endexpansion:" + (((BlockUnEndingArena)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockUnEndingArena.STATE).getName());
    }
}
