package ins;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author GN
 * @description
 * @date 2019/12/16
 */
public class MethodAdapter extends MethodVisitor {

    public MethodAdapter(MethodVisitor mv){
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitCode() {

        //标签表示字节码中的位置，表示接下来要执行的指令，
        Label label = new Label();
//        System.out.println("-------");
//        System.out.println(label);

        /**
         * 方法执行之前植入代码，植入SecurityCheck,checkSecurity
         */
        //访问方法指令，方法指令是调用方法的指令
        visitMethodInsn(Opcodes.INVOKESTATIC, "test/operation/SecurityChecker", "checkSecurity", "()Z");

        //访问跳转指令，第一个参数是要访问的跳转指令的操作码
        //第二个参数是一个标签，指示要访问的跳转指令要跳去的指令
        visitJumpInsn(Opcodes.IFNE, label);

        //访问零操作数指令，参数为要访问的指令的操作码
        visitInsn(Opcodes.RETURN);

        //访问一个标签
        visitLabel(label);

        //开始访问父类的方法代码
        super.visitCode();
    }
}
