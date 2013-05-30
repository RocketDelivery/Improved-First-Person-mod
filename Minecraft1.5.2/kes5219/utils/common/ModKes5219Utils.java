package kes5219.utils.common;

import kes5219.improvedfirstperson.common.IFPCommonProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "kes5219_Utility", name = "Kes5219 Utility Mods", version = "1_IFP1.5.1r1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModKes5219Utils {
	
	@SidedProxy(clientSide="kes5219.utils.client.Kes5219UtilClientProxy", serverSide="kes5219.utils.common.Kes5219UtilCommonProxy")
	public static Kes5219UtilCommonProxy proxy;
	
	@Instance("kes5219_Utility")
	public static ModKes5219Utils instance;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Init
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@PostInit
	public static void postInit(FMLPostInitializationEvent event) {	
		proxy.postInit(event);
	}

}
