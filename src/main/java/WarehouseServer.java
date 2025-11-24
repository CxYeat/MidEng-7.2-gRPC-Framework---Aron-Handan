import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class WarehouseServer {

    private static final int PORT = 50051;
    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(PORT)
                .addService(new WarehouseServiceImpl())
                .build()
                .start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }
        server.awaitTermination();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        WarehouseServer server = new WarehouseServer();
        System.out.println("\n\n");
        System.out.println("Warehouse Service is running!");
        System.out.println("\n\n");
        server.start();
        server.blockUntilShutdown();
    }
}
