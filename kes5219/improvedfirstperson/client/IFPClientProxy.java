package kes5219.improvedfirstperson.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EnumSet;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPModelPlayerBase;
import kes5219.improvedfirstperson.client.renderplayerAPIbase.IFPRenderPlayerBase;
import kes5219.improvedfirstperson.common.IFPCommonProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.improvedfirstperson.hooks.AfterCameraTransformation;
import kes5219.utils.misc.ObjLoader;
import kes5219.utils.misc.PartialTickRetriever;

public class IFPClientProxy extends IFPCommonProxy implements ITickHandler {
	
	private static final int EMPTYMAPITEMID = 395;
	public static Minecraft mc;
	public static boolean animPlayerDetected;
	private boolean displayMessage = true;
	
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
		TickRegistry.registerTickHandler(this, Side.CLIENT);
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		if(Loader.isModLoaded("AnimatedPlayer")) {
			boolean success = false;
			try {
				AnimPlayerCompatHelper.classPlayerData = Class.forName("mods.AnimatedPlayer.PlayerData");
				AnimPlayerCompatHelper.methodGetPlayerData = AnimPlayerCompatHelper.classPlayerData.getMethod("getPlayerData", EntityPlayer.class);
				AnimPlayerCompatHelper.fieldTextureInfo = AnimPlayerCompatHelper.classPlayerData.getField("textureInfo");				
				
				AnimPlayerCompatHelper.classTextureInfo = Class.forName("mods.AnimatedPlayer.PlayerData$TextureInfo");
				AnimPlayerCompatHelper.fieldAnimateEyebrows = AnimPlayerCompatHelper.classTextureInfo.getField("animateEyebrows");
				AnimPlayerCompatHelper.fieldAnimateEyes = AnimPlayerCompatHelper.classTextureInfo.getField("animateEyes");
				AnimPlayerCompatHelper.fieldAnimateMouth = AnimPlayerCompatHelper.classTextureInfo.getField("animateMouth");
				
				AnimPlayerCompatHelper.classRenderPlayer = Class.forName("mods.AnimatedPlayer.client.RenderPlayer");
				AnimPlayerCompatHelper.fieldPlayerModel = AnimPlayerCompatHelper.classRenderPlayer.getField("playerModel");
				
				AnimPlayerCompatHelper.classModelPlayer = Class.forName("mods.AnimatedPlayer.client.ModelPlayer");
				AnimPlayerCompatHelper.fieldHead = AnimPlayerCompatHelper.classModelPlayer.getField("head");
				AnimPlayerCompatHelper.fieldHeadwear = AnimPlayerCompatHelper.classModelPlayer.getField("headwear");
				
				System.out.println("Improved First Person Mod: Animated Player mod successfully identified. Enabling compatibility with Animated Players mod.");
				success = true;
			} 
			//Forge says that the mod is loaded, but the class files couldn't be found.
			//Most likely because names of the classes and methods have been changed.
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				success = false;
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
				success = false;
			}
			catch (NoSuchFieldException e) {
				e.printStackTrace();
				success = false;
			}
			catch (SecurityException e) {
				e.printStackTrace();
				success = false;
			}
			animPlayerDetected = success;
		}
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(displayMessage && animPlayerDetected && ModImprovedFirstPerson.enableBodyRender && mc.theWorld != null)
		{
			mc.ingameGUI.getChatGUI().printChatMessage("[Improved First Person mod] Improved First Person mod has been disabled because incompatibility with Animated Player mod was detected. Use \"Toggle IFP view\" keybind (Default: F6) to force to enable it");
			ModImprovedFirstPerson.enableBodyRender = false;
			displayMessage = false;
		} else if(mc.theWorld == null) displayMessage = true;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return null;
	}
}
