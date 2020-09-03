package ins;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Gena
 * @description
 * @date 2020/2/27 0027
 */
public class MyClassAdapter extends ClassVisitor {

    public MyClassAdapter(ClassVisitor classVisitor){

        super(Opcodes.ASM5, classVisitor);
    }


}
