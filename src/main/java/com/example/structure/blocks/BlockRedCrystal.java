package com.example.structure.blocks;

import com.example.structure.Main;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRedCrystal extends BlockBase {
    private Item itemDropped;
    public BlockRedCrystal(String name, Material material, Item item) {
        super(name, material);
        this.itemDropped = item;
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(rand.nextInt(7) == 0) {
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundHandler.RED_CRYSTAL_HUM, SoundCategory.AMBIENT, 0.5f, 1.0f, true);
        }
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
