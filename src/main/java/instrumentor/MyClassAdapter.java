package instrumentor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Gena
 * @description
 * @date 2020/3/2 0002
 */
public class MyClassAdapter extends ClassVisitor {

    public MyClassAdapter(ClassVisitor cv){

        super(Opcodes.ASM5,cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signatire, Object value){
        return cv.visitField(access, name, desc, signatire, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exception){
        System.out.println("visitmethod" + name);
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exception);

        mv = new MyMethodAdapter(mv, access, name, desc);
        return mv;
    }

}
