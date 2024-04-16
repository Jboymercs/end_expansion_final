package com.example.structure.world.Biome;

import net.minecraft.world.biome.Biome;

public class BiomeFogged extends Biome {

    private int[] fogColorRGB = new int[]{(int) 255, (int) 255, (int) 255};
    public BiomeFogged(BiomeProperties properties) {
        super(properties);
        this.setFogColor(10, 30, 22);
    }

    /**
     * Sets the biome fog color
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public final BiomeFogged setFogColor(int red, int green, int blue) {
        this.fogColorRGB[0] = red;
        this.fogColorRGB[1] = green;
        this.fogColorRGB[2] = blue;
        return this;
    }


}
