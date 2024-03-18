package com.example.structure.event_handler;

import com.example.structure.entity.EntityModBase;
import com.example.structure.items.tools.ISweepAttackOverride;
import com.example.structure.util.ModIndirectDamage;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * Holds various important functionalities only accessible through the forge event system
 */
@Mod.EventBusSubscriber()
public class EntityEventHandler
{

    @SubscribeEvent
    public static void afterShieldAndBeforeArmor(LivingHurtEvent event) {
        if(event.getSource() instanceof ModIndirectDamage) {
            ModIndirectDamage damageSource = ((ModIndirectDamage)event.getSource());
            if(damageSource.getStoppedByArmor()) {
                damageSource.isUnblockable = false;
            }

            if(damageSource.getDisablesShields() && event.getEntityLiving() != null && ModUtils.canBlockDamageSource(damageSource, event.getEntityLiving()) && event.getEntityLiving() instanceof EntityPlayer) {
                ((EntityPlayer)event.getEntityLiving()).disableShield(true);
            }
        }

        float damage = event.getAmount();
        // Factor in elemental armor first

        // Factor in maelstrom armor second


        event.setAmount(damage);
    }



}
