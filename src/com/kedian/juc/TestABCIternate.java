package com.kedian.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.juc
 * @Description:
 * @date 2019/7/31
 * 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印 10 遍，要求输出的结果必须按顺序显示。
 * 如：ABCABCABC…… 依次递归
 */
public class TestABCIternate {

    public static void main(String[] args) {
        AlternateDemo ad = new AlternateDemo();

        new Thread(() -> {
            for (int i = 1; i < 5; i++) {
                ad.loopA(i);
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 1; i < 5; i++) {
                ad.loopB(i);
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 1; i < 5; i++) {
                ad.loopC(i);
                System.out.println("--------------------");
            }
        }, "C").start();
    }
}

//轮询对象
class AlternateDemo {
    //当前正在执行线程的表记
    private int number = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    /**
     * @param totalLoop 循环第几轮
     */
    public void loopA(int totalLoop) {
        lock.lock();
        try {
            //1.判断该执行的线程
            if (number != 1) {
                condition1.await();
            }
            //2.打印结果
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 2;
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopB(int totalLoop) {
        lock.lock();
        try {
            //1.判断该执行的线程
            if (number != 2) {
                condition2.await();
            }
            //2.打印结果
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC(int totalLoop) {
        lock.lock();
        try {
            //1.判断该执行的线程
            if (number != 3) {
                condition3.await();
            }
            //2.打印结果
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }
            //3.唤醒
            number = 1;
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
