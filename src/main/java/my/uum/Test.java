package my.uum;

public class Test {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " start");
        MyThread t1 = new MyThread();
        t1.setName("T1");
        t1.setDaemon(true);
        t1.start();
        System.out.println(Thread.currentThread().getName()+ " End");
    }
}
