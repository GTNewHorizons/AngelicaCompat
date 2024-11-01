package com.gtnewhorizons.angelicacompat.asm.transformers.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.tree.*;

import com.gtnewhorizons.angelicacompat.asm.CompatRegistry;
import com.gtnewhorizons.angelicacompat.core.AngelicaCompatCore;

public class ThreadSafeISBRHAnnotationTransformer implements IClassTransformer {

    private static final Map<String, Boolean> patchClasses = CompatRegistry.INSTANCE.getThreadSafeISBRHAnnotations();

    private static final AnnotationNode perThreadTrue = new AnnotationNode(
        "Lcom/gtnewhorizons/angelica/api/ThreadSafeISBRH;");
    private static final AnnotationNode perThreadFalse = new AnnotationNode(
        "Lcom/gtnewhorizons/angelica/api/ThreadSafeISBRH;");

    static {
        perThreadTrue.values = Arrays.asList("perThread", true);
        perThreadFalse.values = Arrays.asList("perThread", false);
    }

    public byte[] transform(final String className, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (!patchClasses.containsKey(transformedName)) {
            return basicClass;
        }

        ClassReader cr = new ClassReader(basicClass);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        if (cn.visibleAnnotations == null) {
            cn.visibleAnnotations = new ArrayList<>();
        }

        if (patchClasses.getOrDefault(transformedName, true)) {
            cn.visibleAnnotations.add(perThreadTrue);
        } else {
            cn.visibleAnnotations.add(perThreadFalse);
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        final byte[] bytes = cw.toByteArray();
        AngelicaCompatCore.dumpClass(transformedName, basicClass, bytes, this);
        return bytes;
    }
}
