package com.tntmodders.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class TakumiASMTransformer implements IClassTransformer {

    //フィールド名マップを作る!!!
    //IClassTransformerにより呼ばれる書き換え用のメソッド。
    //EntityCreeper/explode(from EntityCreeper/onUpdate)→TakumiASMEvents/takumiExplode
    //(もしクラスがEntityCreeperならばリフレクションでexplodeを呼び出し、そうでないなら実装したtakumiExplodeを呼ぶ)
    //IClassTransformerにより呼ばれる書き換え用のメソッド。
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        //対象クラス以外を除外する。対象は呼び出し元があるクラスである。
        if ("net.minecraft.entity.monster.EntityCreeper".equals(transformedName)) {
            return this.transformEntityCreeper(name, transformedName, bytes);
        }
        if ("net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer".equals(transformedName)) {
            return this.transformTileEntityRenderer(name, transformedName, bytes);
        }
        if ("net.minecraft.entity.item.EntityPainting".equals(transformedName)) {
            return this.transformEntityPainting(name, transformedName, bytes);
        }
        if ("net.minecraft.entity.item.EntityItemFrame".equals(transformedName)) {
            return this.transformEntityItemFrame(name, transformedName, bytes);
        }
        if ("net.minecraft.item.ItemStack".equals(transformedName)) {
            return this.transformItemStack(name, transformedName, bytes);
        }
        return bytes;
    }

    private byte[] transformItemStack(String name, String transformedName, byte... bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(1);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            //クラス内のメソッドを訪れる。
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("damageItem") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("damageItem")) ||
                        methodName.equals("damageItem") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("damageItem"))) {
                    //もし対象だったらMethodVisitorを差し替える。
                    mv = new MethodVisitor(ASM4, mv) {
                        //呼び出す予定のメソッドを読み込む。
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String methodName, String desc,
                                                    boolean itf) {
                            //書き換え対象のメソッドであることを確認する。
                            String s2 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                            if (s2.equals("attemptDamageItem") ||
                                    s2.equals(TakumiASMNameMap.METHOD_MAP.get("attemptDamageItem")) ||
                                    methodName.equals("attemptDamageItem") ||
                                    methodName.equals(TakumiASMNameMap.METHOD_MAP.get("attemptDamageItem"))) {
                                //引数として次に渡す値にthisを指定する。
                                mv.visitVarInsn(ALOAD, 0);
                                //メソッドを読み込む。INVOKESTATICでstaticメソッドを呼び出す。
                                super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiItemStackDamageHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.getObjectType("net/minecraft/item/ItemStack")
                                        ), false);
                              /*  super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiItemStackDamageHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.INT_TYPE, Type.getObjectType("java/util/Random"), Type.getObjectType("net/minecraft/entity/player/EntityPlayerMP"),
                                                Type.getObjectType("net/minecraft/item/ItemStack")
                                        ), false);*/
                                //今回はフックを差し込むだけだが、ここで書き換えも出来る。
                            }
                            //今回は最後に元のクラスを読み込んでreturnする。
                            super.visitMethodInsn(opcode, owner, methodName, desc, itf);
                        }
                    };
                }
                return mv;
            }
        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private byte[] transformEntityCreeper(String name, String transformedName, byte... bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(1);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            //クラス内のメソッドを訪れる。
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("onUpdate") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("onUpdate")) ||
                        methodName.equals("onUpdate") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("onUpdate"))) {
                    //もし対象だったらMethodVisitorを差し替える。
                    mv = new MethodVisitor(ASM4, mv) {
                        //呼び出す予定のメソッドを読み込む。
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String methodName, String desc,
                                                    boolean itf) {
                            //書き換え対象のメソッドであることを確認する。
                            String s2 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                            if (s2.equals("explode") || s2.equals(TakumiASMNameMap.METHOD_MAP.get("explode")) ||
                                    methodName.equals("explode") ||
                                    methodName.equals(TakumiASMNameMap.METHOD_MAP.get("explode"))) {
                                //引数として次に渡す値にthisを指定する。
                                mv.visitVarInsn(ALOAD, 0);
                                //メソッドを読み込む。INVOKESTATICでstaticメソッドを呼び出す。
                                super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiExplodeHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.getObjectType("net/minecraft/entity/monster/EntityCreeper")),
                                        false);
                                //今回はフックを差し込むだけだが、ここで書き換えも出来る。
                            }
                            //今回は最後に元のクラスを読み込んでreturnする。
                            super.visitMethodInsn(opcode, owner, methodName, desc, itf);
                        }
                    };
                }
                return mv;
            }
        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private byte[] transformTileEntityRenderer(String name, String transformedName, byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(1);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            //クラス内のメソッドを訪れる。
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("renderByItem") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("renderByItem")) ||
                        methodName.equals("renderByItem") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("renderByItem"))) {
                    //もし対象だったらMethodVisitorを差し替える。
                    mv = new MethodVisitor(ASM4, mv) {
                        //呼び出す予定のメソッドを読み込む。
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String methodName, String desc,
                                                    boolean itf) {
                            //書き換え対象のメソッドであることを確認する。
                            String s2 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                            if (s2.equals("renderByItem") ||
                                    s2.equals(TakumiASMNameMap.METHOD_MAP.get("renderByItem2")) ||
                                    methodName.equals("renderByItem") ||
                                    methodName.equals(TakumiASMNameMap.METHOD_MAP.get("renderByItem2"))) {
                                //引数として次に渡す値にthisを指定する。
                                mv.visitVarInsn(ALOAD, 1);
                                //メソッドを読み込む。INVOKESTATICでstaticメソッドを呼び出す。
                                super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiRenderByItemHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.getObjectType("net/minecraft/item/ItemStack")), false);
                                //今回はフックを差し込むだけだが、ここで書き換えも出来る。
                            }
                            //今回は最後に元のクラスを読み込んでreturnする。
                            super.visitMethodInsn(opcode, owner, methodName, desc, itf);
                        }
                    };
                }
                return mv;
            }
        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private byte[] transformEntityPainting(String name, String transformedName, byte... bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(1);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            //クラス内のメソッドを訪れる。
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("onBroken") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("onBroken")) ||
                        methodName.equals("onBroken") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("onBroken"))) {
                    //もし対象だったらMethodVisitorを差し替える。
                    mv = new MethodVisitor(ASM4, mv) {
                        //呼び出す予定のメソッドを読み込む。
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String methodName, String desc,
                                                    boolean itf) {
                            //書き換え対象のメソッドであることを確認する。
                            String s2 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                            if (s2.equals("entityDropItem") ||
                                    s2.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem")) ||
                                    methodName.equals("entityDropItem") ||
                                    methodName.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem"))) {
                                //引数として次に渡す値にthisを指定する。
                                mv.visitVarInsn(ALOAD, 0);
                                //メソッドを読み込む。INVOKESTATICでstaticメソッドを呼び出す。
                                super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiPaintingHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.getObjectType("net/minecraft/item/ItemStack"), Type.FLOAT_TYPE, Type.getObjectType("net/minecraft/entity/EntityHanging")), false);
                                //今回はフックを差し込むだけだが、ここで書き換えも出来る。
                            } else {
                                //今回は最後に元のクラスを読み込んでreturnする。
                                super.visitMethodInsn(opcode, owner, methodName, desc, itf);
                            }
                        }
                    };
                }
                return mv;
            }
        };

 /*               MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("entityDropItem") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem")) ||
                        methodName.equals("entityDropItem") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem"))) {
                    mv = new MethodVisitor(ASM4, mv) {
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                    "TakumiPaintingHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                            Type.getObjectType("net/minecraft/entity/EntityHanging")), false);
                        }
                    };
                    return mv;
                }
                return mv;*/
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    private byte[] transformEntityItemFrame(String name, String transformedName, byte... bytes) {
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(1);
        ClassVisitor cv = new ClassVisitor(ASM4, cw) {
            //クラス内のメソッドを訪れる。
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature,
                                             String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("dropItemOrSelf") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("dropItemOrSelf")) ||
                        methodName.equals("dropItemOrSelf") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("dropItemOrSelf"))) {
                    //もし対象だったらMethodVisitorを差し替える。
                    mv = new MethodVisitor(ASM4, mv) {
                        //呼び出す予定のメソッドを読み込む。
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String methodName, String desc,
                                                    boolean itf) {
                            //書き換え対象のメソッドであることを確認する。
                            String s2 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                            if (s2.equals("entityDropItem") ||
                                    s2.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem")) ||
                                    methodName.equals("entityDropItem") ||
                                    methodName.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem"))) {
                                //引数として次に渡す値にthisを指定する。
                                mv.visitVarInsn(ALOAD, 0);
                                //メソッドを読み込む。INVOKESTATICでstaticメソッドを呼び出す。
                                super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                        "TakumiFrameHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                                Type.getObjectType("net/minecraft/item/ItemStack"), Type.FLOAT_TYPE, Type.getObjectType("net/minecraft/entity/EntityHanging")), false);
                                //今回はフックを差し込むだけだが、ここで書き換えも出来る。
                            } else {
                                //今回は最後に元のクラスを読み込んでreturnする。
                                super.visitMethodInsn(opcode, owner, methodName, desc, itf);
                            }
                        }
                    };
                }
                return mv;
            }
        };

 /*               MethodVisitor mv = super.visitMethod(access, methodName, desc, signature, exceptions);
                //呼び出し元のメソッドを参照していることを確認する。
                String s1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, methodName, desc);
                //C:\Users\<ユーザー名>\.gradle\caches\minecraft\net\minecraftforge\forge\1.7.10-10.13.4.1558-1.7.10\forge-1.7.10-10.13.4.1558-1.7.10
                // -decomp.jar\より名称を検索、比較してメソッドの難読化名を探す。
                if (s1.equals("entityDropItem") || s1.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem")) ||
                        methodName.equals("entityDropItem") ||
                        methodName.equals(TakumiASMNameMap.METHOD_MAP.get("entityDropItem"))) {
                    mv = new MethodVisitor(ASM4, mv) {
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            super.visitMethodInsn(INVOKESTATIC, "com/tntmodders/asm/TakumiASMHooks",
                                    "TakumiPaintingHook", Type.getMethodDescriptor(Type.VOID_TYPE,
                                            Type.getObjectType("net/minecraft/entity/EntityHanging")), false);
                        }
                    };
                    return mv;
                }
                return mv;*/
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}


