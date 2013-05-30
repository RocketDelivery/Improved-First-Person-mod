package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class FieldTransformer extends ClassVisitor {
	String fieldName;
	int fieldAccess;
	
	FieldTransformer(ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
	}
	
	 
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if(name.equals(fieldName)) {
			return cv.visitField(fieldAccess, name, desc, signature, value);
		}
		return cv.visitField(access, name, desc, signature, value);
	}
}
