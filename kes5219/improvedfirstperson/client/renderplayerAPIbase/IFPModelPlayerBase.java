package kes5219.improvedfirstperson.client.renderplayerAPIbase;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModelPlayer;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.ModelPlayerBase;
import net.minecraft.util.MathHelper;

public class IFPModelPlayerBase  extends ModelPlayerBase {
	
	private float prevStrafeOffset = 0;
	
	public IFPModelPlayerBase(ModelPlayerAPI modelPlayerAPI) {
		super(modelPlayerAPI);
	}

	@Override
	public void beforeSetRotationAngles(float legSwing, float legYaw, float ticksExistedPartial, float headYawOffset, float pitch, float scale, Entity entity)
	{
		//modelPlayer.bipedRightLeg.rotationPointZ = 0.0f;
		//modelPlayer.bipedRightLeg.rotationPointX = -2.0f;
		//modelPlayer.bipedLeftLeg.rotationPointZ = 0.0f;
		//modelPlayer.bipedLeftLeg.rotationPointX = 2.0f;
		ItemStack item = ((EntityLiving)entity).getHeldItem();

		if(item != null && Item.itemsList[item.itemID] instanceof ItemBow) {
			modelPlayer.heldItemLeft = 1;
			modelPlayer.heldItemRight = 0;
		}
	}

	@Override
	public void afterSetRotationAngles(float legSwing, float legYaw, float ticksExistedPartial, float headYawOffset, float pitch, float scale, Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = (EntityPlayer)entity;

		if (RenderManager.instance.playerViewY == 180.0f) {
			//if the inventory screen is open
			modelPlayer.bipedHead.isHidden = false;
			modelPlayer.bipedHeadwear.isHidden = false;
			return;
		}

		if ((mc.gameSettings.thirdPersonView == 0 && entity == mc.renderViewEntity) || mc.renderViewEntity.isPlayerSleeping()) {
			modelPlayer.bipedHead.isHidden = true;
			modelPlayer.bipedHeadwear.isHidden = true;
		} else {
			modelPlayer.bipedHead.isHidden = false;
			modelPlayer.bipedHeadwear.isHidden = false;
		}

		ItemStack item = player.getHeldItem();

		if (item != null && (Item.map.itemID == item.itemID || Item.emptyMap.itemID == item.itemID)) {
			modelPlayer.bipedRightArm.rotateAngleX = -(float)Math.PI/9;
			modelPlayer.bipedLeftArm.rotateAngleX = -(float)Math.PI/9;
			modelPlayer.bipedRightArm.rotateAngleZ = 0;
			modelPlayer.bipedLeftArm.rotateAngleZ = 0;

			modelPlayer.bipedRightArm.rotateAngleZ += MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F;
			modelPlayer.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F;
			modelPlayer.bipedRightArm.rotateAngleX += MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F;
			modelPlayer.bipedLeftArm.rotateAngleX -= MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F;
		}

		if (modelPlayer.aimedBow) {
			player.renderYawOffset = player.rotationYawHead + 40;
			modelPlayer.bipedLeftArm.rotateAngleY += 0.15F;

			ticksExistedPartial = ticksExistedPartial * 6.0f;
			modelPlayer.bipedRightArm.rotateAngleZ += 0.15F * (MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F);
			modelPlayer.bipedLeftArm.rotateAngleZ -= 0.15F * (MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F);
			modelPlayer.bipedRightArm.rotateAngleX += 0.15F * (MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F);
			modelPlayer.bipedLeftArm.rotateAngleX -= 0.15F * (MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F);

			float headAngle = modelPlayer.bipedHead.rotateAngleX * 15;
			float rot = headAngle / 45;
			boolean negative = rot < 0;

			rot *= rot * 2.5F;

			if (!negative)
			{
				modelPlayer.bipedLeftArm.rotateAngleY += rot;

				rot -= 0.5F;
				rot *= 0.75F;

				if (rot > 0)
					modelPlayer.bipedLeftArm.rotateAngleX -= rot;
			}
			else
			{
				modelPlayer.bipedLeftArm.rotateAngleZ -= rot;
				modelPlayer.bipedLeftArm.rotateAngleX += rot;
				modelPlayer.bipedRightArm.rotateAngleX += rot * 2.75F;
			}
			
			if (mc.gameSettings.thirdPersonView == 0 && player instanceof EntityPlayerSP)
			{
				EntityPlayerSP playerSP = (EntityPlayerSP)player;
				float strafe = playerSP.movementInput.moveStrafe;
				
				float div = Math.max(2, MathHelper.sqrt_float(Math.abs(modelPlayer.bipedHead.rotateAngleX * 40)));
				float targetOffset = strafe / div;
				prevStrafeOffset = prevStrafeOffset + (targetOffset - prevStrafeOffset) * 0.025F;
				
				modelPlayer.bipedLeftArm.rotateAngleZ += prevStrafeOffset;
			}
		}

		if (item == null || Item.itemsList[item.itemID] instanceof ItemBow) {
			modelPlayer.heldItemLeft = 0;
		}

		// Make player lean over when looking down
		if (entity.rotationPitch > 0)
		{
			float off;
			float fovMult = ((mc.thePlayer != entity || mc.gameSettings.thirdPersonView > 0) ? 0.75F : (mc.gameSettings.fovSetting + 1));

			if (fovMult > 1.75F)
				fovMult = 1.75F;

			if (!player.isUsingItem())
			{
				off = Math.abs(entity.rotationPitch / 250F * fovMult);
				modelPlayer.bipedLeftArm.rotateAngleZ -= off;
				modelPlayer.bipedRightArm.rotateAngleZ += off;
			}

			if (!modelPlayer.isSneak)
			{
				off = entity.rotationPitch / 180F * fovMult;
				modelPlayer.bipedBody.rotateAngleX += off;

				off = entity.rotationPitch / 16F * fovMult;
				modelPlayer.bipedRightLeg.rotationPointZ += off;
				modelPlayer.bipedLeftLeg.rotationPointZ += off;

				off = Math.abs(entity.rotationPitch / 50F * fovMult);
				modelPlayer.bipedRightLeg.rotationPointY -= off;
				modelPlayer.bipedLeftLeg.rotationPointY -= off;
			}
		}

		//modelPlayer.bipedLeftArm.rotateAngleZ = 0;
		//modelPlayer.bipedLeftArm.rotateAngleX = 0;
		//modelPlayer.bipedLeftArm.rotateAngleY = var1 * 0.001f;
	}

	ModelPlayer getModelPlayer()
	{
		return modelPlayer;
	}

}
