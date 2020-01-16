package ins;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import javax.swing.*;

/**
 * @author GN
 * @description
 * @date 2019/12/12
 */
public class MethodTransformer extends AdviceAdapter implements Opcodes {

    private static final String no_arg_void = "()V";
    private static final String bool_3Strings_int_void = "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V";
    private String methodName;
    private boolean isInit;
    private int lineNumber;
    private int max_index_cur;

    /**
     * 构造函数
     * @param mv   适配器将方法调用委派给的方法访问者
     * @param access    方法的访问标志
     * @param name    方法的名称
     * @param desc    方法的描述符
     */
    protected MethodTransformer(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM5, mv, access, name, desc);
        methodName = name;
        System.out.println("method" + methodName);
        isInit = (methodName.equals("<init>") || methodName.equals("clinit"));
//        System.out.println(isInit);
        this.max_index_cur = Type.getArgumentsAndReturnSizes(desc)+1;
        System.out.println(desc + max_index_cur);
    }




    /**
     * 访问字段指令，字段指令是指加载或存储对象字段值的指令
     * @param opcode  要访问的字段指令的操作码，getstatic,putstatic,getfield,putfield
     * @param owner  字段所有者类的内部名称
     * @param name   字段名
     * @param desc  字段描述符
     */
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
//        System.out.println(opcode);
        System.out.println(desc);
        //访问被插桩类的构造函数
//        System.out.println("isisisisis" + isInit);
        if (isInit){
            System.out.println("访问构造函数"+owner+"的" + name+ "字段");
//            System.out.println(opcode);
            super.visitFieldInsn(opcode, owner, name, desc);
            return;
        }
        boolean isRead = false;
//        System.out.println(opcode);
        if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC){
            System.out.println("访问读操作字段" + owner + "的" + name +"字段");
            isRead = true;
            instrumentField(true, isRead, owner, name, desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }else{
            System.out.println("非读操作字段" + owner + "的" + name + "字段");
//            max_index_cur ++;
//            int index = max_index_cur;
//            storeValue(desc,index);  //栈顶值存入局部变量表
//            loadValue(desc,index);    //从局部变量表读取一个局部变量，并将其压入操作数栈中
            instrumentField(false, isRead, owner, name, desc);
            super.visitFieldInsn(opcode, owner, name, desc);
        }

    }

    @Override
    public void visitLineNumber(int line, Label start) {
        System.out.println("得到行号");
        lineNumber = line;
        mv.visitLineNumber(line, start);
    }

    public void instrumentField(boolean isBefore, boolean isRead, String fieldOwner, String fieldName, String fieldDesc){
        //参数为要访问的指令的操作码，如果是读操作就把int类型的1放到操作数栈顶，否则把0放到操作数栈顶
        mv.visitInsn(isRead ? Opcodes.ICONST_1 : Opcodes.ICONST_0);
        //访问LDC指令。参数为要加载到栈上的常量
        System.out.println("--------------------------------------");
        mv.visitLdcInsn(methodName);
        System.out.println(methodName);
        mv.visitLdcInsn(fieldName);
        System.out.println(fieldName);
        System.out.println(fieldDesc);
        mv.visitLdcInsn(fieldDesc);
//        System.out.println(lineNumber);
//        System.out.println(new Integer(lineNumber));
//        mv.visitLdcInsn(new Integer(lineNumber));
        System.out.println("-----------------------------------------");

        //在读操作字段之前插入（之后）
        if (isBefore){
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "test/operation/Scheduler", "beforeFieldAccess", bool_3Strings_int_void);
        }else {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "test/Operation/Scheduler", "afterFieldAccess", bool_3Strings_int_void);
        }
    }

    public void loadValue(String desc, int index){
        System.out.println("从局部变量表取值压入操作数栈");
        System.out.println(desc + "----" + index);
        if (desc.startsWith("L") || desc.startsWith("[")){
            mv.visitVarInsn(ALOAD, index);
        }else if (desc.startsWith("I") || desc.startsWith("B") || desc.startsWith("S")
                || desc.startsWith("Z") || desc.startsWith("C")){
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
        System.out.println("栈顶值存到局部变量表");
        System.out.println(dese+"---"+index);
        if (dese.startsWith("L") || dese.startsWith("[")){
            System.out.println("对象或数组");
            mv.visitVarInsn(ASTORE, index);//从操作数栈中弹出对象或数组引用，存储到index指定的局部变量中
        }else if (dese.startsWith("I") || dese.startsWith("B")
                || dese.startsWith("S") || dese.startsWith("Z")
                || dese.startsWith("C")){
            System.out.println("整形、字节、短型、布尔，字符");
            mv.visitVarInsn(ISTORE, index);//int,byte,short,boolean,char
        }else if (dese.startsWith("J")){
            System.out.println("long");
            mv.visitVarInsn(LSTORE,index);//long
        }else if (dese.startsWith("F")){
            System.out.println("浮点型");
            mv.visitVarInsn(FSTORE,index);//float
        }else if (dese.startsWith("D")){
            System.out.println("double");
            mv.visitVarInsn(DSTORE, index);//double
            max_index_cur++;
        }
    }
}
