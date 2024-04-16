package com.example.structure.event_handler;

import com.example.structure.util.ModReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

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
