package kes5219.utils.classtransformer.helper;

import org.objectweb.asm.MethodVisitor;

public interface ICustomProfilerSectionHook {
	public abstract void onDesignatedSection(MethodVisitor mv);
}
