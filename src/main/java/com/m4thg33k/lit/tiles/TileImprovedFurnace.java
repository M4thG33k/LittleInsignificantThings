package com.m4thg33k.lit.tiles;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileImprovedFurnace extends TileEntity implements ISidedInventory{

    public boolean getOn()
    {
        return false;
    }

    public void setFacing(EnumFacing facing)
    {

    }

    public EnumFacing getFacing()
    {
        return null;
    }
}
