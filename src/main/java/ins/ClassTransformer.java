package ins;


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

    /**
     * 调用该类定义类的标头
     * @param version 类的版本
     * @param access   java修饰符
     * @param name   内部形式的类名：包名/类名
     * @param signature   泛型
     * @param superName   内部形式的超类（如接口类继承自object）
     * @param interfaces  内部名指定的接口
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     *  定义字段
     * @param access  java修饰符
     * @param name  字段名
     * @param desc  类型描述符表示字段类型
     * @param signature  泛型
     * @param value 字段的值，若为常量字段final static那就是对应的常量值，否则为空
     * @return
     */
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return cv.visitField(access, name, desc, signature, value);
    }

    /**
     * 定义方法
     * @param access  java修饰符标志
     * @param name 方法名
     * @param desc 方法的描述符
     * @param signature 泛型
     * @param exceptions 方法抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        mv = new MethodTransformer(mv, access, name, desc);
        return mv;
    }
}
