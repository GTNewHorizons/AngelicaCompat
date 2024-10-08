package com.gtnewhorizons.angelicacompat.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.gtnewhorizons.angelicacompat.Common;

public class AsmUtil {

    private static final AnnotationNode perThreadTrue = new AnnotationNode(
        "Lcom/gtnewhorizons/angelica/api/ThreadSafeISBRH;");;
    private static final AnnotationNode perThreadFalse = new AnnotationNode(
        "Lcom/gtnewhorizons/angelica/api/ThreadSafeISBRH;");;

    static {
        perThreadTrue.values = Arrays.asList("perThread", true);
        perThreadFalse.values = Arrays.asList("perThread", false);
    }

    public static void addThreadSafeISBRHAnnotation(String transformedName, ClassNode cn) {
        addThreadSafeISBRHAnnotation(transformedName, cn, false);
    }

    public static void addThreadSafeISBRHAnnotation(String transformedName, ClassNode cn, boolean perThread) {
        if (cn.visibleAnnotations == null) {
            cn.visibleAnnotations = new ArrayList<>();
        }
        cn.visibleAnnotations.add(perThread ? perThreadTrue : perThreadFalse);

        Common.log.info("Added ThreadSafeISBRH with perThread = " + perThread + " to " + transformedName);
    }
}
