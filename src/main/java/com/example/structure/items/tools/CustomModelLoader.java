package com.example.structure.items.tools;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class CustomModelLoader implements ICustomModelLoader {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getNamespace().equals("ee") && modelLocation.getPath().startsWith("models/item/") && modelLocation.getPath().endsWith("dummy");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        CustomModel customModel = new CustomModel(modelLocation.getPath().replace("models/item/", "").replace("_dummy", ""));
        return customModel;
    }
}
