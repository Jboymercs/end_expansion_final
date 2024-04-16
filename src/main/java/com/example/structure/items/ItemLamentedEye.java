package com.example.structure.items;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;


public class ItemLamentedEye extends ItemBase{
    private String info_loc;

    public ItemLamentedEye(String name,String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxDamage(75);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int EyeCooldown = ModConfig.eye_cooldown * 20;
    if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
        player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 80, 3));
        stack.damageItem(1, player);
        worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.BOSS_CAST_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        player.getCooldownTracker().setCooldown(this, EyeCooldown);
        player.swingArm(hand);
    }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
