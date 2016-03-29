package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.api.LitStateProps;
import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ImprovedChestBlock extends BlockContainer{

    public ImprovedChestBlock()
    {
        super(Material.rock);

        this.setDefaultState(this.blockState.getBaseState());

        this.setBlockBounds(0.0625f,0f,0.0625f,0.9375f,0.875f,0.9375f);
        this.setHardness(3.0f);
        this.setUnlocalizedName(Names.IMPROVED_CHEST);
        this.setCreativeTab(LIT.tabLIT);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);

        if (te==null || !(te instanceof TileImprovedChest))
        {
            return true;
        }

        if (worldIn.isSideSolid(pos.add(0,1,0),EnumFacing.DOWN))
        {
            return true;
        }
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(LIT.instance, LitGuiHandler.IMPROVED_CHEST_GUI,worldIn,pos.getX(),pos.getY(),pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileImprovedChest(ChestTypes.getTypeByName("Improved"));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return super.createBlockState();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.markBlockForUpdate(pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((placer.rotationYaw*4f)/360f + 0.5d)&3;
        switch (facing)
        {
            case 0:
                chestFacing = 2;
                break;
            case 1:
                chestFacing = 5;
                break;
            case 2:
                chestFacing = 3;
                break;
            case 3:
                chestFacing = 4;
                break;
            default:
        }

        TileEntity te = worldIn.getTileEntity(pos);
        if (te!=null && te instanceof TileImprovedChest)
        {
            TileImprovedChest chest = (TileImprovedChest)te;
            chest.wasPlaced(placer,stack);
            chest.setFacing(EnumFacing.VALUES[chestFacing]);
            worldIn.markBlockForUpdate(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileImprovedChest chest = (TileImprovedChest)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn,pos,chest);
        super.breakBlock(worldIn,pos,state);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileImprovedChest && ((TileImprovedChest) te).getType().isExplosionResistant())
        {
            return 10000f;
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof IInventory)
        {
            return Container.calcRedstoneFromInventory((IInventory)te);
        }
        return 0;
    }

    private static final EnumFacing[] validRotationAxes = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote)
        {
            return false;
        }

        if (axis== EnumFacing.UP || axis== EnumFacing.DOWN)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileImprovedChest)
            {
                ((TileImprovedChest) tileEntity).rotateAround();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }
}
