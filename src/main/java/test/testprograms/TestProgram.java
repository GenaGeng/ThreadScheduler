package test.testprograms;



import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 控制测试的执行
 * @author phantom
 * @date 2018/10/22
 */
public class TestProgram {

    /**
     * 测试场景：
     * sequentialAndsequential表示顺序（添加元素）顺序（删除元素）
     * concurrentAndconcurrent表示并发并发
     * sequentialAndconcurrent表示顺序并发
     * concurrentAndsequential表示并发顺序
     * */
    private static final String METHODNAME_SEQUENANDSEQUEN = "sequentialAndsequential" ;

    private static final String METHODNAME_SEQUENANDCONCU = "sequentialAndconcurrent" ;

    private static final String METHODNAME_CONCUANDSEQUEN = "concurrentAndsequential" ;

    private static final String METHODNAME_CONCUANDCONCU = "concurrentAndconcurrent" ;

    private static final String METHODNAME_GETRESULTS = "getResults" ;

    private static final String METHODNAME_SETDEFAULTNUMBER = "setDefaultnumber";

    /**service的类对象*/
    private Class serviceClass;

    /**service的构造器实例*/
    private Constructor serviceConstructor;

    /**service类的实例*/
    private Object instance ;

    /**service的方法实例*/
    private Method servicemethod_sequentialAndsequential;

    /**service的方法实例*/
    private Method servicemethod_sequentialAndconcurrent;

    /**service的方法实例*/
    private Method servicemethod_concurrentAndsequential;

    /**service的方法实例*/
    private Method servicemethod_concurrentAndconcurrent;

    /**service的方法实例*/
    private Method servicemethod_getResults;

    private Method servicemethod_setDefaultNumber;

    /**
     * 初始化service
     * @param fullServiceName 测试类的绝对路径
     */
    public void initializeServiceInstance(int index, String fullServiceName){
        try {
            if (serviceClass == null){
                serviceClass = Class.forName(fullServiceName);
            }
            if (serviceConstructor == null){
                serviceConstructor = serviceClass.getConstructor(null);
            }
            if (instance == null){
                instance = serviceConstructor.newInstance(null);
            }
            servicemethod_getResults = serviceClass.getMethod(METHODNAME_GETRESULTS,null);

            switch (index) {
                case 0 :
                    servicemethod_sequentialAndsequential = serviceClass.getMethod(METHODNAME_SEQUENANDSEQUEN,
                            int[].class,String.class,int.class);
                    break;
                case 1:
                    servicemethod_sequentialAndconcurrent = serviceClass.getMethod(METHODNAME_SEQUENANDCONCU,
                            int[].class,String.class,int.class);
                    break;
                case 2:
                    servicemethod_concurrentAndsequential = serviceClass.getMethod(METHODNAME_CONCUANDSEQUEN,
                            int[].class,String.class,int.class);
                    break;
                case 3:
                    servicemethod_concurrentAndconcurrent = serviceClass.getMethod(METHODNAME_CONCUANDCONCU,
                            int[].class,String.class,int.class);
                    break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



    /**
     * 根据变异体的绝对路径、测试场景、开启的线程数目、以及传入的数据执行测试
     * @param index 测试场景
     * @param numberOfThreads 开启的线程数目
     * @param mutantFullName 变异体的绝对路径
     * @param array 传入的数据
     */
    public void executeService(int index, int numberOfThreads, String serviceName, String mutantFullName, int[] array) {
        initializeServiceInstance(index, serviceName);
        try {
            switch (index) {
                case 0:
                    servicemethod_sequentialAndsequential.invoke(instance,array,mutantFullName,numberOfThreads);
                    break;
                case 1:
                    servicemethod_sequentialAndconcurrent.invoke(instance,array,mutantFullName,numberOfThreads);
                    break;
                case 2:
                    servicemethod_concurrentAndsequential.invoke(instance,array,mutantFullName,numberOfThreads);
                    break;
                case 3:
                    servicemethod_concurrentAndconcurrent.invoke(instance,array,mutantFullName,numberOfThreads);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置从列表中取出的数据的个数
     * @param defaultNumber 设置的值，如果步设置该值，默认取出的数据是10
     */
    public void setDefaultNumber(int defaultNumber, String fullServiceName){
        try {
            serviceClass = Class.forName(fullServiceName);
            serviceConstructor = serviceClass.getConstructor(null);
            instance = serviceConstructor.newInstance(null);
            servicemethod_setDefaultNumber = serviceClass.getMethod(METHODNAME_SETDEFAULTNUMBER, int.class);
            servicemethod_setDefaultNumber.invoke(instance,defaultNumber);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取当前测试数据对应的执行结果
     * @return 执行结果
     */
    public int[] getResults(){
        int[] tempArray = null;
        try {
            tempArray = (int[]) servicemethod_getResults.invoke(instance,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return tempArray;
    }


    public int[] executeServiceAndGetResult(int index, int numberOfThreads, String serviceName, String mutantFullName, int[] array){
        executeService(index,numberOfThreads,serviceName,mutantFullName,array);
        int[] tempArray = getResults();
        return tempArray ;
    }


}
