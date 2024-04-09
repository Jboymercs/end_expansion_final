package com.example.structure.items;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityLamentedEye;
import com.example.structure.entity.endking.friendly.EntityFriendKing;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemKingCrown extends ItemBase {

    private String info_loc;

    public ItemKingCrown(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxDamage(1);
        this.setMaxStackSize(1);
    }



    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + ModUtils.translateDesc(info_loc));
    }

    //Summons the Ghost of the King
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int crownCoolDown = ModConfig.crown_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            //Summons A ghost version of the End King
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.NEUTRAL, 1.5f, 1.0f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
            BlockPos posToo = new BlockPos(player.posX, player.posY, player.posZ);
            EntityFriendKing kingGhost = new EntityFriendKing(worldIn, player);
            kingGhost.setPosition(posToo.getX(), posToo.getY() + 1, posToo.getZ());
            worldIn.spawnEntity(kingGhost);
            player.getCooldownTracker().setCooldown(this, crownCoolDown);
        }
        stack.damageItem(10, player);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
