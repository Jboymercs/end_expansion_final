package com.example.structure.event_handler;

import com.example.structure.event_handler.client.EndBiomeMusic;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID, value = Side.CLIENT)
public class ClientEvents {

    private static boolean screenShakePositive = true;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {


        if(ClientRender.SCREEN_SHAKE>0 && event.player != null) {
            event.player.rotationPitch += ClientRender.getScreenShake(screenShakePositive);
            screenShakePositive = !screenShakePositive;
            ClientRender.SCREEN_SHAKE-=0.01f;
        }
    }




}
