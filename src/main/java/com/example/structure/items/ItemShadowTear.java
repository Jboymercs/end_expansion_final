package com.example.structure.items;

import com.example.structure.config.ItemConfig;
import com.example.structure.entity.EntityLamentedEye;
import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemShadowTear extends ItemBase{

    private String info_loc;

    private boolean isSecondTier;
    public ItemShadowTear(String name, CreativeTabs tab, String info_loc, boolean isSecondTier) {
        super(name, tab);
        this.info_loc = info_loc;
        this.isSecondTier = isSecondTier;
        this.setMaxStackSize(1);
        this.setMaxDamage(9);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ItemConfig.shadow_tear_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            List<EntityShadowPlayer> nearbyPlayers = player.world.getEntitiesWithinAABB(EntityShadowPlayer.class, player.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());
            if(nearbyPlayers.isEmpty()) {
                double health = this.isSecondTier ? ItemConfig.shadow_tear_health * ItemConfig.shadow_tear_upgrade : ItemConfig.shadow_tear_health;
                double attackDamage = this.isSecondTier ? ItemConfig.shadow_tear_attack_damage * ItemConfig.shadow_tear_upgrade : ItemConfig.shadow_tear_attack_damage;
                EntityShadowPlayer shadowPlayer = new EntityShadowPlayer(worldIn, 3, health, attackDamage);
                shadowPlayer.setOwnerId(player.getUniqueID());
                shadowPlayer.onSummonViaPlayer(player.getPosition(), player);
                player.world.spawnEntity(shadowPlayer);
                //play elden ring use summon bell sound
                worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.SHADOW_TEAR_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            } else {
                player.sendStatusMessage(new TextComponentTranslation("ee.status.shadow_player", new Object[0]), true);
            }
        }
        stack.damageItem(1, player);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }
}
