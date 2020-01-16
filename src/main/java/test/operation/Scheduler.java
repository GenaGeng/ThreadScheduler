package test.operation;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {

    private static Map<Thread, ThreadInfo> livingThreadInfos;  //活跃线程的信息
    private static Set<ThreadInfo> pausedThreadInfos;  //暂停线程的信息
    private static ReentrantLock schedulerStateLock;

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
        schedulerStateLock = new ReentrantLock();
    }

    public static void beforeFieldAccess(boolean isRead, String methodName, String fieldName, String desc, int lineNumber) {
//        beforeEvent(isRead, methodName, fieldName, desc,lineNumber);
        System.out.println("11111111");
    }

    public static void afterFieldAccess(boolean isRead, String owner, String name, String desc,int lineNumber) {
        System.out.println("22222222");

    }

    public static void beforeEvent(boolean isRead, String methodName, String name, String desc,int lineNumber) {

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
        String action = isRead ? "读" : "写";
        System.out.println("线程" + Thread.currentThread().getName() + " 正在" + action + "变量" + name + ",描述符为 " + desc + ",行数为" + lineNumber);
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

    public static ThreadInfo choose() {
        System.out.println("=====================当前暂停的线程信息======================");
        pausedThreadInfos.forEach(System.out::println);
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        while (choice > pausedThreadInfos.size()) {
            System.out.println("请输入一个正确的index");
            choice = input.nextInt();
        }
        return new ArrayList<>(pausedThreadInfos).get(choice - 1);
    }

}
