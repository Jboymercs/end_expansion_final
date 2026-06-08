package com.jboymercs.endexpansion.blocks;

import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.util.handlers.EESoundTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrystal extends BlockBase {
    private Item itemDropped;
    public BlockCrystal(String name, Material material, Item item) {
        super(name, material);
        this.itemDropped = item;
        this.setSoundType(EESoundTypes.CRYSTAL_PURPLE);
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
