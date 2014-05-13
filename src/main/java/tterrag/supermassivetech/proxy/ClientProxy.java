package tterrag.supermassivetech.proxy;

import static tterrag.supermassivetech.SuperMassiveTech.blockRegistry;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDHopper;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDStarHarvester;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDStorage;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.client.model.ModelBlackHoleStorage;
import tterrag.supermassivetech.client.render.DirectionalModelRenderer;
import tterrag.supermassivetech.client.render.ItemObjRenderer;
import tterrag.supermassivetech.client.render.RenderStarHarvester;
import tterrag.supermassivetech.client.render.RenderWaypoint;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileStarHarvester;
import tterrag.supermassivetech.tile.TileWaypoint;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static DirectionalModelRenderer storage;
    public static ItemObjRenderer storageItem;

    public static DirectionalModelRenderer hopper;
    public static ItemObjRenderer hopperItem;

    public static RenderStarHarvester starHarvester;
    public static ItemObjRenderer starHarvesterItem;
    
    public static RenderWaypoint waypoint;

    @Override
    public void registerRenderers()
    {
        storage = new DirectionalModelRenderer(new ModelBlackHoleStorage(), new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/models/storage.png"));
        hopper = new DirectionalModelRenderer(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/newHopper.obj"), new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/models/hopper.png"));
        starHarvester = new RenderStarHarvester(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/starHarvesterMain.obj"), new ResourceLocation(Reference.MOD_TEXTUREPATH,
                "models/starHarvesterSphere.obj"), new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/ring.obj"));
        waypoint = new RenderWaypoint();
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleStorage.class, storage);
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleHopper.class, hopper);
        ClientRegistry.bindTileEntitySpecialRenderer(TileStarHarvester.class, starHarvester);
        ClientRegistry.bindTileEntitySpecialRenderer(TileWaypoint.class, waypoint);

        renderIDStorage = RenderingRegistry.getNextAvailableRenderId();
        storageItem = new ItemObjRenderer(renderIDStorage, storage);
        RenderingRegistry.registerBlockHandler(storageItem);

        renderIDHopper = RenderingRegistry.getNextAvailableRenderId();
        hopperItem = new ItemObjRenderer(renderIDHopper, hopper);
        RenderingRegistry.registerBlockHandler(hopperItem);

        renderIDStarHarvester = RenderingRegistry.getNextAvailableRenderId();
        starHarvesterItem = new ItemObjRenderer(renderIDStarHarvester, starHarvester);
        RenderingRegistry.registerBlockHandler(starHarvesterItem);
    }

    @Override
    public void init()
    {
        storageItem.init(blockRegistry.blackHoleStorage);
        hopperItem.init(blockRegistry.blackHoleHopper);
        starHarvesterItem.init(blockRegistry.starHarvester);
    }
}
