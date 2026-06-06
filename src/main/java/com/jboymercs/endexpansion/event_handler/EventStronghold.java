package com.jboymercs.endexpansion.event_handler;

import com.jboymercs.endexpansion.config.WorldConfig;
import com.jboymercs.endexpansion.world.stronghold.MapGenBetterStronghold;
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
