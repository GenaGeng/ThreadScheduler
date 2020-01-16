package instrumentor.aser.instrumentation;

import instrumentor.aser.icb.FireEventsClassTransformer;
import instrumentor.aser.icb.JUCEventsClassTransformer;
import instrumentor.aser.icb.SharedAccessEventsClassTransformer;
import instrumentor.aser.icb.ThreadEventsClassTransformer;
import instrumentor.aser.mcr.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import test.Counter.Counter;
import test.operation.Ope;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author GN
 * @description
 * @date 2019/12/18
 */
public class Instrument {
    private static final String SLASH = "/";
    private static final String DOT = ".";
    private static final String SEMICOLON = ";";

    //
    public static String bufferClass;

    public static final String logClass = "edu/tamu/aser/runtime/RVRunTime";
    private static final String JUC_DOTS = "java.util.concurrent";

    private static final String INSTRUMENTATION_PACKAGES_DEFAULT = "default";
    public static final String INSTR_EVENTS_RECEIVER;

    private static Set<String> packagesThatWereInstrumented;
    private static Set<String> packagesThatWereNOTInstrumented;

    static {
        INSTR_EVENTS_RECEIVER = "edu.tamu.aser.reex.Scheduler".replace(DOT, SLASH);
        packagesThatWereInstrumented = new HashSet<String>();
        packagesThatWereNOTInstrumented = new HashSet<String>();
    }

    private static final String MCR_STRATEGY = "scheduler.aser.scheduling.strategy.MCRStrategy";

    //classes not instrumented
    private static final Set<String> pckgPrefixesToIgnore = new HashSet<String>();
    private static final Set<String> pckgsToIgnore = new HashSet<String>();
    private static final Set<String> classPrefixesToIgnore = new HashSet<String>();
    private static final Set<String> classesToIgnore = new HashSet<String>();
    private static final Set<String> pckgPrefixesToAllow = new HashSet<String>();
    private static final Set<String> pckgsToAllow = new HashSet<String>();
    private static final Set<String> classPrefixesToAllow = new HashSet<String>();
    private static final Set<String> classesToAllow = new HashSet<String>();

    //set the memory model, default: SC
    //other models supported: TSO, PSO
    public static String memModel;
    private static Boolean debug = true;

//    static MCRProperties mcrProps = MCRProperties.getInstance();
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_PACKAGES_IGNORE_PREFIXES_KEY), pckgPrefixesToIgnore);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_PACKAGES_IGNORE_KEY), pckgsToIgnore);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_CLASSES_IGNORE_PREFIXES_KEY), classPrefixesToIgnore);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_CLASSES_IGNORE_KEY), classesToIgnore);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_PACKAGES_ALLOW_PREFIXES_KEY), pckgPrefixesToAllow);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_PACKAGES_ALLOW_KEY), pckgsToAllow);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_CLASSES_ALLOW_PREFIXES_KEY), classPrefixesToAllow);
//    storePropertyValues(mcrProps.getProperty(MCRProperties.INSTRUMENTATION_CLASSES_ALLOW_KEY), classesToAllow);

//    memModel = mcrProps.getProperty(MCRProperties.memModel_KEY);
//    debug = Boolean.parseBoolean(mcrProps.getProperty("debug"));

//    static final String strategy = mcrProps.getProperty(MCRProperties.SCHEDULING_STRATEGY_KEY);
    public static void main(String[] args) throws Exception{

        String className = Counter.class.getName();

        if (shouldInstrumentClass(className)){
            if (debug){
                System.out.println("Instrumenting" + className);
            }

            ClassReader cr = new ClassReader(className);

            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

            ClassAdapter ca = new ClassAdapter(cw);

            cr.accept(ca, ClassReader.EXPAND_FRAMES);

            byte[] data = cw.toByteArray();

            File file = new File("E:\\project\\ThreadScheduler\\target\\classes\\" + className.replaceAll("\\.","/") + ".class");

            if (file.exists()){
                System.out.println("exists!");
            }

            FileOutputStream fop = new FileOutputStream(file);

            fop.write(data);

            fop.close();
        }

//        if (shouldInstrumentClass(className)) {
//            if (debug)
//            {
//                System.out.println("Instrumenting " + className);
//            }
//
//            ClassReader classReader = new ClassReader(className);
//            ClassWriter classWriter = new ExtendedClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
//
//            System.out.println("strategy:"+strategy);
//            if (MCR_STRATEGY.equals(strategy)) {
//                ClassAdapter classVisitor = new ClassAdapter(classWriter);
//
//                //in the accept method, it calls visitor.visit()
//                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
////                byte[] classfileBuffer = classWriter.toByteArray();
//            } else {
//                ThreadEventsClassTransformer threadEventsTransformer = new ThreadEventsClassTransformer(classWriter);
//                classReader.accept(threadEventsTransformer, ClassReader.EXPAND_FRAMES);
//
//                classReader = new ClassReader(classWriter.toByteArray());
//                classWriter = new ExtendedClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
//                SharedAccessEventsClassTransformer sharedAccessEventsTransformer = new SharedAccessEventsClassTransformer(classWriter);
//
//                classReader.accept(sharedAccessEventsTransformer, ClassReader.EXPAND_FRAMES);
//
//                classReader = new ClassReader(classWriter.toByteArray());
//                classWriter = new ExtendedClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
//                JUCEventsClassTransformer jucEventsTransformer = new JUCEventsClassTransformer(classWriter);
//                classReader.accept(jucEventsTransformer, ClassReader.EXPAND_FRAMES);
//
//                classReader = new ClassReader(classWriter.toByteArray());
//                classWriter = new ExtendedClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
//                FireEventsClassTransformer fireEventsTransformer = new FireEventsClassTransformer(classWriter);
//
//                classReader.accept(fireEventsTransformer, ClassReader.EXPAND_FRAMES);
//
////                byte[] classfileBuffer = classWriter.toByteArray();
//
//            }
//            File dir = new File("E:\\project\\ThreadScheduler\\target\\classes\\" );
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//
//            File classFile = new File(dir, className.replace("\\", "/") + ".class");
//            System.out.println("class file path: " + classFile);
////            classFile.createNewFile();
////            System.out.println("Writing " + className);
//            byte[] code = classWriter.toByteArray();
//            FileOutputStream fos = new FileOutputStream(classFile);
//            fos.write(code);
//            fos.close();
//
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
    private static void storePropertyValues(String values, Set<String> toSet) {
        if (values != null) {
            String[] split = values.split(SEMICOLON);
            for (String val : split) {
                val = val.replace(DOT, SLASH).trim();
                if (!val.isEmpty()) {
                    toSet.add(val);
                }
            }
        }
    }
}


//                /* Re-transform already loaded java.util.concurrent classes */
//                try {
//                List<Class<?>> classesToReTransform = new ArrayList<Class<?>>();
//        for (Class<?> loadedClass : inst.getAllLoadedClasses()) {
////                System.err.println("all loaded classes: " + loadedClass.toString());
//        if (inst.isModifiableClass(loadedClass) && loadedClass.getPackage().getName().startsWith(JUC_DOTS)) {
//        classesToReTransform.add(loadedClass);
//        }
//        }
//        inst.retransformClasses(classesToReTransform.toArray(new Class<?>[classesToReTransform.size()]));
//        } catch (UnmodifiableClassException e) {
//        e.printStackTrace();
//        System.err.println("Unable to modify a pre-loaded java.util.concurrent class!");
//        System.exit(2);
//        }
//        }
//
//}



