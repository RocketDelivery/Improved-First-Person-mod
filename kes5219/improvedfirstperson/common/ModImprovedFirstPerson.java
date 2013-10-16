package kes5219.improvedfirstperson.common;

import java.lang.reflect.Field;
import java.util.HashMap;

import kes5219.improvedfirstperson.client.FirstPersonMapRenderer;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPModelPlayerBase;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import kes5219.improvedfirstperson.hooks.AfterCameraTransformation;
import kes5219.improvedfirstperson.hooks.ItemRendererHook;
import kes5219.improvedfirstperson.hooks.MouseSelectionOverride;
import kes5219.improvedfirstperson.hooks.RenderEntityHook;
import kes5219.utils.misc.PartialTickRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
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

@Mod(modid = ModImprovedFirstPerson.MOD_ID, name = "Improved First Person View Mod", version = "1.6.4_r1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModImprovedFirstPerson
{
	public static final String MOD_ID = "kes5219_improvedfirstperson";
	
	public static boolean enableBodyRender = true;
	
	@SidedProxy(clientSide="kes5219.improvedfirstperson.client.IFPClientProxy", serverSide="kes5219.improvedfirstperson.common.IFPCommonProxy")
	public static IFPCommonProxy proxy;
	
	@Instance(ModImprovedFirstPerson.MOD_ID)
	public static ModImprovedFirstPerson instance;
	
	public static Configuration config;
	public static HashMap<String, String> configComments = new HashMap(){{
		put("leanAmount", "The amount the player leans over when looking down. (default = 0.75)");
		put("wibblyWobblyHUD", "With this on, the HUD will act somewhat like the \"3D\" HUDs in modern games.");
	}};
	public static float leanAmount = 0.75F;
	public static boolean wibblyWobblyHUD = false;
	
	private static Property getProperty(String category, String key, Object defaultValue)
	{
		Property property = null;
		
		if (defaultValue instanceof String)
			property = config.get(category, key, (String)defaultValue);
		else if (defaultValue instanceof Integer)
			property = config.get(category, key, (Integer)defaultValue);
		else if (defaultValue instanceof Double)
			property = config.get(category, key, (Double)defaultValue);
		else if (defaultValue instanceof Boolean)
			property = config.get(category, key, (Boolean)defaultValue);
		else if (defaultValue instanceof String[])
			property = config.get(category, key, (String[])defaultValue);
		else if (defaultValue instanceof int[])
			property = config.get(category, key, (int[])defaultValue);
		else if (defaultValue instanceof boolean[])
			property = config.get(category, key, (boolean[])defaultValue);
		
		String comment = configComments.get(key);
		
		if (comment != null)
			property.comment = comment;
		
		return property;
	}
	
	private static Property getGenProperty(String key, Object defaultValue)
	{
		return getProperty(Configuration.CATEGORY_GENERAL, key, defaultValue);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		
		config = new Configuration(event.getSuggestedConfigurationFile());
		
		leanAmount = (float)getGenProperty("leanAmount", (double)leanAmount).getDouble(leanAmount);
		wibblyWobblyHUD = getGenProperty("wibblyWobblyHUD", wibblyWobblyHUD).getBoolean(wibblyWobblyHUD);
		
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {	
		proxy.postInit(event);
	}
}
