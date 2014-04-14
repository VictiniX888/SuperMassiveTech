package cofh.api.transport;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implement this interface to explicitly control sided Item Conduit connections
 * for a Tile Entity.
 * 
 * @author Zeldo Kavira
 * 
 */
public interface IItemConduitConnection
{

    public boolean canConduitConnect(ForgeDirection side);

}
