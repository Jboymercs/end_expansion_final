package com.example.structure.util.integration;

import com.example.structure.config.ModConfig;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration {


    public static boolean IS_DEEPER_DEPTHS_LOADED = Loader.isModLoaded("deeperdepths");

    //Adjustment to stuff with BOMD loaded
    public static boolean IS_BOMD_DA_LOADED = Loader.isModLoaded("da");



    public static double getMultiplierCountAll() {
    if(IS_BOMD_DA_LOADED && ModConfig.is_bomd_compat) {
        return 0.3;
    }
        return 0;
    }

    public static double getMultiplierCountAttackDamage() {
        if(IS_BOMD_DA_LOADED && ModConfig.is_bomd_compat) {
            return 0.05;
        }
        return 0;
    }

    public static int getMultiplierCountAttackWeapons() {
    if(IS_BOMD_DA_LOADED && ModConfig.is_bomd_compat) {
        return 1;
    }
    return 0;
    }

    public static int getAdditiveArmorCount() {
       if (IS_BOMD_DA_LOADED && ModConfig.is_bomd_compat) {
            return 1;
        }
        return 0;
    }
}
