package com.example.structure.blocks;
import com.example.structure.entity.tileentity.TileEntityDoorStart;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDungeonDoor extends BlockBase implements IBlockUpdater, ITileEntityProvider{
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");


    public BlockDungeonDoor(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, Boolean.valueOf(false)));
    }

    public static void setTriggerState(World world, boolean value, IBlockState state, BlockPos pos) {
        world.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(value)));
    }


    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultDirection(worldIn, pos, state);
    }


    private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            boolean flag = worldIn.getBlockState(pos.north()).isFullBlock();
            boolean flag1 = worldIn.getBlockState(pos.south()).isFullBlock();

            if (enumfacing == EnumFacing.NORTH && flag && !flag1)
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag)
            {
                enumfacing = EnumFacing.NORTH;
            }
            else
            {
                boolean flag2 = worldIn.getBlockState(pos.west()).isFullBlock();
                boolean flag3 = worldIn.getBlockState(pos.east()).isFullBlock();

                if (enumfacing == EnumFacing.WEST && flag2 && !flag3)
                {
                    enumfacing = EnumFacing.EAST;
                }
                else if (enumfacing == EnumFacing.EAST && flag3 && !flag2)
                {
                    enumfacing = EnumFacing.WEST;
                }
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(TRIGGERED, Boolean.valueOf(false)), 2);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up()) || worldIn.isBlockPowered(pos.down());
        boolean flag1 = state.getValue(TRIGGERED).booleanValue();

        if (!flag1 && flag) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)));
        } else if (flag1 && !flag) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(TRIGGERED, Boolean.valueOf(false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, TRIGGERED});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();

        if (state.getValue(TRIGGERED).booleanValue()) {
            i |= 8;
        }

        return i;
    }
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }


    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.byIndex(meta & 7);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void update(World world, BlockPos pos) {

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDoorStart();
    }
}
