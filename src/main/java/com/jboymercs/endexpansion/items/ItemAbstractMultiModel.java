package com.jboymercs.endexpansion.items;

public abstract class ItemAbstractMultiModel extends ItemBase{
    public ItemAbstractMultiModel(String name) {
        super(name);
    }

    public abstract void registerModels();

}
