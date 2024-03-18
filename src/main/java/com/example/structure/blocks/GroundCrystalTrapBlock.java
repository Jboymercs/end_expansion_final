package com.example.structure.blocks;

import com.example.structure.entity.tileentity.TileEntityDoorStart;
import com.example.structure.entity.tileentity.TileEntityTrap;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GroundCrystalTrapBlock extends BlockBase {


    public GroundCrystalTrapBlock(String name, Material material) {
        super(name, material);
        hasTileEntity = true;
        this.blockSoundType = SoundType.STONE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTrap();
    }
}
