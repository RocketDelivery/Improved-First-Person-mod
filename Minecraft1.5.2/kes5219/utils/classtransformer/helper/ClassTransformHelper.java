package kes5219.utils.classtransformer.helper;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassTransformHelper {
	public static byte[] injectSimpleHook(
			byte[] byteCode, 
			boolean usesConditionalReturn, 
			String tgtMethodName, 
			String tgtMethodDesc,
			String hookMethodOwner,
			String hookMethodName) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodTransformer transformer = new MethodTransformer(classWriter);
		
		transformer.tgtMethodName = tgtMethodName;
		transformer.tgtMethodDesc = tgtMethodDesc;
		transformer.hookMethodOwner = hookMethodOwner;
		transformer.hookMethodName = hookMethodName;
		transformer.isConditionalReturn = usesConditionalReturn;
		transformer.template = null;
		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
	}
	
	public static byte[] injectCustomHook(
			byte[] byteCode,
			CustomMethodTransformer template,
			String tgtMethodName, 
			String tgtMethodDesc) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodTransformer transformer = new MethodTransformer(classWriter);
		
		transformer.tgtMethodName = tgtMethodName;
		transformer.tgtMethodDesc = tgtMethodDesc;
		if(template == null) {
			return byteCode;
		} else {
			transformer.template = template;
		}
		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
	}
	
	public static byte[] changeFieldAcess(
			byte[] byteCode,
			String fieldName,
			int fieldAccess) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		FieldTransformer transformer = new FieldTransformer(classWriter);
		
		transformer.fieldName = fieldName;
		transformer.fieldAccess = fieldAccess;

		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
	}
	
	/*public static byte[] changeField(
			byte[] byteCode,
			String fieldName,
			CustomFieldTransformer template) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(template, 0);
		return classWriter.toByteArray();
	}*/
	
	public static byte[] injectSimpleHookAtProfilerSection(
			byte[] byteCode,
			String tgtMethodName,
			String tgtMethodDesc,
			String hookMethodOwner,
			String hookMethodName,
			String profilerSection) {
		ClassWriter classWriter = new ClassWriter(0);
		MethodProfilerTransformer transformer = new MethodProfilerTransformer(classWriter);
		
		transformer.tgtMethodName = tgtMethodName;
		transformer.tgtMethodDesc = tgtMethodDesc;
		transformer.hookMethodOwner = hookMethodOwner;
		transformer.hookMethodName = hookMethodName;
		transformer.profilerSection = profilerSection;
		transformer.template = null;
		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
		
		/*ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(byteCode);
		cr.accept(cn, 0);
		
		Iterator<MethodNode> iter = cn.methods.iterator();
		
		while(iter.hasNext()) {
			MethodNode mn = iter.next();
			if(mn.name.equals(tgtMethodName) && mn.desc.equals(tgtMethodDesc)) {
				Iterator<AbstractInsnNode> nodeIter = mn.instructions.iterator();
				
				while(nodeIter.hasNext()) {
					AbstractInsnNode node = nodeIter.next();
					if (node.getOpcode() == Opcodes.LDC) {
						
						LdcInsnNode ldcNode = (LdcInsnNode)node;
						if(ldcNode.cst.equals(profilerSection)) {
							AbstractInsnNode insertBefore = ldcNode.getNext().getNext();
							mn.instructions.insertBefore(insertBefore, new MethodInsnNode(Opcodes.INVOKESTATIC, 
																							hookMethodOwner,
																							hookMethodName,
																							"()V"));
						} else continue;
					}
				}
			}
		}
		ClassWriter cw = new ClassWriter(0);
		cn.accept(cw);
		return cw.toByteArray();*/
	}
	
	public static byte[] injectCustomeHookAtProfilerSection(
			byte[] byteCode,
			ICustomProfilerSectionHook template,
			String tgtMethodName,
			String tgtMethodDesc,
			String profilerSection) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodProfilerTransformer transformer = new MethodProfilerTransformer(classWriter);
		
		transformer.tgtMethodName = tgtMethodName;
		transformer.tgtMethodDesc = tgtMethodDesc;
		transformer.profilerSection = profilerSection;
		if(template == null) {
			return byteCode;
		} else {
			transformer.template = template;
		}
		
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(transformer, 0);
		return classWriter.toByteArray();
	}
}
