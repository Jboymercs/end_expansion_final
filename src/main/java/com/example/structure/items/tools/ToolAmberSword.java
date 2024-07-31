package com.example.structure.items.tools;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntitySwordSpike;
import com.example.structure.init.ModCreativeTabs;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.w3c.dom.Entity;

import java.util.List;

public class ToolAmberSword extends ToolSword{

    private String info_loc;


    public ToolAmberSword(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        this.setMaxDamage(550);
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        boolean hasDamagedEntities = false;
        int howManyEntities = 0;
        ItemStack stack = player.getHeldItem(hand);

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            List<EntityLivingBase> targets = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(6D), e -> !e.getIsInvulnerable() && (!(e == player)));
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, SoundCategory.PLAYERS, 0.7F, 0.7F);
            if(!targets.isEmpty()) {
                if(ModConfig.enable_pvp_abilities) {
                    for (EntityLivingBase base : targets) {
                        howManyEntities++;
                        hasDamagedEntities = true;
                        Vec3d pos = base.getPositionVector();
                        EntitySwordSpike spike = new EntitySwordSpike(worldIn, player, base);
                        spike.setPosition(pos.x, pos.y, pos.z);
                        worldIn.spawnEntity(spike);
                    }
                } else {
                    for (EntityLivingBase base : targets) {
                        //Makes sure to not target players if the setting for pvp is disabled
                        if(!(base instanceof EntityPlayer)){
                            howManyEntities++;
                            hasDamagedEntities = true;
                            Vec3d pos = base.getPositionVector();
                            EntitySwordSpike spike = new EntitySwordSpike(worldIn, player, base);
                            spike.setPosition(pos.x, pos.y, pos.z);
                            worldIn.spawnEntity(spike);
                        }
                    }
                }
            }

            int SwordCoolDown = (hasDamagedEntities) ? (howManyEntities + 8) * 20 : 60;
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(howManyEntities * 2, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
