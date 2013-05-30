package kes5219.improvedfirstperson.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPModelPlayerBase;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import kes5219.improvedfirstperson.common.IFPCommonProxy;
import kes5219.improvedfirstperson.hooks.AfterCameraTransformation;
import kes5219.utils.misc.ObjLoader;
import kes5219.utils.misc.PartialTickRetriever;

public class IFPClientProxy extends IFPCommonProxy {
	
	private static final int EMPTYMAPITEMID = 395;
	public static Minecraft mc;
	
	public void preInit(FMLPreInitializationEvent event) {
		mc = Minecraft.getMinecraft();
		RenderPlayerAPI.register("kes5219_improvedfirstperson", IFPRenderPlayerBase.class);
		ModelPlayerAPI.register("kes5219_improvedfirstperson", IFPModelPlayerBase.class);
        
		KeyBindingRegistry.registerKeyBinding(new IFPKeyHandler());
	}
	
	public void init(FMLInitializationEvent event) {
		//AfterCameraTransformation.init();
		MinecraftForgeClient.registerItemRenderer(Item.map.itemID, new FirstPersonMapRenderer());
		MinecraftForgeClient.registerItemRenderer(EMPTYMAPITEMID, new FirstPersonMapRenderer());		
	}
}
