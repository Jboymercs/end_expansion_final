package com.jboymercs.endexpansion.blocks;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModBlocks;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;


public class BlockAmberTorch extends BlockTorch implements IHasModel {

    String info_loc;
    public BlockAmberTorch(String name, String info_loc) {
        super();
        this.setHardness(0.0F);
        this.setLightLevel(0);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        this.setTranslationKey(ModReference.MOD_ID + "." + name);
        this.setRegistryName(name);
        this.info_loc = info_loc;
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.YELLOW + ModUtils.translateDesc(info_loc));
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
