import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class Foo {

    private final Semaphore semaphore1 = new Semaphore(1);
    private final CountDownLatch latch = new CountDownLatch(1);


    public void first(Runnable r) {
        try {
            semaphore1.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("first");
        semaphore1.release();
    }

    public void second(Runnable r) {
        try {
            semaphore1.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore1.release();
        System.out.println("second");
        latch.countDown();

    }

    public void third(Runnable r) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("third");
    }
}

class FooDemo {
    public static void main(String[] args) {
        Foo foo = new Foo();
        Runnable r = () -> {

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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


