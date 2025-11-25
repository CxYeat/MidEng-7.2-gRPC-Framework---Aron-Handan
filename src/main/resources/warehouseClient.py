import sys
import grpc
import warehouse_pb2, warehouse_pb2_grpc

def main():
    # Beispiel ProduktID oder aus Argument
    product_id = sys.argv[1] if len(sys.argv) > 1 else "1234"

    # Verbindung zum gRPC Server
    with grpc.insecure_channel("localhost:50051") as channel:
        stub = warehouse_pb2_grpc.WarehouseServiceStub(channel)

        # Korrekte Nachricht WarehouseData
        request = warehouse_pb2.WarehouseData(
            warehouseID="W1",
            warehouseAddress="Musterstraße 1",
            warehousePostalCode="1010",
            warehouseCity="Wien",
            warehouseCountry="AT",
            timestamp="2025-11-24T22:00:00",
            productData=[
                warehouse_pb2.ProductData(
                    productID=product_id,
                    productName="Beispielprodukt",
                    productCategory="Kategorie1",
                    productQuantity=100,
                    productUnit="Stück"
                )
            ]
        )
        # storeWarehouse aufrufen
        response = stub.storeWarehouse(request)
        # Antwort ausgeben
        print("Antwort vom Server:")
        print(response.text, response.isSuccess)

if __name__ == "__main__":
    main()
