package tterrag.supermassivetech.common.tile.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.MessageEnergyUpdate;
import tterrag.supermassivetech.common.tile.TileSMTInventory;
import tterrag.supermassivetech.common.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public abstract class TileSMTEnergy extends TileSMTInventory implements IEnergyHandler
{
    protected EnergyStorage storage;
    protected int capacity;
    
    private int lastStored = 0;

    public TileSMTEnergy(int cap)
    {
        super();
        init(cap);
    }

    public TileSMTEnergy(float rangeMult, float strengthMult, int cap)
    {
        super(rangeMult, strengthMult);
        init(cap);
    }

    public TileSMTEnergy(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav, int cap)
    {
        super(rangeMult, strengthMult, maxGravXZ, maxGravY, minGrav);
        init(cap);
    }

    private void init(int cap)
    {
        storage = new EnergyStorage(cap);
    }

    public abstract ForgeDirection[] getValidOutputs();

    public abstract ForgeDirection[] getValidInputs();

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            pushEnergy();
            
            if (getEnergyStored() != lastStored)
            {
                sendPacket();
                lastStored = getEnergyStored();
            }
        }
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageEnergyUpdate(xCoord, yCoord, zCoord, getEnergyStored()), worldObj.provider.dimensionId);
    }

    protected void pushEnergy()
    {
        for (ForgeDirection dir : getValidOutputs())
        {
            TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (tile instanceof IEnergyHandler)
            {
                IEnergyHandler ieh = (IEnergyHandler) tile;
                ieh.receiveEnergy(dir, storage.extractEnergy(getOutputSpeed(), false), false);
            }
        }
    }

    /* I/O Handling */

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        if (ArrayUtils.contains(getValidOutputs(), from))
        {
            int ret = storage.extractEnergy(maxExtract, true);
            if (!simulate)
            {
                storage.extractEnergy(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        if (ArrayUtils.contains(getValidInputs(), from))
        {
            int ret = storage.receiveEnergy(maxReceive, true);
            if (!simulate)
            {
                storage.receiveEnergy(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return ArrayUtils.contains(getValidInputs(), from) || ArrayUtils.contains(getValidOutputs(), from);
    }

    /* IEnergyHandler basic impl */

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return getMaxStorage();
    }
    

    /* IWailaAdditionalInfo */
    
    public void getWailaInfo(java.util.List<String> tooltip, int x, int y, int z, net.minecraft.world.World world)
    {
        super.getWailaInfo(tooltip, x, y, z, world);
        
        int energyStored = this.getEnergyStored(), maxEnergyStored = this.getMaxStorage();
        int output = this.getOutputSpeed();

        tooltip.add(EnumChatFormatting.WHITE + Utils.localize("tooltip.bufferStorage", true) + ": " + Utils.getColorForPowerLeft(energyStored, maxEnergyStored) + energyStored
                + " RF");
        tooltip.add(EnumChatFormatting.WHITE + Utils.localize("tooltip.currentOutputMax", true) + ": " + Utils.getColorForPowerLeft(output, this.getMaxOutputSpeed())
                + output + " RF");
    }

    
    /* getters & setters */

    public int getEnergyStored()
    {
        return storage.getEnergyStored();
    }

    public void setEnergyStored(int energy)
    {
        storage.setEnergyStored(energy);
    }

    public int getMaxStorage()
    {
        return storage.getMaxEnergyStored();
    }

    public void setMaxStorage(int storage)
    {
        this.storage.setCapacity(storage);
    }

    public int getOutputSpeed()
    {
        return storage.getMaxExtract();
    }

    public int getMaxOutputSpeed()
    {
        return getOutputSpeed();
    }

    public void setOutputSpeed(int outputSpeed)
    {
        this.storage.setMaxExtract(outputSpeed);
    }

    public int getInputSpeed()
    {
        return storage.getMaxReceive();
    }

    public void setInputSpeed(int inputSpeed)
    {
        this.storage.setMaxReceive(inputSpeed);
    }
    
    /* Read/Write NBT */
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        storage.writeToNBT(nbt);
        super.writeToNBT(nbt);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        storage.readFromNBT(nbt);
        super.readFromNBT(nbt);
    }
}