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

        //创建标签对象label
        Label label = new Label();

        /**
         * 方法执行之前植入代码，植入SecurityCheck,checkSecurity
         */
        //INVOESTATIC "test/operation/SecurityChecker" checkSecurity ()Z
        visitMethodInsn(Opcodes.INVOKESTATIC, "test/operation/SecurityChecker", "checkSecurity", "()Z");

        // IFNE label   IFNE是如果上一条语句不等于0就跳转的意思
        visitJumpInsn(Opcodes.IFNE, label);

        // RETURN
        visitInsn(Opcodes.RETURN);

        //label
        visitLabel(label);

        //原来的代码，如果不等于0，就跳转到原来的代码继续执行
        super.visitCode();
    }
}
