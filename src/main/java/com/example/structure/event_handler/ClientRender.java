package com.example.structure.event_handler;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientRender {
    public static float SCREEN_SHAKE = 0f;

    public static float getScreenShake(boolean positive) {
        float factor = SCREEN_SHAKE/2F;
        return positive ? factor : factor*-1;
    }

}
