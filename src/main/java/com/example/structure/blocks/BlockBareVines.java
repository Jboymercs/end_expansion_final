package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import com.example.structure.util.data.AdvancedStateMap;
import com.example.structure.util.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBareVines extends BlockBush implements IHasModel, RegistryHandler.IStateMappedBlock{
    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);
    public static final PropertyBool IS_TOP = PropertyBool.create("is_top");
    public static final PropertyBool IS_BOTTOM = PropertyBool.create("is_bottom");

    public static final PropertyBool CAN_GROW = PropertyBool.create("can_grow");


    public BlockBareVines(String name, Material materialIn) {
        super(materialIn);
        setTranslationKey(name);
        setRegistryName(name);
        setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        this.setDefaultState(this.blockState.getBaseState().withProperty(IS_TOP, true).withProperty(IS_BOTTOM, false).withProperty(CAN_GROW, true));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{IS_TOP, IS_BOTTOM, CAN_GROW});
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean isTop = worldIn.getBlockState(pos.up()).getBlock() != this;
        boolean isBottom = worldIn.getBlockState(pos.down()).getBlock() != this;
        return state.withProperty(IS_TOP, isTop).withProperty(IS_BOTTOM, isBottom);
    }

    @Override
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    protected boolean isValidBlock(World world, BlockPos pos, IBlockState blockState) {
        return blockState.isSideSolid(world, pos, EnumFacing.DOWN) || blockState.getBlock() == this || blockState.getBlock() == ModBlocks.BARE_LEAVES;
    }


    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return isValidBlock(world, pos.up(), world.getBlockState(pos.up())) && canBlockStay(world, pos, world.getBlockState(pos));
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return isValidBlock(worldIn, pos.up(), worldIn.getBlockState(pos.up()));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!isValidBlock(worldIn, pos.up(), worldIn.getBlockState(pos.up()))) {
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(rand.nextInt(24) == 0 && worldIn.isAirBlock(pos.down()) && this.canGrowAt(worldIn, pos, state)) {
            worldIn.setBlockState(pos.down(), this.getDefaultState());
        }
    }

    protected boolean canGrowAt(World world, BlockPos pos, IBlockState state) {
        return state.getValue(CAN_GROW);
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == ModBlocks.BARE_LEAVES;
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(CAN_GROW) ? 1 : 0;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(CAN_GROW, true);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CAN_GROW, meta == 1);
    }

    @Override
    public void setStateMapper(AdvancedStateMap.Builder builder) {
        builder.ignore(CAN_GROW);
    }
}
