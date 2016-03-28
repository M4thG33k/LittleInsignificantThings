package com.m4thg33k.lit.api.chest;

import com.m4thg33k.lit.core.util.LogHelper;

import java.util.ArrayList;

public class ChestTypes {

    public static final ArrayList<ChestTypes> allTypes = new ArrayList<ChestTypes>();

    private String typeName;
    private int size;


    public ChestTypes(String typeName, int size)
    {
        this.typeName = typeName;
        this.size = size;
    }

    public static void addType(String typeName,int size)
    {
        if (ChestTypes.getTypeByName(typeName)!=null)
        {
            LogHelper.error("Error! Chest type: " + typeName + " has already been registered! Skipping!");
            return;
        }
        ChestTypes type = new ChestTypes(typeName,size);
        allTypes.add(type);
    }

    public static ChestTypes getTypeByName(String name)
    {
        for (ChestTypes type : allTypes)
        {
            if (type.typeName.equals(name))
            {
                return type;
            }
        }
        return null;
    }
    public String getTypeName()
    {
        return typeName;
    }

    public int getSize()
    {
        return size;
    }
}
