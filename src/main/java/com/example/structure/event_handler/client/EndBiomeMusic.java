package com.example.structure.event_handler.client;


import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class EndBiomeMusic implements ITickable {

    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic, currentRecord, menuMusic, minecraftMusic;
    private int timeUntilNextMusic = 100;

    public EndBiomeMusic(Minecraft mcIn) {
        this.mc = mcIn;
    }
    @Override
    public void update() {
        TrackType tracktype = TrackType.TRACK_ONE;

        if (this.mc.player != null)
        {
            if (this.mc.player.dimension != 1 && this.mc.player.world.getBiomeForCoordsBody(this.mc.player.getPosition()) != BiomeRegister.END_ASH_WASTELANDS)
            {
                this.stopMusic();
            }
            else {
                if (this.currentMusic != null)
                {
                    if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
                    {
                        this.currentMusic = null;
                        this.timeUntilNextMusic = Math.min(MathHelper.getInt(this.rand, tracktype.getMinDelay(), tracktype.getMaxDelay()), this.timeUntilNextMusic);
                    }
                }

                this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, tracktype.getMaxDelay());

                if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0 && this.mc.player.world.getBiomeForCoordsBody(this.mc.player.getPosition()) == BiomeRegister.END_ASH_WASTELANDS)
                {
                    this.playMusic(tracktype);
                    System.out.println("Playing Music from Mod");
                }
            }
        }

        if (!this.mc.getSoundHandler().isSoundPlaying(this.menuMusic))
        {
            this.menuMusic = null;
        }
    }


    public boolean playingMusic()
    {
        return this.currentMusic != null;
    }

    public void stopMinecraftMusic()
    {
        if (this.minecraftMusic != null)
        {
            this.mc.getSoundHandler().stopSound(this.minecraftMusic);
            this.minecraftMusic = null;
        }
    }

    public void trackRecord(ISound record)
    {
        this.currentRecord = record;
    }

    public ISound getRecord()
    {
        return this.currentRecord;
    }

    public boolean playingRecord()
    {
        return this.currentRecord != null;
    }

    public boolean playingMenuMusic()
    {
        return this.menuMusic != null;
    }

    public boolean playingMinecraftMusic()
    {
        return this.minecraftMusic != null;
    }

    public void stopMusic()
    {
        if (this.currentMusic != null)
        {
            this.mc.getSoundHandler().stopSound(this.currentMusic);
            this.currentMusic = null;
            this.timeUntilNextMusic = 0;
        }
    }

    public void playMusic(TrackType requestedMusicType)
    {
        this.currentMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
        this.mc.getSoundHandler().playSound(this.currentMusic);
        this.timeUntilNextMusic = Integer.MAX_VALUE;
    }


    @SideOnly(Side.CLIENT)
    public static enum TrackType
    {
        TRACK_ONE(ModSoundHandler.BIOME_MUSIC, 1200, 15000);

        private final SoundEvent musicLocation;
        private final int minDelay;
        private final int maxDelay;

        private TrackType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn)
        {
            this.musicLocation = musicLocationIn;
            this.minDelay = minDelayIn;
            this.maxDelay = maxDelayIn;
        }

        public SoundEvent getMusicLocation()
        {
            return this.musicLocation;
        }

        public int getMinDelay()
        {
            return this.minDelay;
        }

        public int getMaxDelay()
        {
            return this.maxDelay;
        }
    }
}
