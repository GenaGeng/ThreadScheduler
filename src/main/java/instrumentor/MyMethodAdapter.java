package instrumentor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author Gena
 * @description
 * @date 2020/3/2 0002
 */
public class MyMethodAdapter extends AdviceAdapter {

    private String methodname;

    private static final String BOOL_3STRINGS_INT_VOID = "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V";

    public MyMethodAdapter(MethodVisitor mv, int access, String name, String desc){
        super(Opcodes.ASM5, mv, access, name, desc);
        methodname = name;
    }

    @Override
    public void visitCode(){
        super.visitCode();
    }

//    @Override
//    public void visitMethodInsn(int opcode, String owner, String name, String desc){
//
//        boolean isSetAcc = false;
//        if (opcode == Opcodes.INVOKEVIRTUAL ){
//
//            isSetAcc = true;
//
//            instrumentField(true, isSetAcc, owner, name, desc);
//        }
//    }
//
//    public void instrumentField(boolean isBefore, boolean isSetAcc, String fieldOwner, String fieldName, String fieldDesc){
//
//        mv.visitInsn(isBefore ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
//
//        mv.visitLdcInsn(methodname);
//
//        mv.visitLdcInsn(fieldName);
//
//        mv.visitLdcInsn(fieldDesc);
//
//        if (isBefore){
//
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "scheduler/Scheduler", "beforeMethodAccess", BOOL_3STRINGS_INT_VOID);
//        }else {
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "scheduler/Scheduler", "afterMethodAccess", BOOL_3STRINGS_INT_VOID);
//        }
//
//    }


}
