package kes5219.improvedfirstperson.hooks;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.utils.misc.PartialTickRetriever;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

public class AfterCameraTransformation {
	
	public static final float EYE_OFFSET = 0.14f;
	public static final float EYE_OFFSET_HEIGHT = 0.11f;
	public static final double BODY_HEIGHT = 1.5d;
	
    private static FloatBuffer win_pos = GLAllocation.createDirectFloatBuffer(16);
	public static float crosshairYPos;
	
	//Class transformer is used to inject a code calling this method right after
	//the line 1092 of class EntityRenderer, where it says  
	//this.mc.mcProfiler.endStartSection("frustrum");
	//
	//This method handles camera transformation and its functionalities are to
	//to offset the camera to make it look more realistic and to prevent users from seeing through blocks
	public static void afterCameraTransform() {
		if (ModImprovedFirstPerson.enableBodyRender)
		{
			Minecraft mc = IFPClientProxy.mc;
			EntityPlayer player = (EntityPlayer)mc.renderViewEntity;
			float partialTick = PartialTickRetriever.getPartialTick();
			
			if (!player.isPlayerSleeping() && mc.gameSettings.thirdPersonView == 0)
			{
		        float camXOffset = ActiveRenderInfo.objectX;
		        float camYOffset = ActiveRenderInfo.objectY;
		        float camZOffset = ActiveRenderInfo.objectZ;
		        
				GL11.glLoadIdentity();
				displayOverlayEffects(partialTick);
				GL11.glTranslatef(0.0f, -EYE_OFFSET_HEIGHT, 0.0f);
		        GL11.glRotatef(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTick, 1.0F, 0.0F, 0.0F);
		        GL11.glTranslatef(0.0f, 0.0625f, EYE_OFFSET);
		        GL11.glRotatef(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTick + 180.0F, 0.0F, 1.0F, 0.0F);
		        ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
	
				double xOffset = player.posX + ActiveRenderInfo.objectX;
				double yOffset = player.posY + ActiveRenderInfo.objectY;
				double zOffset = player.posZ + ActiveRenderInfo.objectZ;
				
				MovingObjectPosition movingObjPos = player.rayTrace(EYE_OFFSET, 1.0f);
				
				if (movingObjPos != null) {
					int blockID = player.worldObj.getBlockId(movingObjPos.blockX, movingObjPos.blockY, movingObjPos.blockZ);
					
					if (!Block.blocksList[blockID].isOpaqueCube()) {
						double xDisp = movingObjPos.hitVec.xCoord - xOffset;//player.posX;
						double yDisp = movingObjPos.hitVec.yCoord - yOffset;//player.posY;
						double zDisp = movingObjPos.hitVec.zCoord - zOffset;//player.posZ;
						double length = Math.sqrt(xDisp * xDisp + yDisp * yDisp + zDisp * zDisp);
						float eyeDisplacement = EYE_OFFSET_HEIGHT;
						if(length < EYE_OFFSET_HEIGHT) {
							eyeDisplacement = (float)length;
						}
						GL11.glLoadIdentity();
						displayOverlayEffects(partialTick);
						GL11.glTranslatef(0.0f, -eyeDisplacement, 0.0f);
				        GL11.glRotatef(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTick, 1.0F, 0.0F, 0.0F);
				        GL11.glTranslatef(0.0f, 0.0f, EYE_OFFSET);
				        GL11.glTranslatef(0.0f, 0.063f, 0.0f);
				        GL11.glRotatef(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTick + 180.0F, 0.0F, 1.0F, 0.0F);
					}
				}
				
		        ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
		        //determineCrosshairPosition(partialTick/*camXOffset, camYOffset, camZOffset*/);
			}
		}
	}
	
	private static void determineCrosshairPosition(float partialTick) {
		//Part of an uncompleted experimental feature.
	}
	
	//same as setupViewBobbing method in EntityRenderer.
    public static void setupViewBobbing(float par1)
    {
    	Minecraft mc = IFPClientProxy.mc;
        if (mc.renderViewEntity instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer)mc.renderViewEntity;
            float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
            float var4 = -(var2.distanceWalkedModified + var3 * par1);
            float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * par1;
            float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * par1;
            GL11.glTranslatef(MathHelper.sin(var4 * (float)Math.PI) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * (float)Math.PI) * var5), 0.0F);
            GL11.glRotatef(MathHelper.sin(var4 * (float)Math.PI) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(var4 * (float)Math.PI - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
        }
    }
    
    //same as hurtCameraEffect method in EntityRenderer
	public static void hurtCameraEffect(float par1)
    {
		Minecraft mc = IFPClientProxy.mc;
        EntityLiving var2 = mc.renderViewEntity;
        float var3 = (float)var2.hurtTime - par1;
        float var4;

        if (var2.getHealth() <= 0)
        {
            var4 = (float)var2.deathTime + par1;
            GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
        }

        if (var3 >= 0.0F)
        {
            var3 /= (float)var2.maxHurtTime;
            var3 = MathHelper.sin(var3 * var3 * var3 * var3 * (float)Math.PI);
            var4 = var2.attackedAtYaw;
            GL11.glRotatef(-var4, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var4, 0.0F, 1.0F, 0.0F);
        }
    }
	
	//same as displayOverlayEffects method in EntityRenderer
	private static void displayOverlayEffects(float partialTick) {
		Minecraft mc = IFPClientProxy.mc;
		hurtCameraEffect(partialTick);
	    if (mc.gameSettings.viewBobbing)
	    {
	    	setupViewBobbing(partialTick);
	    }
	    
        float timeInPortal = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * partialTick;
        
        //mc.entityRenderer.rendererUpdateCount is private by default, but changed to public
        //during initialization using class transformer.
        int rendererUpdateCount = mc.entityRenderer.rendererUpdateCount;
        if (timeInPortal > 0.0F)
        {
            byte var5 = 20;

            if (mc.thePlayer.isPotionActive(Potion.confusion))
            {
                var5 = 7;
            }

            float var6 = 5.0F / (timeInPortal * timeInPortal + 5.0F) - timeInPortal * 0.04F;
            var6 *= var6;
            GL11.glRotatef(((float)rendererUpdateCount + partialTick) * (float)var5, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / var6, 1.0F, 1.0F);
            GL11.glRotatef(-((float)rendererUpdateCount + partialTick) * (float)var5, 0.0F, 1.0F, 1.0F);
        }
	}
}
