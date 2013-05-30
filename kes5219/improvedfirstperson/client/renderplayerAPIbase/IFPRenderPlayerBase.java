package kes5219.improvedfirstperson.client.renderplayerAPIbase;

import kes5219.utils.misc.PartialTickRetriever;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.src.ModelPlayer;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.src.RenderPlayerBase;
import net.minecraft.util.MathHelper;

public class IFPRenderPlayerBase extends RenderPlayerBase {
	public IFPRenderPlayerBase(RenderPlayerAPI renderPlayerAPI) {
		super(renderPlayerAPI);
	}

	/* 
	public void drawFirstPersonHand(EntityPlayer entityPlayer) {
		new Exception().printStackTrace();
		//does not get called at all since the method renderItemInFirstPerson is modified
		//do nothing
	}*/
	
	 
	public void renderSpecialHeadArmor(EntityPlayer var1, float var2) {
		Minecraft mc = Minecraft.getMinecraft();
		/*if(mc.gameSettings.thirdPersonView > 0 && var1 == mc.renderViewEntity) {
			renderPlayer.localRenderSpecialHeadArmor(var1, var2);
		}*/
		if(var1 != mc.renderViewEntity ||
			mc.gameSettings.thirdPersonView > 0 || 
			RenderManager.instance.playerViewY == 180.0f/*if the inventory is open*/) {
			renderPlayer.localRenderSpecialHeadArmor(var1, var2);
		}
	}

	private static final int swingRotation = -130;
	private static final int swingRotationWindup = 35;
	private static final float swingCancel = 0.7F;
	
	public void afterPositionSpecialItemInHand(EntityPlayer var1, float var2, EnumAction var3, ItemStack var4) {
		//renders bow on player's left hand
		if (Item.itemsList[var4.itemID] instanceof ItemBow) {
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			renderPlayer.getModelBipedMainField().bipedLeftArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            
            float var5 = 0.625F;
            GL11.glTranslatef(0.05F, -0.05F, 0.2F);
            //GL11.glRotatef(-5.0F, 0.0F, 1.0F, 0.0F);
            float zRot = 15F * renderPlayer.getModelBipedMainField().bipedHead.rotateAngleX;
            GL11.glScalef(var5, -var5, var5);
            GL11.glRotatef(-120.0F + zRot, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(10F + zRot, 0.0F, 1.0F, 0.0F); //Y-axis
		}
		else if (var1.isSwingInProgress && var1.swingProgress > 0)
		{
			Item heldItem = var1.getHeldItem().getItem();
			
			if (heldItem.isFull3D())
			{
				float partialTick = PartialTickRetriever.getPartialTick();
				float actualSwing = var1.getSwingProgress(partialTick);
				float rot = actualSwing * swingRotation + swingRotationWindup;
				
				float cancel = actualSwing - swingCancel;
				
				if (cancel > 0)
				{
					cancel = cancel / (1 - swingCancel) * swingRotation;
					rot -= cancel;
				}
				
	            GL11.glRotatef(rot, 1, 0, 0.6F);
			}
		}
	}
}
