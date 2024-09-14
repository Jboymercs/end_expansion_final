package com.example.structure.event_handler;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.world.stronghold.MapGenBetterStronghold;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventStronghold {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onMineshaftGen(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.STRONGHOLD && WorldConfig.replace_stronghold) {
            event.setNewGen(new MapGenBetterStronghold());
        }
    }
}
