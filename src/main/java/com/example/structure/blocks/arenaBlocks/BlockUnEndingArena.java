package com.example.structure.blocks.arenaBlocks;

import com.example.structure.blocks.BlockBase;
import com.example.structure.blocks.IBlockUpdater;
import com.example.structure.entity.tileentity.TileEntityUnEndingArena;
import com.example.structure.init.ModItems;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockUnEndingArena extends BlockBase implements ITileEntityProvider, IBlockUpdater {

    public static final PropertyEnum<BlockEnumArenaStates> STATE = PropertyEnum.create("state", BlockEnumArenaStates.class);

    private Item activationItem;
    private Item activationItem2;
    private Item activationItem3;
    private Item activationItem4;


    int counter = 0;

    public BlockUnEndingArena(String name, Material material) {
        super(name, material);
        this.setBlockUnbreakable();

    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityUnEndingArena)) return state;
        TileEntityUnEndingArena spawner = (TileEntityUnEndingArena) te;
        return state.withProperty(STATE, spawner.getState());
    }

    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem || player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem2 || player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem3 || player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem4;
                }
            });

        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        //change this to activate on inserting an item
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityUnEndingArena) {
            TileEntityUnEndingArena spawner = (TileEntityUnEndingArena) te;
            if(spawner.getState() != BlockEnumArenaStates.ACTIVE) {
                if (player.getHeldItemMainhand().getItem() == this.activationItem) {
                    spawner.setState(BlockEnumArenaStates.ACTIVE, 1, 1.25);
                    player.getHeldItemMainhand().shrink(1);
                    return true;
                } else if (player.getHeldItemMainhand().getItem() == this.activationItem2) {
                    spawner.setState(BlockEnumArenaStates.ACTIVE, 2, 1.5);
                    player.getHeldItemMainhand().shrink(1);
                    return true;
                } else if (player.getHeldItemMainhand().getItem() == this.activationItem3) {
                    spawner.setState(BlockEnumArenaStates.ACTIVE, 3, 1.25);
                    player.getHeldItemMainhand().shrink(1);
                    return true;
                } else if (player.getHeldItemMainhand().getItem() == this.activationItem4) {
                    spawner.setState(BlockEnumArenaStates.ACTIVE, 4, 1.5);
                    player.getHeldItemMainhand().shrink(1);
                    return true;
                }
            }

        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(STATE).getLightLevel();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE);
    }



    @Nullable
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityUnEndingArena();
    }


    public String byState(IBlockState state) {
        return  "unending_arena";
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }


    public BlockUnEndingArena(String name, Material material, float hardness, float resistance, SoundType soundType, Item item1, Item item2, Item item3, Item item4) {
        super(name, material, hardness, resistance, soundType);
        this.activationItem = item1;
        this.activationItem2 = item2;
        this.activationItem3 = item3;
        this.activationItem4 = item4;
        this.setBlockUnbreakable();
    }
}
