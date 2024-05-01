package com.example.structure.blocks;

import com.example.structure.entity.tileentity.TileEntityReverse;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockReverseBlock extends BlockBase implements ITileEntityProvider, IBlockUpdater {


    public BlockReverseBlock(String name, Material material) {
        super(name, material);
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
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);

        worldIn.removeTileEntity(pos);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        neighborChanged(state, worldIn, pos, null, null);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityReverse();
    }

    @Override
    public void update(World world, BlockPos pos) {

    }
}
