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

import kes5219.improvedfirstperson.common.ModImprovedFirstPerson;
import kes5219.improvedfirstperson.hooks.CrosshairRenderHook;
import kes5219.utils.classtransformhelper.ClassTransformHelper;
import kes5219.utils.classtransformhelper.CustomMethodTransformer;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;


public class ClassTransformer implements IClassTransformer {

	//Injects codes into the base class files during initialization, making it possible to gain access to core parts
	//of Minecraft without directly modifying the base class files.
	 
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(name.equals(ObfuscationTable.ClassItemRenderer)) {
			byte[] returnVal =  ClassTransformHelper.injectSimpleHook(bytes, true, ObfuscationTable.MethodRenderItemInFirstPerson, "(F)", "kes5219/improvedfirstperson/hooks/ItemRendererHook", "shouldRenderItemInFirstPerson");
			System.out.println("Improved First Person Mod: Successfully modified renderItemInFirstPerson in ItemRenderer");
			return returnVal;
		} else
		if(name.equals(ObfuscationTable.ClassEntityRenderer)) {
			byte[] tempByte1 =  ClassTransformHelper.changeFieldAcess(bytes, ObfuscationTable.FieldRendererUpdateCount, Opcodes.ACC_PUBLIC);
			System.out.println("Improved First Person Mod: Successfully changed the field rendererUpdateCount to public in EntityRenderer");
						
			byte[] tempByte2 = ClassTransformHelper.injectSimpleHookAtProfilerSection(tempByte1, ObfuscationTable.MethodRenderWorld, ObfuscationTable.MethodRenderWorldDesc, "kes5219/improvedfirstperson/hooks/AfterCameraTransformation", "afterCameraTransform", "frustrum");
			System.out.println("Improved First Person Mod: Successfully injected hook into renderWorld in EntityRenderer");
			
			byte[] tempByte3 = ClassTransformHelper.injectCustomHook(tempByte2, new MethodGetMouseOverTransformer(), ObfuscationTable.MethodGetMouseOver, ObfuscationTable.MethodGetMouseOverDesc);
			System.out.println("Improved First Person Mod: Successfully injected hook into getMouseOver in EntityRenderer");
			return tempByte3;
		} else
		if(name.equals(ObfuscationTable.ClassRenderFish)) {
			byte[] returnVal = ClassTransformHelper.injectCustomHook(bytes, new MethodDoRenderFishHookTransformer(), ObfuscationTable.MethodDoRenderFishHook, ObfuscationTable.MethodDoRenderFishHookDesc);
			System.out.println("Improved First Person Mod: Successfully modified doRenderFishHook in RenderFish");
			return returnVal;
		} else
		if(name.equals(ObfuscationTable.ClassRenderGlobal)) {
			System.out.println(ObfuscationTable.MethodRenderEntities + " " + ObfuscationTable.MethodRenderEntitiesDesc);
			byte[] returnVal = ClassTransformHelper.injectSimpleHookAtProfilerSection(bytes, ObfuscationTable.MethodRenderEntities, ObfuscationTable.MethodRenderEntitiesDesc, "kes5219/improvedfirstperson/hooks/RenderEntityHook", "onRenderEntities", "tileentities");
			System.out.println("Improved First Person Mod: Successfully modified renderEntities in RenderGlobal");
			return returnVal;
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
			if(opcode == Opcodes.RETURN) {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, "kes5219/improvedfirstperson/hooks/MouseSelectionOverride", "onMethodEnd", "()V");
			}
			mv.visitInsn(opcode);
		}
	}
}
