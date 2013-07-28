package kes5219.improvedfirstperson.hooks;

import java.nio.FloatBuffer;

import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.utils.misc.PartialTickRetriever;

public class MouseSelectionOverride {

	private static double tempPosX;
	private static double tempPosY;
	private static double tempPosZ;
	private static double tempPrevPosX;
	private static double tempPrevPosY;
	private static double tempPrevPosZ;
	private static boolean shouldRestore;
	
	//Class transformer is used to inject a code calling the following method in
	//the beginning of the method getMouseOver(float par1) in class EntityRenderer.
	//The primary purpose of these two methods is to fix the problem with block selection
	//due to camera offset done in AfterCameraTransformation class.
	public static void onMethodStart() {
		Minecraft mc = IFPClientProxy.mc;
		
		if(mc.renderViewEntity != null && mc.gameSettings != null && mc.gameSettings.thirdPersonView == 0)
		{
			shouldRestore = true;
			EntityLivingBase viewEntity = mc.renderViewEntity;
			tempPosX = viewEntity.posX;
			tempPosY = viewEntity.posY;
			tempPosZ = viewEntity.posZ;
			tempPrevPosX = viewEntity.prevPosX;
			tempPrevPosY = viewEntity.prevPosY;
			tempPrevPosZ = viewEntity.prevPosZ;
			viewEntity.posX += ActiveRenderInfo.objectX;
			viewEntity.posY += ActiveRenderInfo.objectY;
			viewEntity.posZ += ActiveRenderInfo.objectZ;
			viewEntity.prevPosX = viewEntity.posX;
			viewEntity.prevPosY = viewEntity.posY;
			viewEntity.prevPosZ = viewEntity.posZ;	
		} else {
			shouldRestore = false;
		}
		
	}
	
	//Class transformer is used to inject a code calling the following method in
	//the end of the method getMouseOver(float par1) in class EntityRenderer.
	public static void onMethodEnd() {
		if(shouldRestore)
		{
			EntityLivingBase viewEntity = IFPClientProxy.mc.renderViewEntity;
			viewEntity.posX = tempPosX;
			viewEntity.posY = tempPosY;
			viewEntity.posZ = tempPosZ;
			viewEntity.prevPosX = tempPrevPosX;
			viewEntity.prevPosY = tempPrevPosY;
			viewEntity.prevPosZ = tempPrevPosZ;
		}
	}
}
