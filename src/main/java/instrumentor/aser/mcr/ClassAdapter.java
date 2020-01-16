package instrumentor.aser.mcr;

import instrumentor.aser.instrumentation.RVGlobalStateForInstrumentation;
import org.objectweb.asm.*;


public class ClassAdapter extends ClassVisitor {

    private String classname;
	private String source;
	private boolean isThreadClass;
	private boolean isRunnableClass;

    private static final String RUN_METHOD_NAME = "run";

	public ClassAdapter(ClassVisitor classVisitor) {
		super(Opcodes.ASM5, classVisitor);
	}

	/**
     * 访问类的头部
	 * @param version 类的版本
	 * @param access  类的修饰符
	 * @param name  类的名称
	 * @param signature 类的签名，如果类不是泛型或者没有继承泛型类，那么signature为空；
	 * @param superName 类的父类名称
	 * @param interfaces
	 */
	@Override
	public void visit(int version,
					  int access, String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
		//记录当前插桩类名
		this.classname = name;
		//记录是否是线程或者线程的子类
		isThreadClass = RVGlobalStateForInstrumentation.instance.isThreadClass(classname);
		//记录这个类或者其父类是否实现了Runnable接口
	    isRunnableClass = RVGlobalStateForInstrumentation.instance.isRunnableClass(classname);
	}
	
	//访问类的源码
	@Override
    public void visitSource(String source, String debug) {
        this.source = source;
        if (cv != null) {
            cv.visitSource(source, debug);
        }
    }


    /**
     * 访问一个类的域信息，修改
	 * @param access 表示该域的访问方式
	 * @param name 域的名称
	 * @param desc 域的描述，一般指参数类型
	 * @param signature  域的签名
	 * @param value   域的初始值
	 * @return  返回一个可以访问该域注解和属性的访问对象
	 */

    public FieldVisitor visitField(int access, String name, String desc,
								   String signature, Object value)
	{
        String sig_var = (classname+"."+name).replace("/", ".");
        //RVGlobalStateForInstrumentation是一个记录运行时全局状态的类
		//在variableIdMap<String,Integer>以<classname+variableName, id>对记录了变量id从0开始自增
        RVGlobalStateForInstrumentation.instance.getVariableId(sig_var);
        if((access & Opcodes.ACC_VOLATILE)!=0)
        {
            RVGlobalStateForInstrumentation.instance.addVolatileVariable(sig_var);
        }
        
        if (cv != null) {
            return cv.visitField(access, name, desc, signature, value);
        }
        return null;
    }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
									 String signature, String[] exceptions) {

	    MethodVisitor mv = cv.visitMethod(access&(~Opcodes.ACC_SYNCHRONIZED),
                name, desc, signature, exceptions);

		if (mv != null) {
            boolean isSynchronized = false;
            boolean isStatic = false;

            if((access & Opcodes.ACC_SYNCHRONIZED)!=0)
                isSynchronized = true;
            if((access & Opcodes.ACC_STATIC)!=0)
                isStatic = true;
           
            boolean possibleRunMethod = name.equals(RUN_METHOD_NAME) && !isStatic
                    && (Type.getArgumentsAndReturnSizes(desc) >> 2) == 1 &&
                    Type.getReturnType(desc).equals(Type.VOID_TYPE) &&
                    (isThreadClass||isRunnableClass);

            mv = new MethodAdapter(mv,
			        source, 
			        access, 
			        desc, 
			        classname,
			        name,name+desc,name.equals("<init>")||name.equals("<clinit>"), 
			        isSynchronized,
			        isStatic,
			        possibleRunMethod);
		}
		return mv;
	}
}
