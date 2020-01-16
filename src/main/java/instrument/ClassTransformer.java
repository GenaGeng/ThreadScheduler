package instrument;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author GN
 * @description
 * @date 2019/12/12
 */
public class ClassTransformer extends ClassVisitor {

    public ClassTransformer(ClassVisitor classVisitor){
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        System.out.println("MethodVisitor");
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//        System.out.println(".........1..........");
//        System.out.println(mv);
//        System.out.println(".........1..........");
        mv = new MethodTransformer(mv, access, name, desc);
//        System.out.println("........2......");
//        System.out.println(mv);
//        System.out.println("........2.......");
        return mv;
    }
}
