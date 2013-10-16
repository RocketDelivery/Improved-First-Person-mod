package kes5219.improvedfirstperson.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import thehippomaster.AnimatedPlayer.AnimatedPlayer;

import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.MemoryConnection;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPModelPlayerBase;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import kes5219.improvedfirstperson.common.IFPCommonProxy;
import kes5219.improvedfirstperson.hooks.AfterCameraTransformation;
import kes5219.utils.misc.ObjLoader;
import kes5219.utils.misc.PartialTickRetriever;

public class IFPClientProxy extends IFPCommonProxy {
	
	private static final int EMPTYMAPITEMID = 395;
	private static Minecraft mc;
	private static MemoryConnection memConnection;
	
	public static boolean animatedPlayerInstalled = false;
	
	public void preInit(FMLPreInitializationEvent event)
	{
		RenderPlayerAPI.register("kes5219_improvedfirstperson", IFPRenderPlayerBase.class);
		ModelPlayerAPI.register("kes5219_improvedfirstperson", IFPModelPlayerBase.class);
        
		KeyBindingRegistry.registerKeyBinding(new IFPKeyHandler());
		
		try
		{
			if (AnimatedPlayer.instance != null)
			{
				System.out.println("Animated Player mod detected by Improved First Person.");
				animatedPlayerInstalled = true;
			}
		}
		catch (LinkageError e)
		{
			
		}
		
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
	
	public void init(FMLInitializationEvent event)
	{
		//AfterCameraTransformation.init();
		MinecraftForgeClient.registerItemRenderer(Item.map.itemID, new FirstPersonMapRenderer());
		MinecraftForgeClient.registerItemRenderer(EMPTYMAPITEMID, new FirstPersonMapRenderer());		
	}
	
	//@ForgeSubscribe
	public void renderHelmetOverlay()
	{
		
	}
	
	public static Minecraft getMC()
	{
		if (mc == null)
		{
			mc = Minecraft.getMinecraft();
		}
		
		return mc;
	}

	public static boolean isGamePaused() {
		if (memConnection == null)
			memConnection = (MemoryConnection)mc.thePlayer.sendQueue.getNetManager();
		
		return memConnection.isGamePaused();
	}
	
}
