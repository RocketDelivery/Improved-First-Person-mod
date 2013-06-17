package kes5219.improvedfirstperson.hooks;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import kes5219.improvedfirstperson.client.AnimPlayerCompatHelper;
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
		Minecraft mc = IFPClientProxy.mc;
		
		if (ModImprovedFirstPerson.enableBodyRender &&
				mc.gameSettings.thirdPersonView == 0 &&
				!mc.thePlayer.isPlayerSleeping() &&
				mc.renderViewEntity.shouldRenderInPass(MinecraftForgeClient.getRenderPass())) {
			if (IFPClientProxy.animPlayerDetected) {		
				try {
					Object playerData = AnimPlayerCompatHelper.methodGetPlayerData.invoke(null, (Object)IFPClientProxy.mc.renderViewEntity);
					Object texInfo = AnimPlayerCompatHelper.fieldTextureInfo.get(playerData);
					
					boolean animEyes = AnimPlayerCompatHelper.fieldAnimateEyes.getBoolean(texInfo);
					boolean animEyebrows = AnimPlayerCompatHelper.fieldAnimateEyebrows.getBoolean(texInfo);
					boolean animMouth = AnimPlayerCompatHelper.fieldAnimateMouth.getBoolean(texInfo);
					
					if(animEyes) AnimPlayerCompatHelper.fieldAnimateEyes.setBoolean(texInfo, false);
					if(animEyebrows) AnimPlayerCompatHelper.fieldAnimateEyebrows.setBoolean(texInfo, false);
					if(animMouth) AnimPlayerCompatHelper.fieldAnimateMouth.setBoolean(texInfo, false);
					
					RenderManager.instance.renderEntity(IFPClientProxy.mc.renderViewEntity, PartialTickRetriever.getPartialTick());

					if(animEyes) AnimPlayerCompatHelper.fieldAnimateEyes.setBoolean(texInfo, true);
					if(animEyebrows) AnimPlayerCompatHelper.fieldAnimateEyebrows.setBoolean(texInfo, true);
					if(animMouth) AnimPlayerCompatHelper.fieldAnimateMouth.setBoolean(texInfo, true);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				}
			} else RenderManager.instance.renderEntity(IFPClientProxy.mc.renderViewEntity, PartialTickRetriever.getPartialTick());
		}
	}
}
