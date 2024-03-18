package com.example.structure.event_handler;

import com.example.structure.items.DurableShield;
import com.example.structure.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID, value = Side.CLIENT)
public class ClientEvents {

    private static boolean screenShakePositive = true;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(ClientRender.SCREEN_SHAKE>0) {
            event.player.rotationPitch += ClientRender.getScreenShake(screenShakePositive);
            screenShakePositive = !screenShakePositive;
            ClientRender.SCREEN_SHAKE-=0.01f;
        }
    }


}
