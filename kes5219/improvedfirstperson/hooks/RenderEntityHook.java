package kes5219.improvedfirstperson.hooks;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.utils.misc.PartialTickRetriever;

public class RenderEntityHook {

	//Class transformer is used to inject a code calling the following method right
	//after the line 512 of the class RenderGlobal, where it says
	//this.theWorld.theProfiler.endStartSection("tileentities");
	//The primary purpose of this method is to enable rendering of the player body
	//even in first person mod.
	public static void onRenderEntities() {
		Minecraft mc = IFPClientProxy.getMC();
		
		if (ModImprovedFirstPerson.enableBodyRender &&
				mc.gameSettings.thirdPersonView == 0 &&
				!mc.thePlayer.isPlayerSleeping() &&
				mc.renderViewEntity.shouldRenderInPass(MinecraftForgeClient.getRenderPass())) {
			RenderManager.instance.renderEntity(mc.renderViewEntity, PartialTickRetriever.getPartialTick());
		}
	}
}
