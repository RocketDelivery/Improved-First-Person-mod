package kes5219.improvedfirstperson.hooks;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
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
					//The reason why this if statement is here instead of postInit in IFPClientProxy
					//is that Animated Player mod registers its own player renderer in postInit process.
					if(AnimPlayerCompatHelper.playerRenderer == null) {
						AnimPlayerCompatHelper.playerRenderer = (Render)RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
					}
					
					Object playerData = AnimPlayerCompatHelper.methodGetPlayerData.invoke(null, (Object)IFPClientProxy.mc.renderViewEntity);
					Object texInfo = AnimPlayerCompatHelper.fieldTextureInfo.get(playerData);
					Object modelPlayer = AnimPlayerCompatHelper.fieldPlayerModel.get(AnimPlayerCompatHelper.playerRenderer);
					Object modelArmorHead = AnimPlayerCompatHelper.fieldPlayerArmorHeadModel.get(AnimPlayerCompatHelper.playerRenderer);
					
					boolean animEyes = AnimPlayerCompatHelper.fieldAnimateEyes.getBoolean(texInfo);
					boolean animEyebrows = AnimPlayerCompatHelper.fieldAnimateEyebrows.getBoolean(texInfo);
					boolean animMouth = AnimPlayerCompatHelper.fieldAnimateMouth.getBoolean(texInfo);
					
					ModelRenderer rendererHead = (ModelRenderer)AnimPlayerCompatHelper.fieldHead.get(modelPlayer);
					ModelRenderer rendererHeadwear = (ModelRenderer)AnimPlayerCompatHelper.fieldHeadwear.get(modelPlayer);
					ModelRenderer rendererArmorHeadwear = (ModelRenderer)AnimPlayerCompatHelper.fieldHeadwear.get(modelArmorHead);
					
					boolean renderHead = rendererHead.showModel;
					boolean renderHeadwear = rendererHeadwear.showModel;
					boolean renderArmorHeadwear = rendererArmorHeadwear.showModel;
					
					if(animEyes) AnimPlayerCompatHelper.fieldAnimateEyes.setBoolean(texInfo, false);
					if(animEyebrows) AnimPlayerCompatHelper.fieldAnimateEyebrows.setBoolean(texInfo, false);
					if(animMouth) AnimPlayerCompatHelper.fieldAnimateMouth.setBoolean(texInfo, false);
					if(renderHead) rendererHead.showModel = false;
					if(renderHeadwear) rendererHeadwear.showModel = false;
					if(renderArmorHeadwear) rendererArmorHeadwear.showModel = false;
					
					RenderManager.instance.renderEntity(IFPClientProxy.mc.renderViewEntity, PartialTickRetriever.getPartialTick());

					if(animEyes) AnimPlayerCompatHelper.fieldAnimateEyes.setBoolean(texInfo, true);
					if(animEyebrows) AnimPlayerCompatHelper.fieldAnimateEyebrows.setBoolean(texInfo, true);
					if(animMouth) AnimPlayerCompatHelper.fieldAnimateMouth.setBoolean(texInfo, true);
					if(renderHead) rendererHead.showModel = true;
					if(renderHeadwear) rendererHeadwear.showModel = true;
					if(renderArmorHeadwear) rendererArmorHeadwear.showModel = true;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				} catch (ClassCastException e) {
					e.printStackTrace();
					IFPClientProxy.animPlayerDetected = false;
				}
			} else RenderManager.instance.renderEntity(IFPClientProxy.mc.renderViewEntity, PartialTickRetriever.getPartialTick());
		}
	}
}
