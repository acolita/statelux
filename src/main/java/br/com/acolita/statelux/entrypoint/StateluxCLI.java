package br.com.acolita.statelux.entrypoint;
import br.com.acolita.statelux.Statelux;
import java.io.IOException;

public class StateluxCLI {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.currentThread().setName("Statelux - main");
        final Statelux statelux = new Statelux();
        statelux.run();
        Thread.sleep(10_000);
    }
}
