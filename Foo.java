import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Foo {
    static boolean startSecond;
    static boolean startThird;

    synchronized void startSecond() {
        startSecond = true;
        notify();
    }

    synchronized void startThird() {
        startThird = true;
        notify();
    }

    synchronized void stopThread() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void first(Runnable r) {
        System.out.println("first");
        startSecond();
    }

    public void second(Runnable r) {
        while (startSecond == false) {
            stopThread();
        }
        System.out.println("second");
        startThird();

    }

    public void third(Runnable r) {
            while (startThird == false) {}
            System.out.println("third");
        }

}

class FooDemo {
    public static void main(String[] args) {
        Foo foo = new Foo();
        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> foo.first(r));
        CompletableFuture.runAsync(() -> foo.third(r));
        CompletableFuture.runAsync(() -> foo.second(r));

        try {
            cf1.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Возникли трудности: " + e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


