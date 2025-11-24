import io.grpc.stub.StreamObserver;

public class WarehouseServiceImpl extends WarehouseServiceGrpc.WarehouseServiceImplBase {

    @Override
    public void storeWarehouse(Warehouse.WarehouseData request, StreamObserver<Warehouse.StoreResponse> responseObserver) {
        System.out.println("Store Warehouse request: " + request.toString());
        StringBuilder text = new StringBuilder("Handling warehouse endpoint\n")
                .append("Warehouse ID: ").append(request.getWarehouseID()).append("\n")
                .append("Warehouse Address: ").append(request.getWarehouseAddress()).append("\n")
                .append("Warehouse Country: ").append(request.getWarehouseCountry()).append("\n")
                .append("Warehouse City: ").append(request.getWarehouseCity()).append("\n")
                .append("Warehouse Postal Code: ").append(request.getWarehousePostalCode()).append("\n")
                .append("Warehouse Timestamp: ").append(request.getTimestamp()).append("\n");

        for (Warehouse.ProductData product : request.getProductDataList()) {
            String productLine = String.format("Product ID: %s, Quantity: %d%n",
                    product.getProductID(), product.getProductQuantity());
            System.out.print(productLine);
            text.append(productLine);
        }
        Warehouse.StoreResponse response = Warehouse.StoreResponse.newBuilder()
                .setText(text.toString())
                .setIsSuccess(true)
                .build();

        // Antwort senden
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    /**
    @Override
    public void hello( Hello.HelloRequest request, StreamObserver<Hello.HelloResponse> responseObserver) {

        System.out.println("Handling hello endpoint: " + request.toString());

        String text = "Hello World, " + request.getFirstname() + " " + request.getLastname();
        Hello.HelloResponse response = Hello.HelloResponse.newBuilder().setText(text).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    */
}