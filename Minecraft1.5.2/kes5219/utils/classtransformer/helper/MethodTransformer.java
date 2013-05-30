package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodTransformer extends ClassVisitor {
	String tgtMethodName;
	String tgtMethodDesc;
	String hookMethodOwner;
	String hookMethodName;
	boolean isConditionalReturn;
	CustomMethodTransformer template;
	
	MethodTransformer(ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
	}
	
	 
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if(name.equals(tgtMethodName) && desc.startsWith(tgtMethodDesc)) {
			if(template != null) {
				MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
				template.init(mv);
				return template;
			} else {
				MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
				return new MethodAdaptor(mv);
			}
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}
	
	private class MethodAdaptor extends MethodVisitor {
		
		public MethodAdaptor(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
		}
		
		 
		public void visitCode() {
			mv.visitCode();
			if(isConditionalReturn) {
				//returns if returned value is not true;
				Label label0 = new Label();
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, hookMethodOwner, hookMethodName, "()Z");
				mv.visitJumpInsn(Opcodes.IFNE, label0);
				mv.visitInsn(Opcodes.RETURN);
				mv.visitLabel(label0);
			} else {
				mv.visitMethodInsn(Opcodes.INVOKESTATIC, hookMethodOwner, hookMethodName, "()V");
			}
		}
	}
}
