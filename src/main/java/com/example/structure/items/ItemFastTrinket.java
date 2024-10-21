package com.example.structure.items;

import com.example.structure.config.ItemConfig;
import com.example.structure.entity.trader.action.player.ActionShortAOE;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemFastTrinket extends ItemBase{

    private String info_loc;

    public ItemFastTrinket(String name, CreativeTabs tab, String info_loc) {
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 6 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            Vec3d moveVec = player.getLookVec().scale(-((1.1 * 0.5) + 0.1D));
            if(player.canBePushed()) {
                new ActionShortAOE().performAction(player, player);
                player.motionX = moveVec.x;
                player.motionY = moveVec.y * 0.5;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
            }
            stack.damageItem(2, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
