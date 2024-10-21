package com.example.structure.items;

import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemBoldTrinket extends ItemBase {
    private String info_loc;

    public ItemBoldTrinket(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxStackSize(1);
        this.setMaxDamage(200);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 12 * 20;
        if (!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            player.hurtResistantTime = 40;
            stack.damageItem(2, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

}
