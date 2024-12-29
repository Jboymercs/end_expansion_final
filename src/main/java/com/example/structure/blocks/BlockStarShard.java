package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockStarShard extends BlockOre implements IHasModel {

    public BlockStarShard(String name) {
        super();
        setTranslationKey(name);
        setRegistryName(name);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockStarShard(String name, float hardness, float resistance, SoundType soundType) {
        this(name);
        setHardness(hardness);
        setResistance(resistance);
        this.setSoundType(soundType);
        this.setCreativeTab(ModCreativeTabs.ITEMS);

    }


    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();

        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            int i = 0;

            if (this == ModBlocks.AMBER_ORE) {
                i = MathHelper.getInt(rand, 3, 7);
            }

            return i;
        }
        return 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.STAR_SHARD_RAW;
    }



    /** Used for Overlay texture. */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    { return BlockRenderLayer.CUTOUT_MIPPED; }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
