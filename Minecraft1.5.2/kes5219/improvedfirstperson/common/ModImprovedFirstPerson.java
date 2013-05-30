package kes5219.improvedfirstperson.common;

import java.lang.reflect.Field;

import kes5219.improvedfirstperson.client.FirstPersonMapRenderer;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPModelPlayerBase;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import kes5219.improvedfirstperson.hooks.AfterCameraTransformation;
import kes5219.improvedfirstperson.hooks.ItemRendererHook;
import kes5219.improvedfirstperson.hooks.MouseSelectionOverride;
import kes5219.improvedfirstperson.hooks.RenderEntityHook;
import kes5219.utils.misc.PartialTickRetriever;
//import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
//import net.minecraft.src.ModelPlayerAPI;
//import net.minecraft.src.RenderPlayerAPI;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(modid = "kes5219_improvedfirstperson", name = "Improved First Person View Mod", version = "1.5.1_r1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModImprovedFirstPerson
{	
	@SidedProxy(clientSide="kes5219.improvedfirstperson.client.IFPClientProxy", serverSide="kes5219.improvedfirstperson.common.IFPCommonProxy")
	public static IFPCommonProxy proxy;
	
	@Instance("kes5219_improvedfirstperson")
	public static ModImprovedFirstPerson instance;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Init
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		if(!ItemRendererHook.shouldRenderItemInFirstPerson()) System.out.println("Should not render in first person");
	}

	@PostInit
	public static void postInit(FMLPostInitializationEvent event) {	
		proxy.postInit(event);
	}
}
