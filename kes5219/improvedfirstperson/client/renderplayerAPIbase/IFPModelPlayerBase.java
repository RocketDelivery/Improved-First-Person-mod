package kes5219.improvedfirstperson.client.renderplayerAPIbase;

import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.utils.misc.PartialTickRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemMapBase;
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
		EntityPlayer player = (EntityPlayer)entity;
		//modelPlayer.bipedRightLeg.rotationPointZ = 0.0f;
		//modelPlayer.bipedRightLeg.rotationPointX = -2.0f;
		//modelPlayer.bipedLeftLeg.rotationPointZ = 0.0f;
		//modelPlayer.bipedLeftLeg.rotationPointX = 2.0f;
		ItemStack item = player.getHeldItem();

		if (item != null && Item.itemsList[item.itemID] instanceof ItemBow) {
			modelPlayer.heldItemLeft = 1;
			modelPlayer.heldItemRight = 0;
		}
	}

	@Override
	public void afterSetRotationAngles(float legSwing, float legYaw, float ticksExistedPartial, float headYawOffset, float pitch, float scale, Entity entity) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = (EntityPlayer)entity;
		float partialTick = PartialTickRetriever.getPartialTick();

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
			float rotationYaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTick; 
			player.renderYawOffset = rotationYaw + 40;
			modelPlayer.bipedLeftArm.rotateAngleY += 0.15F;

			ticksExistedPartial = ticksExistedPartial * 6.0f;
			//modelPlayer.bipedRightArm.rotateAngleZ += 0.15F * (MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F);
			modelPlayer.bipedLeftArm.rotateAngleZ -= 0.15F * (MathHelper.cos(ticksExistedPartial * 0.09F) * 0.05F + 0.05F);
			//modelPlayer.bipedRightArm.rotateAngleX += 0.15F * (MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F);
			modelPlayer.bipedLeftArm.rotateAngleX -= 0.15F * (MathHelper.sin(ticksExistedPartial * 0.067F) * 0.05F);

			float headAngle = modelPlayer.bipedHead.rotateAngleX * 15;
			float rot = headAngle / 45;

			rot *= rot * 2.5F;

			if (headAngle > 0)
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
		if (entity.rotationPitch > 0 && !modelPlayer.isRiding)
		{
			float off;
			float fovMult = ((mc.thePlayer != entity || mc.gameSettings.thirdPersonView > 0) ? 0.75F : (mc.gameSettings.fovSetting + 1));

			if (fovMult > 1.75F)
				fovMult = 1.75F;
			
			ItemStack heldItem = player.getHeldItem();
			boolean map = heldItem != null && (heldItem.getItem() instanceof ItemMapBase);

			if (!map)
			{
				off = Math.abs(entity.rotationPitch / 250F * fovMult);
				
				if (player.isUsingItem())
					off /= ((player.getItemInUseDuration() + partialTick) / 4) + 1;
				
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
		
		if (player.isUsingItem())
		{
			ItemStack heldItemStack = player.getHeldItem();
			
			if (heldItemStack != null)
			{
				Item heldItem = heldItemStack.getItem();
				EnumAction action = heldItem.getItemUseAction(heldItemStack);
				
				if (action == EnumAction.eat || action == EnumAction.drink)
				{
                    float useLeftPartial = (float)player.getItemInUseCount() + 1 - partialTick;
                    float progress = useLeftPartial / (float)heldItemStack.getMaxItemUseDuration();
                    float moveAmount = progress * progress * progress;
                    moveAmount = moveAmount * moveAmount * moveAmount;
                    moveAmount = moveAmount * moveAmount * moveAmount;
                    moveAmount = 1.0F - moveAmount;
					
					modelPlayer.bipedRightArm.rotateAngleX -= moveAmount * 1.25F;
					modelPlayer.bipedRightArm.rotateAngleY -= moveAmount / 2;
					modelPlayer.bipedRightArm.rotateAngleZ += moveAmount / 2;

					float bodyYaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTick;
					float headYaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTick;
					float offset = (headYaw - bodyYaw) / 80 * moveAmount;

					modelPlayer.bipedRightArm.rotateAngleX += offset / 2.5F;
					modelPlayer.bipedRightArm.rotateAngleY += offset + 0.2F;

					modelPlayer.bipedRightArm.rotationPointZ += moveAmount;
					modelPlayer.bipedRightArm.rotationPointZ += (offset - 0.2F) * 2;
					
					float headPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTick;
					offset = headPitch / 180;
					
					modelPlayer.bipedRightArm.rotateAngleY -= offset;
					
					if (offset > 0)
						offset /= 1.5F;

					modelPlayer.bipedRightArm.rotateAngleX += offset;
				}
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
