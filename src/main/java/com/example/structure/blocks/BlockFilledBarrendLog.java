package com.example.structure.blocks;

import com.example.structure.entity.tileentity.TileEntityUpdater;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModRand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFilledBarrendLog extends BlockLogBase implements IBlockUpdater, ITileEntityProvider {

    private Item droppedItem;

    public BlockFilledBarrendLog(String name, float hardness, float resistance, SoundType soundType, Item droppedItem) {
        super(name, hardness, resistance, soundType);
        this.droppedItem = droppedItem;
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
                                    float hitZ) {
        if(worldIn == null) {
            return false;
        }
        if(!worldIn.isRemote) {
            EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, new ItemStack(droppedItem, ModRand.range(1,2)));
            worldIn.spawnEntity(item);
            worldIn.setBlockState(pos, ModBlocks.BARE_BARK_HOLE.getDefaultState());
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void update(World world, BlockPos pos) {

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpdater();
    }
}
