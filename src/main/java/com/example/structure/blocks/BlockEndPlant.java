package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockEndPlant extends BlockBush implements IHasModel {
    /**
     * Plant for the End Biome
     */

    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);
    private Item itemDropped;

    public BlockEndPlant(String name, Item itemDropped) {
        super(Material.PLANTS);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        setSoundType(SoundType.PLANT);
        this.itemDropped = itemDropped;
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
        if(rand.nextInt(2)==0) {
            return itemDropped;
        }
        return null;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == ModBlocks.BARE_SANS || state.getBlock() == Blocks.END_STONE;
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

}
