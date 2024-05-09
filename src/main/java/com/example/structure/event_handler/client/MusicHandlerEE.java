package com.example.structure.event_handler.client;

import com.example.structure.util.handlers.BiomeRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MusicHandlerEE {

    private Minecraft mc = Minecraft.getMinecraft();

    private EndBiomeMusic musicTicker = new EndBiomeMusic(mc);

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) throws Exception
    {
        TickEvent.Phase phase = event.phase;
        TickEvent.Type type = event.type;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (phase == TickEvent.Phase.END && type.equals(TickEvent.Type.CLIENT) && !mc.isGamePaused() && !musicTicker.playingRecord())
        {
            musicTicker.update();
        }

        if(musicTicker.playingMinecraftMusic() && mc.player.world.getBiomeForCoordsBody(mc.player.getPosition()) == BiomeRegister.END_ASH_WASTELANDS) {
            System.out.println("Stopping Minecraft Music");
            musicTicker.stopMinecraftMusic();
        }

        if (musicTicker.playingRecord() && !(mc.getSoundHandler().isSoundPlaying(musicTicker.getRecord())))
        {
            musicTicker.trackRecord(null);
        }

        if (Minecraft.getMinecraft().world == null && !(screen instanceof GuiScreenWorking))
        {

            if (musicTicker.playingMinecraftMusic())
            {
                System.out.println("Stopping Minecraft Music");
                musicTicker.stopMinecraftMusic();
            }
        }
    }
}
