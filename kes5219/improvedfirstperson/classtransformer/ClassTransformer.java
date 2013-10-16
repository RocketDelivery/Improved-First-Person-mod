package kes5219.improvedfirstperson.classtransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import thehippomaster.AnimatedPlayer.PlayerData;
import thehippomaster.AnimatedPlayer.client.ModelPlayer;

import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.utils.classtransformhelper.ClassTransformHelper;
import kes5219.utils.classtransformhelper.CustomMethodTransformer;
import kes5219.utils.classtransformhelper.MethodTransformer;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;


public class ClassTransformer implements IClassTransformer {
	
	/*public static byte[] addMethod(
			byte[] byteCode,
			String addToClass,
			String addMethodName,
			String addMethodDesc) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		classWriter.newMethod(addToClass, addMethodName, addMethodDesc, itf)
		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
	}*/

	//Injects codes into the base class files during initialization, making it possible to gain access to core parts
	//of Minecraft without directly modifying the base class files.
	
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (name.equals(ObfTable.ClassItemRenderer))
		{
			bytes = ClassTransformHelper.injectSimpleHook(bytes, true, ObfTable.MethodRenderItemInFirstPerson, "(F)V", "kes5219/improvedfirstperson/hooks/ItemRendererHook", "shouldRenderItemInFirstPerson");
			System.out.println("Improved First Person Mod: Successfully modified renderItemInFirstPerson in ItemRenderer");
		}
		else if (name.equals(ObfTable.ClassEntityRenderer))
		{
			bytes = ClassTransformHelper.changeFieldAcess(bytes, ObfTable.FieldRendererUpdateCount, Opcodes.ACC_PUBLIC);
			System.out.println("Improved First Person Mod: Successfully changed the field rendererUpdateCount to public in EntityRenderer");

			bytes = ClassTransformHelper.injectCustomHook(bytes, new AfterCameraTransformTransformer(), ObfTable.MethodSetupCameraTransform, ObfTable.MethodSetupCameraTransformDesc);
			System.out.println("Improved First Person Mod: Successfully injected hook into setupCameraTransform in EntityRenderer");
			
			bytes = ClassTransformHelper.injectCustomHook(bytes, new MethodGetMouseOverTransformer(), ObfTable.MethodGetMouseOver, ObfTable.MethodGetMouseOverDesc);
			System.out.println("Improved First Person Mod: Successfully injected hook into getMouseOver in EntityRenderer");
		}
		else if (name.equals(ObfTable.ClassRenderFish))
		{
			bytes = ClassTransformHelper.injectCustomHook(bytes, new MethodDoRenderFishHookTransformer(), ObfTable.MethodDoRenderFishHook, ObfTable.MethodDoRenderFishHookDesc);
			System.out.println("Improved First Person Mod: Successfully modified doRenderFishHook in RenderFish");
		}
		else if (name.equals(ObfTable.ClassRenderGlobal))
		{
			bytes = ClassTransformHelper.injectSimpleHookAtProfilerSection(bytes, ObfTable.MethodRenderEntities, ObfTable.MethodRenderEntitiesDesc, "kes5219/improvedfirstperson/hooks/RenderEntityHook", "onRenderEntities", "tileentities");
			System.out.println("Improved First Person Mod: Successfully modified renderEntities in RenderGlobal");
		}
		else if (name.equals(ObfTable.ClassItemEditableBook) || name.equals(ObfTable.ClassItemWritableBook))
		{
			bytes = ClassTransformHelper.injectCustomHook(bytes, new MethodOnItemRightClickTransformer(),
					ObfTable.MethodOnItemRightClick, ObfTable.MethodOnItemRightClickDesc);
			System.out.println("Improved First Person Mod: Successfully modified onItemRightClick in " +
					(name.equals(ObfTable.ClassItemEditableBook) ? "ItemEditableBook" : "ItemWritableBook"));
			
			/*bytes = ClassTransformHelper.injectCustomHook(bytes, new MethodOnItemRightClickTransformer(),
					ObfuscationTable.MethodOnItemRightClick, ObfuscationTable.MethodOnItemRightClickDesc);
			System.out.println("Improved First Person Mod: Successfully modified onItemRightClick in " +
					(name.equals(ObfuscationTable.ClassItemEditableBook) ? "ItemEditableBook" : "ItemWritableBook"));*/
		}
		else if (name.equals(ObfTable.ClassEntityLivingBase))
		{
			bytes = ClassTransformHelper.injectCustomHook(bytes, new PlayerPreRotationTransformer(),
					ObfTable.MethodOnUpdate, ObfTable.MethodOnUpdateDesc, ObfTable.ClassEntityLivingBase.replace('.', '/'));
			
			bytes = ClassTransformHelper.injectCustomHook(bytes, new PlayerPostRotationTransformer(),
					ObfTable.MethodOnUpdate, ObfTable.MethodOnUpdateDesc, ObfTable.ClassEntityLivingBase.replace('.', '/'));
			System.out.println("Improved First Person Mod: Successfully modified onUpdate in EntityLivingBase");
		}
		else if (name.equals(ObfTable.ClassModelPlayerAnimatedPlayerMod))
		{
			bytes = ClassTransformHelper.injectCustomHook(bytes, new AnimatedPlayerAnimateTransformer(),
					ObfTable.MethodAnimateAnimatedPlayerMod, ObfTable.MethodAnimateAnimatedPlayerModDesc);
			System.out.println("Improved First Person Mod: Successfully injected code into animate in ModelPlayer from the Animated Player mod");
			
			bytes = ClassTransformHelper.changeFieldAcess(bytes, "partialTick", Opcodes.ACC_PUBLIC);
			System.out.println("Improved First Person Mod: Successfully changed the field partialTick to public in ModelPlayer from the Animated Player mod");
		}
		else if (name.equals(ObfTable.ClassAnimatedPlayer))
		{
			bytes = ClassTransformHelper.injectCustomHook(bytes, new AnimatedPlayerModContainerTransformer(),
					ObfTable.MethodOnClientTickAnimatedPlayerMod, ObfTable.MethodOnClientTickAnimatedPlayerModDesc);
			System.out.println("Improved First Person Mod: Successfully injected code into onClientTick in AnimatedPlayer");
		}
		
		return bytes;
	}
	
	private class MethodDoRenderFishHookTransformer extends CustomMethodTransformer {
		 
		public void visitCode() {
			mv.visitCode();
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/RenderFishHook", "onMethodStart", "()V");
		}
		
		public void visitInsn(int opcode) {
			if(opcode == Opcodes.RETURN) {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/RenderFishHook", "onMethodEnd", "()V");
			}
			mv.visitInsn(opcode);
		}
	}
	
	private class MethodGetMouseOverTransformer extends CustomMethodTransformer {
		 
		public void visitCode() {
			mv.visitCode();
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/MouseSelectionOverride", "onMethodStart", "()V");
		}
		
		 
		public void visitInsn(int opcode) {
			if (opcode == Opcodes.RETURN) {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/MouseSelectionOverride", "onMethodEnd", "()V");
			}
			mv.visitInsn(opcode);
		}
	}
	
	private class AfterCameraTransformTransformer extends CustomMethodTransformer {
		
		public void visitInsn(int opcode) {
			if (opcode == Opcodes.RETURN) {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/AfterCameraTransformation", "afterCameraTransform", "()V");
			}
			mv.visitInsn(opcode);
		}
	}
	
	private class PlayerPreRotationTransformer extends CustomMethodTransformer {
		@Override
		public void visitLdcInsn(Object cst) {
			if (cst instanceof String && ((String)cst).equals("headTurn")) {
				try
				{
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitFieldInsn(Opcodes.GETFIELD, ObfTable.ClassEntityLivingBase.replace('.', '/'), ObfTable.FieldRenderYawOffset, "F");
					mv.visitVarInsn(Opcodes.FSTORE, 15);
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
			mv.visitLdcInsn(cst);
		}
	}
	
	private class PlayerPostRotationTransformer extends CustomMethodTransformer {
		@Override
		public void visitLdcInsn(Object cst) {
			if (cst instanceof String && ((String)cst).equals("rangeChecks")) {
				try
				{
					mv.visitVarInsn(Opcodes.ALOAD, 0);
					mv.visitVarInsn(Opcodes.FLOAD, 15);
					mv.visitVarInsn(Opcodes.FLOAD, 6);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/EntityLivingBaseHook", "handleRotation", "(L" + ObfTable.ClassEntityLivingBase.replace('.', '/') + ";FF)V");
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
			mv.visitLdcInsn(cst);
		}
	}
	
	private class MethodOnItemRightClickTransformer extends CustomMethodTransformer {
		
		public void visitCode() {
			mv.visitCode();
			//returns if returned value is not true;
			Label label = new Label();
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/BookHook",
					"cancelOpeningGUI",
					"()Z");
			mv.visitJumpInsn(Opcodes.IFEQ, label);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitLabel(label);
		}
	}
	
	private class AnimatedPlayerAnimateTransformer extends CustomMethodTransformer {
		@Override
		public void visitCode() {
			mv.visitCode();
			
			try
			{
				mv.visitVarInsn(Opcodes.ALOAD, 1);
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/AnimatedPlayerHooks",
						"beforeAnimatedPlayerRender",
						"(L" + ObfTable.ClassEntityPlayer.replace('.', '/') + ";L" + ObfTable.ClassModelPlayerAnimatedPlayerMod.replace('.', '/') + ";)V");
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc)
		{
			boolean skipInsn = false;
			
			if (opcode == Opcodes.PUTFIELD &&
					owner.equals(ObfTable.ClassEntityPlayer.replace('.', '/')) &&
					name.equals(ObfTable.FieldRenderYawOffset))
			{
				mv.visitInsn(Opcodes.POP2);
				skipInsn = true;
			}
			
			if (!skipInsn)
			{
				mv.visitFieldInsn(opcode, owner, name, desc);
			}
		}
	}
	
	private class AnimatedPlayerModContainerTransformer extends CustomMethodTransformer {
		/*private boolean found1 = false;
		private boolean found2 = false;
		private boolean injected = false;*/
		
		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			mv.visitMethodInsn(opcode, owner, name, desc);
			
			/*//mv.visitMethodInsn(INVOKEVIRTUAL, "thehippomaster/AnimatedPlayer/PlayerData", "isAirborne", "(Lnet/minecraft/entity/player/EntityPlayer;)Z");
			if (!found1 && opcode == Opcodes.INVOKEVIRTUAL &&
					name.equals("isAirborne"))
			{
				found1 = true;
			}*/
		}
		
		@Override
		public void visitFrame(int opcode, int nLocal, Object[] local, int nStack, Object[] stack)
		{
			mv.visitFrame(opcode, nLocal, local, nStack, stack);

			/*//mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			if (found1 && !found2 &&
					opcode == Opcodes.F_SAME &&
					nLocal == 0 &&
					nStack == 0)
			{
				found2 = true;
			}*/
		}
		
		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc)
		{
			boolean skipInsn = false;
			
			/*//mv.visitFieldInsn(Opcodes.PUTFIELD, PlayerData.class.getName().replace('.', '/'), "jumpTick", "I");
			if (found2 && !injected &&
					opcode == Opcodes.PUTFIELD &&
					name.equals("jumpTick"))
			{
				try
				{
					mv.visitInsn(Opcodes.POP);
					mv.visitVarInsn(Opcodes.ALOAD, 4);
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/AnimatedPlayerHooks", "getJumpTickIncrementDown", "(L" + PlayerData.class.getName().replace('.', '/') + ";)I");
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
				
				injected = true;
			}*/
			
			//mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "renderYawOffset", "F");
			if (opcode == Opcodes.PUTFIELD &&
					owner.equals(ObfTable.ClassEntityPlayer.replace('.', '/')) &&
					name.equals(ObfTable.FieldRenderYawOffset))
			{
				mv.visitInsn(Opcodes.POP2);
				skipInsn = true;
			}
			
			if (!skipInsn)
			{
				mv.visitFieldInsn(opcode, owner, name, desc);
			}
		}
	}
}
