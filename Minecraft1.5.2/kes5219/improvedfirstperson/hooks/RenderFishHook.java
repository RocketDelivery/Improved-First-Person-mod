package kes5219.improvedfirstperson.hooks;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.src.ModLoader;

public class RenderFishHook {
	private static int thirdPersonViewTemp;
	
	//These two methods are used to trick Minecraft into rendering the fishlines
	//as if the view was in third person even in first person view.
	
	//Class transformer is used to inject a code calling the following method in
	//the beginning of the method doRenderFishHook in class RenderFish.
	public static void onMethodStart() {
		thirdPersonViewTemp = IFPClientProxy.mc.gameSettings.thirdPersonView;
		IFPClientProxy.mc.gameSettings.thirdPersonView = 1;
	}
	
	//Class transformer is used to inject a code calling the following method in
	//the beginning of the method doRenderFishHook in class RenderFish.
	public static void onMethodEnd() {
		IFPClientProxy.mc.gameSettings.thirdPersonView = thirdPersonViewTemp;
	}
}
