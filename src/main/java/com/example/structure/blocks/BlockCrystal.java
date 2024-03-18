package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.IHasModel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrystal extends BlockBase {
    private Item itemDropped;
    public BlockCrystal(String name, Material material, Item item) {
        super(name, material);
        this.itemDropped = item;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }


    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if(rand.nextInt(2) == 0) {
            return itemDropped;
        }
        return null;
    }
}
