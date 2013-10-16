package kes5219.improvedfirstperson.classtransformer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ObfTable {
	static boolean isObfuscated;
		
	static String ClassItemRenderer;
	static String MethodRenderItemInFirstPerson;
	
	static String ClassEntityRenderer;
	static String FieldRendererUpdateCount;
	static String MethodRenderWorld;
	static String MethodRenderWorldDesc;
	static String MethodSetupCameraTransform;
	static String MethodSetupCameraTransformDesc;
	static String MethodGetMouseOver;
	static String MethodGetMouseOverDesc;
	
	static String ClassRenderFish;
	static String MethodDoRenderFishHook;
	static String MethodDoRenderFishHookDesc;
	
	static String ClassRenderGlobal;
	static String MethodRenderEntities;
	static String MethodRenderEntitiesDesc;
	
	static String ClassGuiIngame;
	static String MethodRenderGameOverlay;
	static String MethodRenderGameOverlayDesc;

	static String ClassItemEditableBook;
	static String ClassItemWritableBook;
	static String MethodOnItemRightClick;
	static String MethodOnItemRightClickDesc;

	static String ClassEntityLivingBase;
	static String ClassEntityPlayer;
	static String MethodOnUpdate;
	static String MethodOnUpdateDesc;
	static String FieldRenderYawOffset;

	static String ClassAnimatedPlayer;
	static String MethodOnClientTickAnimatedPlayerMod;
	static String MethodOnClientTickAnimatedPlayerModDesc;
	
	static String ClassModelPlayerAnimatedPlayerMod;
	static String MethodAnimateAnimatedPlayerMod;
	static String MethodAnimateAnimatedPlayerModDesc;
	
}
