package kes5219.improvedfirstperson.classtransformer;

import java.util.Map;

import net.minecraft.world.World;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@TransformerExclusions({"kes5219.improvedfirstperson.classtransformer"})
@MCVersion("1.6.4")
public class ImprovedFirstPersonLoadingPlugin implements IFMLLoadingPlugin {
	
	public String[] getLibraryRequestClass() {
		return null;
	}
	
	public String[] getASMTransformerClass() {
		boolean isObfuscated = !World.class.getSimpleName().equals("World");
		
		ObfTable.ClassItemRenderer = isObfuscated ? "bfj" : "net.minecraft.client.renderer.ItemRenderer";
		ObfTable.MethodRenderItemInFirstPerson = isObfuscated ? "a" : "renderItemInFirstPerson";
		
		ObfTable.ClassEntityRenderer = isObfuscated ? "bfe" : "net.minecraft.client.renderer.EntityRenderer";
		ObfTable.FieldRendererUpdateCount = isObfuscated ? "field_78529_t" : "rendererUpdateCount";
		ObfTable.MethodRenderWorld = isObfuscated ? "a" : "renderWorld";
		ObfTable.MethodRenderWorldDesc = "(FJ)V";
		ObfTable.MethodGetMouseOver = isObfuscated ? "a" : "getMouseOver";
		ObfTable.MethodGetMouseOverDesc = "(F)V";
		ObfTable.MethodSetupCameraTransform = isObfuscated ? "a" : "setupCameraTransform";
		ObfTable.MethodSetupCameraTransformDesc = "(FI)V";
		
		ObfTable.ClassRenderFish = isObfuscated ? "bgq" : "net.minecraft.client.renderer.entity.RenderFish";
		ObfTable.MethodDoRenderFishHook = isObfuscated ? "a" : "doRenderFishHook";
		ObfTable.MethodDoRenderFishHookDesc = isObfuscated ? "(Lul;DDDFF)V" : "(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V";
		
		ObfTable.ClassRenderGlobal = isObfuscated ? "bfl" : "net.minecraft.client.renderer.RenderGlobal";
		ObfTable.MethodRenderEntities = isObfuscated ? "a" : "renderEntities";		
		ObfTable.MethodRenderEntitiesDesc = isObfuscated ? "(Latc;Lbft;F)V" : "(Lnet/minecraft/util/Vec3;Lnet/minecraft/client/renderer/culling/ICamera;F)V";

		ObfTable.ClassItemEditableBook = isObfuscated ? "zo" : "net.minecraft.item.ItemEditableBook";
		ObfTable.ClassItemWritableBook = isObfuscated ? "zn" : "net.minecraft.item.ItemWritableBook";
		ObfTable.MethodOnItemRightClick = isObfuscated ? "a" : "onItemRightClick";
		ObfTable.MethodOnItemRightClickDesc = isObfuscated ? "(Lye;Labw;Luf;)Lye;" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;";

		ObfTable.ClassEntityLivingBase = isObfuscated ? "of" : "net.minecraft.entity.EntityLivingBase";
		ObfTable.ClassEntityPlayer = isObfuscated ? "uf" : "net.minecraft.entity.player.EntityPlayer";
		ObfTable.MethodOnUpdate = isObfuscated ? "l_" : "onUpdate";
		ObfTable.MethodOnUpdateDesc = "()V";
		ObfTable.FieldRenderYawOffset = isObfuscated ? "field_70761_aq" : "renderYawOffset";
		
		ObfTable.ClassAnimatedPlayer = "thehippomaster.AnimatedPlayer.AnimatedPlayer";
		ObfTable.MethodOnClientTickAnimatedPlayerMod = "onClientTick";
		ObfTable.MethodOnClientTickAnimatedPlayerModDesc = "(Lnet/minecraft/world/World;)V";
		
		ObfTable.ClassModelPlayerAnimatedPlayerMod = "thehippomaster.AnimatedPlayer.client.ModelPlayer";
		ObfTable.MethodAnimateAnimatedPlayerMod = "animate";
		ObfTable.MethodAnimateAnimatedPlayerModDesc = "(Lnet/minecraft/entity/player/EntityPlayer;Lthehippomaster/AnimatedPlayer/PlayerData;FFFFFF)V";
		
		return new String[] {"kes5219.improvedfirstperson.classtransformer.ClassTransformer"};
	}
	
	public String getModContainerClass() {
		return null;
	}
	
	public String getSetupClass() {
		return null;
	}
	
	public void injectData(Map<String, Object> data) {
		
	}
	
}
