package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public abstract class CustomFieldTransformer extends ClassVisitor {
	public CustomFieldTransformer() {
		super(Opcodes.ASM4, null);
	}
	
	final void init(ClassVisitor cv) {
		this.cv = cv;
	}
	
	 
	public abstract FieldVisitor visitField(int access, String name, String desc, String signature, Object value);
}