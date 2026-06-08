package com.jboymercs.endexpansion.blocks;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModBlocks;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCoriumOre extends BlockOre implements IHasModel {

    public BlockCoriumOre(String name) {
        super();
        setTranslationKey(ModReference.MOD_ID + "." + name);
        setRegistryName(name);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockCoriumOre(String name, float hardness, float resistance, SoundType soundType) {
        this(name);
        setHardness(hardness);
        setResistance(resistance);
        this.setSoundType(soundType);

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
            return ModItems.AMBER_RAW_ORE;
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
