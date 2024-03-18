package com.example.structure.items;

import com.example.structure.Main;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemModBook extends ItemBase{

    private String info_loc;
    public ItemModBook(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(worldIn.isRemote){
            Main.proxy.openGuiBook(stack, player);
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
