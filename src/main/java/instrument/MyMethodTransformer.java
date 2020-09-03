package instrument;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

public class MyMethodTransformer extends AdviceAdapter implements Opcodes {
    private static final String NO_ARG_VOID = "()V";
    private static final String BOOL_3STRINGS_INT_VOID = "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V";
    private String methodnName;
    private boolean isInit;
    private int lineNumber;
    private int max_index_cur;

    protected MyMethodTransformer(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM5, mv, access, name, desc);
        methodnName = name;
        isInit = (methodnName.equals("<init>")||methodnName.equals("<clinit>"));
        this.max_index_cur = Type.getArgumentsAndReturnSizes(desc)+1;
    }


    @Override
    public void visitLineNumber(int line, Label start) {
        lineNumber=line;
        mv.visitLineNumber(line, start);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {

        if (isInit){
            super.visitFieldInsn(opcode, owner, name, desc);
            return;
        }
        boolean isRead = false;
        if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC) {
            isRead = true;
            instrumentField(true,isRead,owner,name,desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        } else {
            max_index_cur++;
            int index = max_index_cur;
            storeValue(desc,index);//栈顶值存入局部变量表
            loadValue(desc,index);//-
            instrumentField(true,isRead,owner,name,desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }
//        instrumentField(false,isRead,owner,name,desc);
    }

    private void loadValue(String desc, int index) {
        if (desc.startsWith("L") || desc.startsWith("[")) {
            mv.visitVarInsn(ALOAD, index);
        } else if (desc.startsWith("I")) {
            mv.visitVarInsn(ILOAD, index);
        } else if (desc.startsWith("B")) {
            mv.visitVarInsn(ILOAD, index);
        } else if (desc.startsWith("S")) {
            mv.visitVarInsn(ILOAD, index);
        } else if (desc.startsWith("Z")) {
            mv.visitVarInsn(ILOAD, index);
        } else if (desc.startsWith("C")) {
            mv.visitVarInsn(ILOAD, index);
        } else if (desc.startsWith("J")) {
            mv.visitVarInsn(LLOAD, index);
        } else if (desc.startsWith("F")) {
            mv.visitVarInsn(FLOAD, index);
        } else if (desc.startsWith("D")) {
            mv.visitVarInsn(DLOAD, index);
        }
    }

    private void storeValue(String desc, int index) {
        if (desc.startsWith("L") || desc.startsWith("[")) {
            mv.visitVarInsn(ASTORE, index);
        } else if (desc.startsWith("I") || desc.startsWith("B")
                || desc.startsWith("S") || desc.startsWith("Z")
                || desc.startsWith("C")) {
                     //duplicate the value on top of stack
            mv.visitVarInsn(ISTORE, index);  //store value to the #index variable
        } else if (desc.startsWith("J")) {
            mv.visitVarInsn(LSTORE, index);
            max_index_cur++;
        } else if (desc.startsWith("F")) {
            mv.visitVarInsn(FSTORE, index);
        } else if (desc.startsWith("D")) {
            mv.visitVarInsn(DSTORE, index);
            max_index_cur++;
        }

        // if(classname.equals("org/eclipse/core/runtime/internal/adaptor/PluginConverterImpl"))
        // System.out.println("Signature: "+desc);
    }

    private void instrumentField(boolean isBefore, boolean isRead, String fieldOwner, String fieldName, String fieldDesc){
        mv.visitInsn(isRead ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        mv.visitLdcInsn(methodnName);
        mv.visitLdcInsn(fieldName);
        mv.visitLdcInsn(fieldDesc);
        mv.visitLdcInsn(new Integer(lineNumber));
        if (isBefore) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    Instrumentor.EVENT_RECIEVER,
                    "beforeFieldAccess",
                    BOOL_3STRINGS_INT_VOID);
        } else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Instrumentor.EVENT_RECIEVER, "afterFieldAccess", BOOL_3STRINGS_INT_VOID,false);
        }
    }

}
