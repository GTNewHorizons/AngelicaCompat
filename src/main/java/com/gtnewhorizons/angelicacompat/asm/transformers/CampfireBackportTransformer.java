package com.gtnewhorizons.angelicacompat.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.gtnewhorizons.angelicacompat.asm.AsmUtil;

public class CampfireBackportTransformer implements IClassTransformer {

    private static final String RenderBlockCampfire = "connor135246.campfirebackport.client.rendering.RenderBlockCampfire";

    @Override
    public byte[] transform(final String className, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (!transformedName.equals(RenderBlockCampfire)) {
            return basicClass;
        }

        final ClassReader cr = new ClassReader(basicClass);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        AsmUtil.addThreadSafeISBRHAnnotation(transformedName, cn, false);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
