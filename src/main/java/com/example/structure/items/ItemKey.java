package com.example.structure.items;

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

public class ItemKey extends ItemBase{
    private String info_loc;

    protected boolean isEndFortress;
    public ItemKey(String name, String info_loc, CreativeTabs tab, boolean isEndFortress) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.isEndFortress = isEndFortress;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(this.isEndFortress) {
            tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
        } else {
            tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
        }

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);





        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
