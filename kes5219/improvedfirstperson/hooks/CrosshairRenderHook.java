package kes5219.improvedfirstperson.hooks;

import java.nio.FloatBuffer;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.utils.misc.PartialTickRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.util.glu.GLU;

//Part of an unfinished feature. Never gets used and should be ignored.
public class CrosshairRenderHook {
	private static float relCrosshairPos;
	private static FloatBuffer win_pos = GLAllocation.createDirectFloatBuffer(16);

	public static void calculateCrosshairPos() {
		Minecraft mc = IFPClientProxy.getMC();
		
		if(mc.objectMouseOver != null) {
			float partialTick = PartialTickRetriever.getPartialTick();
			float xDiff;
			float yDiff;
			float zDiff;
			if(mc.objectMouseOver.entityHit != null) {
				 double collisionBoxSize = mc.objectMouseOver.entityHit.getCollisionBorderSize();
                 AxisAlignedBB var16 = mc.objectMouseOver.entityHit.boundingBox.expand(collisionBoxSize, collisionBoxSize, collisionBoxSize);
                 Vec3 var6 = mc.renderViewEntity.getPosition(partialTick);
                 double var2 = (double)mc.playerController.getBlockReachDistance();
                 if (mc.playerController.extendedReach()) {
                     var2 = 6.0D;
                 }
                 Vec3 var7 = mc.renderViewEntity.getLook(partialTick);
                 Vec3 var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
                 MovingObjectPosition var17 = var16.calculateIntercept(var6, var8);
                 if(var17 == null) {
                	 //default middle
                	 return;
                 }
                 xDiff = (float) (var17.hitVec.xCoord - (mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * partialTick));
 				 yDiff = (float) (var17.hitVec.yCoord - (mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * partialTick));
 				 zDiff = (float) (var17.hitVec.zCoord - (mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * partialTick));
			} else {
                xDiff = (float) (mc.objectMouseOver.hitVec.xCoord - (mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * partialTick));
				yDiff = (float) (mc.objectMouseOver.hitVec.yCoord - (mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * partialTick));
				zDiff = (float) (mc.objectMouseOver.hitVec.zCoord - (mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * partialTick));
			}
			
			relCrosshairPos = win_pos.get(1);
		}
	}
	
	public static boolean shouldCustomRenderCrosshair(GuiIngame gui) {
		if(ModLoader.getMinecraftInstance().gameSettings.thirdPersonView == 0) {
			renderCustomCrosshair(gui);
			return true;
		} else {
			return false;
		}
	}
	
	private static void renderCustomCrosshair(GuiIngame gui) {
		Minecraft mc = IFPClientProxy.getMC();
		ScaledResolution var5 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int var6 = var5.getScaledWidth();
        gui.drawTexturedModalRect(var6 / 2 - 7, 113-((int)(relCrosshairPos / var5.getScaleFactor())-120), 0, 0, 16, 16);
	}
	
	public static boolean test() {
		return false;
	}
}
