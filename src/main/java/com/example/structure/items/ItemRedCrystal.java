package com.example.structure.items;

import com.example.structure.blocks.BlockDoorActivation;
import com.example.structure.entity.tileentity.TileEntityActivate;
import com.example.structure.entity.tileentity.TileEntityDeactivate;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;


public class ItemRedCrystal extends ItemBase{

    private String info_loc;


    public ItemRedCrystal(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

                if(player.getHeldItem(hand).getMetadata() == 1 && worldIn.getBlockState(pos).getBlock().equals(ModBlocks.END_ASH_DOOR)) {
                    if(player.getHeldItem(hand).getItem() == this) {
                        TileEntity tileEntity = worldIn.getTileEntity(pos);
                        if(tileEntity instanceof TileEntityActivate) {
                            player.getHeldItem(hand).shrink(1);
                            return EnumActionResult.SUCCESS;
                        }
                    }
                }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

}
