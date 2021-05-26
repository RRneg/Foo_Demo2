import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class Foo {

    private final CountDownLatch latch1 = new CountDownLatch(1);
    private final CountDownLatch latch = new CountDownLatch(1);


    public void first(Runnable r) {
        System.out.println("first");
        latch1.countDown();
        }

    public void second(Runnable r) {
        try {
            latch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> foo.second(r));

        CompletableFuture.runAsync(() -> foo.third(r));

        CompletableFuture.runAsync(() -> foo.first(r));

        try {
            cf1.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Возникли трудности: " + e);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


