package com.example.structure.blocks;

import com.example.structure.entity.tileentity.TileEntityDeactivate;
import com.example.structure.entity.tileentity.TileEntityUpdater;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDoorDeactivation extends BlockBase implements IBlockUpdater, ITileEntityProvider {
    int counter = 0;

    private Item activationItem;
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockDoorDeactivation(String name, Material material, Item item) {
        super(name, material);
        this.setBlockUnbreakable();
        this.hasTileEntity = true;
        this.activationItem = item;
        this.setSoundType(SoundType.STONE);
    }



    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Nullable
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDeactivate();
    }


    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
                }
            });

        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);

        worldIn.removeTileEntity(pos);

    }



    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
                                    float hitZ) {

        if (playerIn.getHeldItemMainhand().isEmpty()) {
            worldIn.setBlockState(pos, ModBlocks.END_ASH_DOOR.getDefaultState());

        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

}
