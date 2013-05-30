package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodProfilerTransformer extends ClassVisitor {
	String profilerSection;
	String tgtMethodName;
	String tgtMethodDesc;
	String hookMethodOwner;
	String hookMethodName;
	ICustomProfilerSectionHook template;
	
	public MethodProfilerTransformer(ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if(name.equals(tgtMethodName) && desc.startsWith(tgtMethodDesc)) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			return new MethodProfilerAdaptor(mv);
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

	private class MethodProfilerAdaptor extends MethodVisitor {
		
		public MethodProfilerAdaptor(MethodVisitor mv) {
			super(Opcodes.ASM4, mv);
			//mv.visitLdcInsn(cst)
		}
		
		@Override
		public void visitLdcInsn(Object cst) {
			if(cst instanceof String && ((String)cst).equals(profilerSection)) {
				if(template != null) {
					template.onDesignatedSection(mv);
				} else {
					//if using simple hook
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, hookMethodOwner, hookMethodName, "()V");
				}
			}
			mv.visitLdcInsn(cst);
		}
	}
}
