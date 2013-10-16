package kes5219.improvedfirstperson.hooks;

import thehippomaster.AnimatedPlayer.PlayerData;
import thehippomaster.AnimatedPlayer.client.ModelPlayer;
import kes5219.improvedfirstperson.client.IFPClientProxy;
import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class AnimatedPlayerHooks {
	
	public static void beforeAnimatedPlayerRender(EntityPlayer player, ModelPlayer model)
	{
		Minecraft mc = IFPClientProxy.getMC();
		
		model.head.isHidden = ModImprovedFirstPerson.enableBodyRender &&
				mc.gameSettings.thirdPersonView == 0 &&
				player == mc.renderViewEntity &&
				RenderManager.instance.playerViewY != 180;
	}
	
	public static int getJumpTickIncrementDown(PlayerData data)
	{
		if (data.jumpTick > 4)
		{
			return 4;
		}
		else
		{
			switch (data.jumpTick)
			{
			case 4:
				return 2;
			case 2:
				return 1;
			case 1:
				return 0;
			}
		}
		
		return MathHelper.clamp_int(data.jumpTick - 1, 0, 4);
	}
	
    public static void doAnimatedPlayerTransforms(EntityPlayer player, Render render, float limbSwing, float limbSwingAmount, float existedPartial, float headOffset, float pitch, float partialTick)
    {
    	thehippomaster.AnimatedPlayer.client.RenderPlayer apRender = (thehippomaster.AnimatedPlayer.client.RenderPlayer)render;
    	ModelPlayer model = apRender.playerModel;
        PlayerData data = PlayerData.getPlayerData(player);
        
        model.partialTick = partialTick;
        model.setAngles(data);
        model.animate(player, data, limbSwing, limbSwingAmount, existedPartial, headOffset, pitch, 0.0625F);
        
        AfterCameraTransformation.doModelRenderTransforms(model.pelvis, true);
        AfterCameraTransformation.doModelRenderTransforms(model.chest, true);
        AfterCameraTransformation.doModelRenderTransforms(model.head, false);
        AfterCameraTransformation.undoRotation(model.chest);
        AfterCameraTransformation.undoRotation(model.pelvis);
    }
	
}
