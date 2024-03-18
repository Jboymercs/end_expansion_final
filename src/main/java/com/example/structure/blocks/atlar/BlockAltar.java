package com.example.structure.blocks.atlar;

import com.example.structure.Main;
import com.example.structure.blocks.BlockBase;
import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.init.ModItems;
import com.example.structure.proxy.CommonProxy;
import com.example.structure.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.sound.midi.SysexMessage;

public class BlockAltar extends Block implements ITileEntityProvider, IHasModel {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public BlockAltar() {
        super(Material.ROCK);
        this.setBlockUnbreakable();
        setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    private TileEntityAltar getTEA(World world, BlockPos pos) {
        return
                (TileEntityAltar) world.getTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityAltar altar = getTEA(world, pos);

        if(!world.isRemote) {
          player.openGui(Main.instance, CommonProxy.GUI_ALTAR, world, pos.getX(), pos.getY(), pos.getZ());

        }
        return true;

        }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityAltar();
    }


    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        super.breakBlock(world, pos, state);
    }


    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
