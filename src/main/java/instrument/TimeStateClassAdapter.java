package instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author GN
 * @description
 * @date 2019/12/23
 */
public class TimeStateClassAdapter extends ClassVisitor {

    public TimeStateClassAdapter(ClassVisitor cv){
        super(Opcodes.ASM5,cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        MethodVisitor methodVisitor = mv;

        if (name.equals("account")){

            methodVisitor = new TimeStateMethodAdapter(mv);
        }

        return methodVisitor;
    }
}
