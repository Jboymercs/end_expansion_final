package com.example.structure.packets;

import com.example.structure.world.Biome.WorldProviderEndEE;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.Struct;

public class MessageStructureProtection implements IMessage {

    private StructureBoundingBox box;

    @SuppressWarnings("unused")
    public MessageStructureProtection() {

    }

    public MessageStructureProtection(StructureBoundingBox box) {
        this.box = box;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        box = new StructureBoundingBox(
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readInt(), buf.readInt()
        );
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(box.minX);
        buf.writeInt(box.minY);
        buf.writeInt(box.minZ);
        buf.writeInt(box.maxX);
        buf.writeInt(box.maxY);
        buf.writeInt(box.maxZ);
    }

    public static class Handler implements IMessageHandler<MessageStructureProtection, IMessage> {

        @Override
        public IMessage onMessage(MessageStructureProtection message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                WorldProvider provider = Minecraft.getMinecraft().world.provider;

                // add weather box if needed
                if (provider instanceof WorldProviderEndEE) {
                    IRenderHandler weatherRenderer = provider.getWeatherRenderer();

                }
            });

            return null;
        }
    }
}
