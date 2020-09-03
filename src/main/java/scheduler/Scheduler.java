package scheduler;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {

    private static Map<Thread, ThreadInfo> livingThreadInfos;
    private static Set<ThreadInfo> pausedThreadInfos;
    private static ReentrantLock schedulerStateLock;

    //设置为守护进程，一直循环下去
    static {
        initState();
        Thread thread = new Thread(() -> {
            while (true) {
                schedulerStateLock.lock();
                try {
                    if (!livingThreadInfos.isEmpty() && !pausedThreadInfos.isEmpty()) {
                        ThreadInfo threadInfo = choose();
                        pausedThreadInfos.remove(threadInfo);
                        threadInfo.getPausingSemaphore().release();
                    }
                } finally {
                    schedulerStateLock.unlock();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static void initState() {
        livingThreadInfos = new HashMap<>();
        pausedThreadInfos = new HashSet<>();
        //创建一个非公平可重入锁
        schedulerStateLock = new ReentrantLock();
    }

    public static void beforeMethodAccess(boolean isSetAcc, String methodName, String desc){
        beforeSet(isSetAcc, methodName, desc);
    }

    public static void beforeSet(boolean isSetAcc, String methodNme, String desc){

        ThreadInfo currentTI;
        try {
            schedulerStateLock.lock();
            Thread currentThread = Thread.currentThread();

            if (!livingThreadInfos.containsKey(currentThread)) {
                currentTI = new ThreadInfo(currentThread);
                livingThreadInfos.put(currentThread, currentTI);
                pausedThreadInfos.add(currentTI);
            } else {
                currentTI = livingThreadInfos.get(currentThread);
                pausedThreadInfos.add(currentTI);
            }

        } finally {
            schedulerStateLock.unlock();

        }
        String action = isSetAcc ? "setAcc" : "非setAcc";
        System.out.println("线程" + Thread.currentThread().getName() + " 正在" + action + ",方法名:" + methodNme + ",描述符为 " + desc);
        if (currentTI != null) {
            try {
                System.out.println(1);
                currentTI.getPausingSemaphore().acquire();
                System.out.println(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void beforeFieldAccess(boolean isRead, String methodName, String fieldName, String desc, int lineNumber) {
        beforeEvent(isRead, methodName, fieldName, desc);
    }

    public static void afterFieldAccess(boolean isRead, String owner, String name, String desc) {

    }

    public static void beforeEvent(boolean isRead, String methodName, String name, String desc) {

//        ThreadInfo currentTI;
//        try {
//            schedulerStateLock.lock();
//            Thread currentThread = Thread.currentThread();
//
//            if (!livingThreadInfos.containsKey(currentThread)) {
//                currentTI = new ThreadInfo(currentThread);
//                livingThreadInfos.put(currentThread, currentTI);
//                pausedThreadInfos.add(currentTI);
//            } else {
//                currentTI = livingThreadInfos.get(currentThread);
//                pausedThreadInfos.add(currentTI);
//            }
//
//        } finally {
//            schedulerStateLock.unlock();
//
//        }
//        String action = isRead ? "读" : "写";
//        System.out.println("线程" + Thread.currentThread().getName() + " 正在" + action + "变量" + name + ",描述符为 " + desc);
//        if (currentTI != null) {
//            try {
//                System.out.println(1);
//                currentTI.getPausingSemaphore().acquire();
//                System.out.println(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static ThreadInfo choose() {
        System.out.println("=====================当前暂停的线程信息======================");
        pausedThreadInfos.forEach(System.out::println);
        Scanner input = new Scanner(System.in);
        //把输入的整型数字赋值给choice
        int choice = input.nextInt();
        while (choice > pausedThreadInfos.size()) {
            System.out.println("请输入一个正确的index");
            choice = input.nextInt();
        }
        return new ArrayList<>(pausedThreadInfos).get(choice - 1);
    }

}
