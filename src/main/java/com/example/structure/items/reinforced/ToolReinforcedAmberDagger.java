package com.example.structure.items.reinforced;

import com.example.structure.config.ItemConfig;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.items.tools.ToolSword;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
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

public class ToolReinforcedAmberDagger extends ToolSword {
    private String info_loc;

    public ToolReinforcedAmberDagger(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        this.info_loc = info_loc;
        this.setMaxDamage(250);
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = (ItemConfig.dagger_cooldown * 20) - 1;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 140, 1));
            worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.LORD_KNIGHT_FLY, SoundCategory.PLAYERS, 0.7F, 1.3F);
            Vec3d moveVec = player.getLookVec().scale(-((ItemConfig.sword_velocity * 0.5) + 0.1D));
            if(player.canBePushed()) {
                player.motionX = moveVec.x;
                player.motionY = moveVec.y * 0.5;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
            }
            stack.damageItem(1, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.1D, 0));
        }

        return multimap;
    }
}
