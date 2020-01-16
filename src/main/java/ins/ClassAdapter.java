package ins;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author GN
 * @description
 * @date 2019/12/16
 */
public class ClassAdapter extends ClassVisitor {

    public ClassAdapter(ClassVisitor cv){

        super(Opcodes.ASM5, cv);

    }

    /**重写visitMethod,访问到operation方法时，给出自定义MethodVisitor,实际改写方法内容*/
    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {

        MethodVisitor mv = super.visitMethod(i, s, s1, s2, strings);

        MethodVisitor warappendMv = mv;

        if (mv != null){
            //对于operation方法
            if (s.equals("operation")){
                //使用自定义的MethodVisitor方法，实际改写方法内容
                warappendMv = new MethodAdapter(mv);
            }
        }

        return warappendMv;
    }
}
