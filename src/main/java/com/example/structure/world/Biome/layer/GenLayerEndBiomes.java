package com.example.structure.world.Biome.layer;

import com.example.structure.Main;
import com.example.structure.util.handlers.BiomeRegister;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerEndBiomes extends GenLayer {
    private final int SKY_ID;
    private final int END_ASH_WASTELAND;

    private final int PLACEHOLDER;
    private final static int MAIN_ISLAND_SIZE;

    static
    {                                               //CHANGE TO INT FOR CONFIG
        MAIN_ISLAND_SIZE = (int) (80 / Math.pow(2, (6-1)));
//		SKY_ID = Biome.getIdForBiome(Biomes.SKY);
//		END_FOREST_ID = Biome.getIdForBiome(Biomes.MESA);
//		END_VOLCANO_ID = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
//		PLACEHOLDER = SKY_ID;
    }

    public GenLayerEndBiomes(long seed, GenLayer parent)
    {
        super(seed);
        this.parent = parent;
        SKY_ID = Biome.getIdForBiome(Biomes.SKY);
        END_ASH_WASTELAND = Biome.getIdForBiome(BiomeRegister.END_ASH_WASTELANDS);
        PLACEHOLDER = SKY_ID;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] inLayer = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] outLayer = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                int biomeInt = inLayer[j + i * areaWidth];

                if(biomeInt == 0 || (areaX < MAIN_ISLAND_SIZE && areaX > -MAIN_ISLAND_SIZE && areaY < MAIN_ISLAND_SIZE && areaY > -MAIN_ISLAND_SIZE))
                {
                    outLayer[j + i * areaWidth] = SKY_ID;
                }
                else if(biomeInt == 1)
                {
                    outLayer[j + i * areaWidth] = END_ASH_WASTELAND;
                }
                else if(biomeInt == 3)
                {
                    outLayer[j + i * areaWidth] =  PLACEHOLDER;
                }
                else
                {
                    Main.LOGGER.warn("Shit: biome id " + biomeInt + " found in genlayer");
                    outLayer[j + i * areaWidth] = SKY_ID;
                }

            }

        }

        return outLayer;

    }
}
