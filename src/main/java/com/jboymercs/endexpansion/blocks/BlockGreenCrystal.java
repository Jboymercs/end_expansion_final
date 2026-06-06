package com.jboymercs.endexpansion.blocks;

import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.entity.tileentity.TileEntityUpdater;
import com.jboymercs.endexpansion.init.ModPotions;
import com.jboymercs.endexpansion.util.handlers.EESoundTypes;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockGreenCrystal extends BlockBase implements IBlockUpdater, ITileEntityProvider {

    int counter = 0;
    private Item itemDropped;
    public BlockGreenCrystal(String name, Material material, Item item) {
        super(name, material);
        this.itemDropped = item;
        this.setSoundType(EESoundTypes.CRYSTAL_GREEN);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }


    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    assert player != null;
                    return player.isPotionActive(ModPotions.MADNESS);
                }
            });
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpdater();
    }
}
