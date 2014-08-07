package tterrag.supermassivetech.common.item.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockStorage extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockStorage(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.blackHoleStorage", true);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound != null)
        {
            List<String> strs = new ArrayList<String>();

            if (stack.stackTagCompound.getTag("itemStack") != null)
                strs.add(Utils.formatString(Utils.localize("tooltip.stored", true) + ": ", "", stack.stackTagCompound.getLong("itemsStored"), true, true) + " "
                        + StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));

            if (stack.stackTagCompound.getTag("fluidStack") != null)
                strs.add(Utils.formatString(Utils.localize("tooltip.stored", true) + ": ", " mB", stack.stackTagCompound.getLong("fluidStored"), true, true) + " "
                        + StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));
            // TODO fix deprecated usage of getLocalizedName()
            return Utils.makeTooltipString(strs);
        }

        return null;
    }
}