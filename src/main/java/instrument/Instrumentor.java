package instrument;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * @author GN
 * @description
 * @date 2019/12/23
 */
public class Instrumentor {

    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String SEMICOLON = ";";

    //
    public static String bufferClass;

    public static final String logClass = "edu/tamu/aser/runtime/RVRunTime";
    private static final String JUC_DOTS = "java.util.concurrent";

    private static final String INSTRUMENTATION_PACKAGES_DEFAULT = "default";

    private static Set<String> packagesThatWereInstrumented;
    private static Set<String> packagesThatWereNOTInstrumented;

    //classes not instrumented
    private static final Set<String> pckgPrefixesToIgnore = new HashSet<String>();
    private static final Set<String> pckgsToIgnore = new HashSet<String>();
    private static final Set<String> classPrefixesToIgnore = new HashSet<String>();
    private static final Set<String> classesToIgnore = new HashSet<String>();
    private static final Set<String> pckgPrefixesToAllow = new HashSet<String>();
    private static final Set<String> pckgsToAllow = new HashSet<String>();
    private static final Set<String> classPrefixesToAllow = new HashSet<String>();
    private static final Set<String> classesToAllow = new HashSet<String>();

    public static void premain(String agentArgs, Instrumentation inst) throws Exception{

        System.out.println("Agent premain called");
        System.out.println("agentArgs:" + agentArgs);

        inst.addTransformer(new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

//                System.out.println(className);
                if (className.contains("test")) {

                    System.out.println("load transform...");

                    System.out.println("-----meet " + className + "-------");

                    /** classfileBuffer */
//                    System.out.println("classbuffer" + classfileBuffer);
                    ClassReader cr = new ClassReader(classfileBuffer);

                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

                    ClassTransformer ct = new ClassTransformer(cw);

//                    System.out.println("ct=========" + ct);

                    cr.accept(ct, ClassReader.SKIP_DEBUG);

                    classfileBuffer = cw.toByteArray();

//                    System.out.println("classbuffer222222" + classfileBuffer);

//                    byte[] code = cw.toByteArray();

//                    File dir = new File("out");
////                    System.out.println(dir);
//                    if (!dir.exists()) {
//                        dir.mkdir();
//                    }

                    File classFile = new File("E:\\project\\ThreadScheduler\\target\\classes\\" + className.replaceAll("\\.", "/") + ".class");
                    System.out.println(classFile);

                    if (classFile.exists()){
                        System.out.println("exit!");
                    }

//                    try {
//                        classFile.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(classFile);
                        fos.write(classfileBuffer);
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return classfileBuffer;
            }
        });

        /* Re-transform already loaded java.util.concurrent classes */
//        try {
//            List<Class<?>> classesToReTransform = new ArrayList<Class<?>>();
//            for (Class<?> loadedClass : inst.getAllLoadedClasses()) {
////                System.err.println("all loaded classes: " + loadedClass.toString());
//                if (inst.isModifiableClass(loadedClass) && loadedClass.getPackage().getName().startsWith("java.util.concurrent")) {
//                    classesToReTransform.add(loadedClass);
//                }
//            }
//            inst.retransformClasses(classesToReTransform.toArray(new Class<?>[classesToReTransform.size()]));
//        } catch (UnmodifiableClassException e) {
//            e.printStackTrace();
//            System.err.println("Unable to modify a pre-loaded java.util.concurrent class!");
//            System.exit(2);
//        }
    }

    private static boolean shouldInstrumentClass(String name) {
        /*
         * when using Java 8 for controller and controller-test
         * name could be null
         */
        if (name==null) {
            return false;
        }

        String pckgName = INSTRUMENTATION_PACKAGES_DEFAULT;
        int lastSlashIndex = name.lastIndexOf(SLASH);
        // Non-default package
        if (lastSlashIndex != -1) {
            pckgName = name.substring(0, lastSlashIndex);
        }

        // Phase 1 - check if explicitly allowed
//        System.out.println("是否包含"+classesToAllow.contains(name));
        if (classesToAllow.contains(name)) {
            packagesThatWereInstrumented.add(pckgName);
            return true;
        }

        // Phase 2 - check if prefix is allowed
        for (String classPrefix : classPrefixesToAllow) {
            if (name.startsWith(classPrefix)) {
                packagesThatWereInstrumented.add(pckgName);
                return true;
            }
        }

        // Phase 3 - check if package is allowed
        if (pckgsToAllow.contains(pckgName)) {
            packagesThatWereInstrumented.add(pckgName);
            return true;
        }

        // Phase 4 - check if package is allowed via prefix matching
        for (String pckgPrefix : pckgPrefixesToAllow) {
            if (pckgName.startsWith(pckgPrefix)) {
                packagesThatWereInstrumented.add(pckgName);
                return true;
            }
        }

        // Phase 5 - check for any ignores
        if (classesToIgnore.contains(name)) {
            packagesThatWereNOTInstrumented.add(pckgName);
            return false;
        }

        if (pckgsToIgnore.contains(pckgName)) {
            packagesThatWereNOTInstrumented.add(pckgName);
            return false;
        }

        for (String classPrefix : classPrefixesToIgnore) {
            if (name.startsWith(classPrefix)) {
                packagesThatWereNOTInstrumented.add(pckgName);
                return false;
            }
        }

        for (String pckgPrefix : pckgPrefixesToIgnore) {
            //System.out.println(pckgPrefix);
            if (pckgName.startsWith(pckgPrefix)) {
                if (pckgName.startsWith("com/googlecode")) {
                    return true;
                }
                packagesThatWereNOTInstrumented.add(pckgName);
                return false;
            }
        }

        // Otherwise instrument by default
        packagesThatWereInstrumented.add(pckgName);
        return true;
    }
}
