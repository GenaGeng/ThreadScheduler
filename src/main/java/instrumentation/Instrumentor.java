package instrumentation;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Gena
 * @description  对class类进行拦截，按自己的逻辑进行插桩
 * @date 2019/12/5 0005
 */
public class Instrumentor {
    public static String EVENT="";

    /**
     * premain()方法是在main方法之前执行的方法
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation){
        instrumentation.addTransformer(new DefineTransform());
    }
    static class DefineTransform implements ClassFileTransformer {

        public byte[] transform(ClassLoader loader, String className,
                                Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            System.out.println("Instrumenting:" + className);
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter();
        }
    }
}
