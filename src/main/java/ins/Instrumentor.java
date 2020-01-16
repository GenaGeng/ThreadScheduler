package ins;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import test.account.Account;
import test.operation.Ope;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author GN
 * @description
 * @date 2019/12/16
 */
public class Instrumentor {

    public static void main(String[] args) throws Exception{

        String className = Ope.class.getName();
        System.out.println("instrumenting " + className);

        ClassReader cr = new ClassReader(className);
//        System.out.println(cr);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        ClassAdapter ct = new ClassAdapter(cw);

        //ClassReader.SKIP_DEBUG表示不遍历调试内容，跳过调试信息（ClassVisitSource,MethodVisitor.visitLocalVariable,MethodVisitor.visitLineNumber不会被解析和访问）
        cr.accept(ct, ClassReader.SKIP_DEBUG);

        byte[] data = cw.toByteArray();
//        System.out.println(data);

        File file = new File("E:\\project\\ThreadScheduler\\target\\classes\\" + className.replaceAll("\\.", "/") + ".class");
//        System.out.println(file);

        if (file.exists()){
            System.out.println("exists!");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        System.out.println(fileOutputStream);
        fileOutputStream.write(data);
//        System.out.println(fileOutputStream);
        fileOutputStream.close();
    }
}
