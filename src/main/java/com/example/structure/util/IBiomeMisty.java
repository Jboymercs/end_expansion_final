package com.example.structure.util;

public interface IBiomeMisty {

    /**
     * Set the density/how much mist there should be in the biome.
     * @return 0 to have no mist at all.
     */
    float getMistDensity(int var1, int var2, int var3);

    /**
     * Sets the color of the mist. Uses decimal values for color.
     * @return 0 for black mist.
     */
    int getMistColour(int var1, int var2, int var3);

}
