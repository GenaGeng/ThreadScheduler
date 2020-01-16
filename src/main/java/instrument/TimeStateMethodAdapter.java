package instrument;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author GN
 * @description
 * @date 2019/12/23
 */
public class TimeStateMethodAdapter extends MethodVisitor {

    public TimeStateMethodAdapter(MethodVisitor mv){
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        //访问方法指令，方法指令是调用方法的指令
        Label label = new Label();
        visitLabel(label);
        visitMethodInsn(Opcodes.INVOKESTATIC, "test/operation/TimeState", "countTime", "()V");
        visitEnd();
    }
}
