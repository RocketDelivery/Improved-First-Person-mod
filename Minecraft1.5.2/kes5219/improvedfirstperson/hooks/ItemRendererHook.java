package kes5219.improvedfirstperson.hooks;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.ModLoader;

public class ItemRendererHook {
	
	//Class transformer is used to inject the following code at the beginning of the method
	//renderItemInFirstPerson(float par1) in ItemRenderer :
	//if(!ItemRendererHook.shouldRenderItemInFirstPerson()) return;
	//By returning false, this method prevents rendering of default first person handheld item and player hand.
    public static boolean shouldRenderItemInFirstPerson()
	{
    	GameSettings settings = IFPClientProxy.mc.gameSettings;
		if(settings.thirdPersonView == 0 && !IFPClientProxy.mc.renderViewEntity.isPlayerSleeping() && !settings.hideGUI)
		{
			//Do not render the item and hand
			return false;
		} else
		{
			//render the item and hand
			return true;
		}
	}
}
