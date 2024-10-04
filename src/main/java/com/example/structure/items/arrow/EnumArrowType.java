package com.example.structure.items.arrow;

public enum EnumArrowType {

    Green(0, "green"), Unholy(1, "unholy"), Chomper(2, "chomper");

    public int meta;

    public String name;

    EnumArrowType(int meta, String name)
    {
        this.meta = meta;
        this.name = name;
    }

    public static EnumArrowType getType(int meta)
    {
        return meta == 1 ? Unholy :  meta == 2 ? Chomper : Green;
    }

    public int getMeta()
    {
        return this.meta;
    }

    public String toString()
    {
        return this.name;
    }
}
