package com.example.structure.items;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityLamentedEye;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemMedallion extends ItemBase{

    private String info_loc;


    public ItemMedallion(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxStackSize(1);
        this.setMaxDamage(50);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.medal_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            EntityLamentedEye eyeTooSummon = new EntityLamentedEye(worldIn, (float) player.posX,(float) player.posY, (float) player.posZ, player);
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.0F);
            eyeTooSummon.setPosition(player.posX, player.posY, player.posZ);
           worldIn.spawnEntity(eyeTooSummon);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        stack.damageItem(1, player);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
