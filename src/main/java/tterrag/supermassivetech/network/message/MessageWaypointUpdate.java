package tterrag.supermassivetech.network.message;

import tterrag.supermassivetech.tile.TileWaypoint;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageWaypointUpdate implements IMessage, IMessageHandler<MessageWaypointUpdate, IMessage>
{
    public MessageWaypointUpdate(){}
    
    private int r, g, b;
    private String name;
    private int x, y, z;
    
    public MessageWaypointUpdate(int r, int g, int b, String name, int x, int y, int z)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(r);
        buf.writeInt(g);
        buf.writeInt(b);
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        r = buf.readInt();
        g = buf.readInt();
        b = buf.readInt();
        
        name = ByteBufUtils.readUTF8String(buf);
        
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }
    
    @Override
    public IMessage onMessage(MessageWaypointUpdate message, MessageContext ctx)
    {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        
        TileEntity te = world.getTileEntity(message.x, message.y, message.z);
        
        if (te != null && te instanceof TileWaypoint)
        {
            TileWaypoint tewp = (TileWaypoint) te;
            tewp.waypoint.setColor(message.r, message.g, message.b);
            tewp.waypoint.setName(message.name);
        }
        
        return null;
    }
}
