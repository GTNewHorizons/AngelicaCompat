package com.gtnewhorizons.angelicacompat.asm.transformers.generic;

import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;

import com.gtnewhorizons.angelicacompat.asm.AsmUtil;
import com.gtnewhorizons.angelicacompat.asm.CompatRegistry;
import com.gtnewhorizons.angelicacompat.core.AngelicaCompatCore;

public class GetTileEntityNullGuardTransformer implements IClassTransformer {

    private static final Map<String, List<String>> patchMethods = CompatRegistry.INSTANCE
        .getTileEntityNullGuardTransforms();

    public byte[] transform(final String className, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (!patchMethods.containsKey(transformedName)) {
            return basicClass;
        }

        ClassReader cr = new ClassReader(basicClass);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for (String targetMethod : patchMethods.get(transformedName)) {
            for (MethodNode method : cn.methods) {
                if (!method.name.equals(targetMethod)) continue;
                injectGetTileEntityNullGuard(method);
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        final byte[] bytes = cw.toByteArray();
        AngelicaCompatCore.dumpClass(transformedName, basicClass, bytes, this);
        return bytes;
    }

    public static void injectGetTileEntityNullGuard(MethodNode mn) {
        /*
         * Searching for the following pattern:
         * L0 {
         * ...
         * INVOKEINTERFACE net/minecraft/world/IBlockAccess.getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;
         * CHECKCAST *
         * ASTORE x
         * }
         * L1 {...
         * Then injecting the following after the ASTORE such that we get
         * L0 {
         * ASTORE x
         * ALOAD x
         * IFNONNULL L1
         * ICONST_0
         * IRETURN
         * }
         * L1 {...
         * Ultimately this converts:
         * MyTileEntity tile = (MyTileEntity) world.getTileEntity(x, y, z);
         * doSomethingWithTileEntity(tile);
         * into:
         * MyTileEntity tile = (MyTileEntity) world.getTileEntity(x, y, z);
         * if (tile == null) {
         * return false;
         * } else {
         * doSomethingWithTileEntity(tile);
         * }
         */
        for (int i = 0; i < mn.instructions.size(); i++) {
            AbstractInsnNode in = mn.instructions.get(i);
            if (in instanceof MethodInsnNode min) {
                if (min.getOpcode() == Opcodes.INVOKEINTERFACE
                    && min.name.equals(AsmUtil.obf("getTileEntity", "func_147438_o"))
                    && min.owner.equals("net/minecraft/world/IBlockAccess")) {
                    AbstractInsnNode castNodeAbstract = min.getNext();
                    if (castNodeAbstract instanceof TypeInsnNode castNodeType) {
                        if (castNodeType.getOpcode() == Opcodes.CHECKCAST) {
                            AbstractInsnNode astoreNodeAbstract = castNodeType.getNext();
                            if (astoreNodeAbstract instanceof VarInsnNode astoreNodeVar) {
                                if (astoreNodeVar.getOpcode() == Opcodes.ASTORE) {
                                    InsnList list = new InsnList();
                                    LabelNode exit = new LabelNode();
                                    list.add(new VarInsnNode(Opcodes.ALOAD, astoreNodeVar.var));
                                    list.add(new JumpInsnNode(Opcodes.IFNONNULL, exit));
                                    list.add(new InsnNode(Opcodes.ICONST_0));
                                    list.add(new InsnNode(Opcodes.IRETURN));
                                    list.add(exit);
                                    mn.instructions.insert(astoreNodeVar, list);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
