package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class CustomMethodTransformer extends MethodVisitor {
	
	protected CustomMethodTransformer() {
		super(Opcodes.ASM4, null);
	}
	
	final void init(MethodVisitor mv) {
		this.mv = mv;
	}
	
	 
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return super.visitAnnotation(desc, visible);
	}
	
	 
	public AnnotationVisitor visitAnnotationDefault() {
		return super.visitAnnotationDefault();
	}
	
	 
	public void visitAttribute(Attribute attr) {
		super.visitAttribute(attr);
	}
	
	 
	public void visitCode() {
		super.visitCode();
	}
	
	 
	public void visitEnd() {
		super.visitEnd();
	}
	
	 
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		super.visitFieldInsn(opcode, owner, name, desc);
	}
	
	 
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
		super.visitFrame(type, nLocal, local, nStack, stack);
	}
	
	 
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
	}
	
	 
	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
	}
	
	 
	public void visitIntInsn(int opcode, int operand) {
		super.visitIntInsn(opcode, operand);
	}
	
	 
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}
	
	 
	public void visitJumpInsn(int opcode, Label label) {
		super.visitJumpInsn(opcode, label);
	}
	
	 
	public void visitLabel(Label label) {
		super.visitLabel(label);
	}
	
	 
	public void visitLdcInsn(Object cst) {
		super.visitLdcInsn(cst);
	}
	
	 
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
	}
	
	 
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	 
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	 
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack, maxLocals);
	}
	
	 
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		super.visitMethodInsn(opcode, owner, name, desc);
	}
	
	 
	public void visitMultiANewArrayInsn(String desc, int dims) {
		super.visitMultiANewArrayInsn(desc, dims);
	}
	
	 
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		return super.visitParameterAnnotation(parameter, desc, visible);
	}
	
	 
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	 
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		super.visitTryCatchBlock(start, end, handler, type);
	}
	
	 
	public void visitTypeInsn(int opcode, String type) {
		super.visitTypeInsn(opcode, type);
	}
	
	 
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
	}
}

