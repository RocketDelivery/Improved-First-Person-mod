package kes5219.improvedfirstperson.client;

import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.Player;

import thehippomaster.AnimatedPlayer.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.MemoryConnection;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

public class ClientEventHandler {
	
	@ForgeSubscribe
	public void jumpEvent(LivingJumpEvent event)
	{
		/*if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			
			if (IFPClientProxy.animatedPlayerInstalled)
			{
				PlayerData data = PlayerData.getPlayerData(player);
				
				if (data.jumpTick > 0)
				{
					data.jumpRight = !data.jumpRight;
				}
			}
		}*/
	}
	
	private double lastOffX = 0;
	private double lastOffY = 0;
	private double lastScale = 1;
	
	@ForgeSubscribe
	public void preRenderHUD(RenderGameOverlayEvent.Post event)
	{
		if (ModImprovedFirstPerson.wibblyWobblyHUD)
		{
			Minecraft mc = IFPClientProxy.getMC();
			
			GuiIngameForge hud = (GuiIngameForge)mc.ingameGUI;
			
			if (hud.renderCrosshairs && event.type == ElementType.CROSSHAIRS)
			{
				ScaledResolution res = event.resolution;
				double width = res.getScaledWidth_double();
				double height = res.getScaledHeight_double();
				double width2 = width / 2;
				double height2 = height / 2;
				double scaleFac = res.getScaleFactor();
				
		        GL11.glPushMatrix();
		        
		        double offX = lastOffX;
		        double offY = lastOffY;
		        
		        double scale = lastScale;
		        
		        if (!IFPClientProxy.isGamePaused())
		        {
			        double dX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			        double dY = mc.thePlayer.posY - mc.thePlayer.prevPosY;
			        double dZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			        
			        float rotYaw = mc.thePlayer.rotationYawHead * -0.017453292F;
			        float rotPitch = mc.thePlayer.rotationPitch * -0.017453292F;
	
			        double dYaw = mc.thePlayer.rotationYaw - mc.thePlayer.renderArmYaw;
			        double dPitch = mc.thePlayer.rotationPitch - mc.thePlayer.renderArmPitch;
			        
			        if (mc.gameSettings.thirdPersonView == 2)
			        {
			        	rotYaw += 180 * -0.017453292F;
			        	rotPitch *= -1;
			        	dYaw *= -1;
			        }
			        
			        float cosYaw = MathHelper.cos(rotYaw);
			        float sinYaw = MathHelper.sin(rotYaw);
			        float cosPitch = MathHelper.cos(rotPitch);
			        float sinPitch = MathHelper.sin(rotPitch);
			        
			        float moveDir = (float)Math.atan2(dZ, dX) * 180.0F / (float)Math.PI - 90.0F;
			        
			        double localYawZ = dX * sinYaw + dZ * cosYaw;
			
			        double localX = -dX * cosYaw + dZ * sinYaw;
			        double localY = dY * cosPitch - localYawZ * sinPitch;
			        double localZ = localYawZ * cosPitch + dY * sinPitch;
			        
			        offX = localX * -50 - dYaw * 0.5;
			        offY = localY * 25 - dPitch * 0.4;
			        
			        scale = 0.9 + (localZ * 0.3);
			        scale = Math.min(Math.max(scale, 0.8), 1);
			        
			        double moveAmt = 3 / scaleFac;
			        
			        // Applying scale factor adjustment for UI
			        offX /= moveAmt;
			        offY /= moveAmt;
			        scale = (scale - 1) / moveAmt + 1;
			        
			        // Smoothing
			        offX = lastOffX + (offX - lastOffX) * 0.3;
			        offY = lastOffY + (offY - lastOffY) * 0.3;
			        scale = lastScale + (scale - lastScale) * 0.3;
		        }
		        
		        GL11.glTranslated(width2, height2, 0);
		        GL11.glScaled(scale, scale, 1);
		        GL11.glTranslated(-width2, -height2, 0);
		        
		        GL11.glTranslated(offX, offY, 0);
		        
		        lastOffX = offX;
		        lastOffY = offY;
		        lastScale = scale;
			}
			else if (event.type == ElementType.ALL)
			{
				GL11.glPopMatrix();
			}
		}
	}
	
}
