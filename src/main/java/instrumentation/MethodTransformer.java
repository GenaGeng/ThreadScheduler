package instrumentation;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author GN
 * @description
 * @date 2019/12/12
 */
public class MethodTransformer extends AdviceAdapter implements Opcodes {

    private static final String no_arg_void = "()V";
    private static final String bool_3Strings_int_void = "(ZLjava/langlString;Ljava/lang/String;Ljava/lang/String;I)V";
    private String methodName;
    private boolean isInit;
    private int lineNumber;
    private int max_index_cur;

    /**
     * 这也不是单列模式啊。。。。
     * @param mv
     * @param access
     * @param name
     * @param desc
     */
    protected MethodTransformer(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM5, mv, access, name, desc);
        methodName = name;
        isInit = (methodName.equals("<init>") || methodName.equals("clinit"));
        this.max_index_cur = Type.getArgumentsAndReturnSizes(desc)+1;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        lineNumber = line;
        mv.visitLineNumber(line, start);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if (isInit){
            super.visitFieldInsn(opcode, owner, name, desc);
            return;
        }
        boolean isRead = false;
        if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC){
            isRead = true;
            instrumentField(true, isRead, owner, name, desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }else{
            max_index_cur ++;
            int index = max_index_cur;
            storeValue(desc,index);  //栈顶值存入局部变量表
            loadValue(desc,index);    //从局部变量表读取一个局部变量，并将其压入操作数栈中
            instrumentField(true, isRead, owner, name, desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    public void instrumentField(boolean isBefore, boolean isRead, String fieldOwner, String fieldName, String fieldDesc){
        mv.visitInsn(isRead ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(fieldName);
        mv.visitLdcInsn(fieldDesc);
        mv.visitLdcInsn(new Integer(lineNumber));

        if (isBefore){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Instrumentor.EVENT, "beforeFieldAccess", bool_3Strings_int_void);
        }else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Instrumentor.EVENT, "afterFieldAccess", bool_3Strings_int_void);
        }
    }

    public void loadValue(String desc, int index){
        if (desc.startsWith("L") || desc.startsWith("[")){
            mv.visitVarInsn(ALOAD, index);
        }else if (desc.startsWith("I")){
            mv.visitVarInsn(ILOAD, index);
        }else if (desc.startsWith("B")){
            mv.visitVarInsn(ILOAD, index);
        }else if (desc.startsWith("S")){
            mv.visitVarInsn(ILOAD, index);
        }else if (desc.startsWith("Z")){
            mv.visitVarInsn(ILOAD, index);
        }else if (desc.startsWith("C")){
            mv.visitVarInsn(ILOAD, index);
        }else if (desc.startsWith("J")){
            mv.visitVarInsn(LLOAD, index);
        }else if (desc.startsWith("F")){
            mv.visitVarInsn(FLOAD, index);
        }else if (desc.startsWith("D")){
            mv.visitVarInsn(DLOAD, index);
        }
    }

    public void storeValue(String dese,int index){
        if (dese.startsWith("L") || dese.startsWith("[")){
            mv.visitVarInsn(ASTORE, index);
        }else if (dese.startsWith("I") || dese.startsWith("B")
                || dese.startsWith("S") || dese.startsWith("Z")
                || dese.startsWith("C")){
            mv.visitVarInsn(ISTORE, index);
        }else if (dese.startsWith("J")){
            mv.visitVarInsn(LSTORE,index);
        }else if (dese.startsWith("F")){
            mv.visitVarInsn(FSTORE,index);
        }else if (dese.startsWith("D")){
            mv.visitVarInsn(DSTORE, index);
            max_index_cur++;
        }
    }
}
