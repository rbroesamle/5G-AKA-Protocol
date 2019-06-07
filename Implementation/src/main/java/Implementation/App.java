package Implementation;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

public class App {

    public static void main(String[] args) {
        try {
            Flowable.range(1, 10)
                    .parallel()
                    .runOn(Schedulers.newThread())
                    .sequential()
                    .subscribe(System.out::println);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
