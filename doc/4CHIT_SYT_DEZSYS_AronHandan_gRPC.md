# DEZSYS_GK72_DATAWAREHOUSE_GRPC

**Autor:** Aron Handan

**Datum:** 11.11.2025

___

## gRPC Framework

**Q: What is gRPC and why does it work accross languages and platforms?**

**A:** gRPC is an open-source, high-performance framework for making remote procedure calls, allowing a client to call a method on a server as if it were a local  object. It uses HTTP/2 for transport, Protocol Buffers for a contract-first API definition, and supports many languages, making it ideal for building connected systems and microservices. gRPC follows a client-server model where the client calls methods on the server over 
the network as if they were local functions. **The architecture is designed to make this communication fast, reliable, and language-agnostic**

---

**Q:** Describe the RPC life cycle starting with the RPC client?

**A:** The client calls a remote method (e.g., `hello()`), and gRPC converts the request into a **Protocol Buffers** message that gets serialized into binary. This message is sent over the network to the server. The server receives and processes it, creates a response message, and sends it back to the client, which then decodes it and returns the result.

---

**Q:** Describe the workflow of Protocol Buffers?

**A:** First, a `.proto` file is created to define the structure of the data. Then, gRPC tools generate source code for different languages. When the program runs, data is **serialized** into a compact binary format for transmission and **deserialized** back into objects when received.

---

**Q:** What are the benefits of using protocol buffers?

**A:** They are **faster**, **smaller**, and **more efficient** than formats like JSON or XML, are **type-safe**, **backward compatible**, and work on many **languages and platforms**.

---

**Q:** When is the use of protocol not recommended?

**A:**  When data needs to be **human-readable** (e.g., configuration files or debugging) since Protocol Buffers store data in a **binary format**, not as plain text.

**Q:** List 3 different data types that can be used with protocol buffers?

**A:** `string`, `int32`, and `bool`.

---

## GK - Hello World

## hello.proto

This file defines a small gRPC service that receives a first and last name and returns a text response. The line `syntax = "proto3";` shows that it uses **Protocol Buffers (Proto)** , a format similar to JSON but **smaller and faster** because it stores data in **binary form**. Each field (`firstname`, `lastname`, `text`) has a unique number for efficient data transfer, and smaller numbers (1–15) use less space.

### HelloWorldServiceImpl.java

It's the actual code that **does the work**. When the client calls `stub.hello()`, **this class catches the request**, **assembles the response** ("Hello Max Mustermann!"), and **immediately sends it back** using the **`responseObserver`**. The **`onCompleted()`** method is just a final "Job done, goodbye!" signal to the client. The entire gRPC framework handles the "wiring," but this class provides the **message**.

### HelloWorldServer.java

Simply put, the **`HelloWorldServer`** class is the **Host** that runs your gRPC service. It **sets up the party** by using `ServerBuilder` to **open a port** (50051) and then calls **`.addService()`** to tell the gRPC framework: "Hey, use the logic found in **`HelloWorldServiceImpl`** for all incoming requests." After starting the server with `.start()`, the `blockUntilShutdown()` method acts like a **bouncer**; it makes the main program wait, keeping the server running and listening for client calls until you manually end it 

### HelloWorldClient.java

Simply put, the **`HelloWorldClient`** is the **customer** that wants to talk to the server. It first creates a **`ManagedChannel`**, which is essentially the **phone line** connected to the server at `localhost:50051`. Then, it generates a **`BlockingStub`**. This Stub is like a **local proxy** that lets you call the remote `hello` method as if it were running on your own machine. The client builds the request data (the names), sends the call using **`stub.hello(...)`**, **waits synchronously** for the server's reply, prints the result, and finally, **`channel.shutdown()`** hangs up the phone line when done.

___

## EK

### Things that got changed during the implementation of Warehouse

First, the **`.proto` file** is defined. This file specifies the gRPC service, including all messages and RPC methods. Based on this `.proto` definition, the Protocol Buffers compiler (`protoc`) automatically generates the required **client and server code** for each supported programming language (e.g., Java, Python, Go, C#, etc.).  
This means that the data structures do not need to be created manually — they are generated directly from the `.proto` specification.

```protobuf
// Which syntax this file uses.
syntax = "proto3";

// Service contract
service WarehouseService {
  rpc storeWarehouse(WarehouseData) returns (StoreResponse);
}

message ProductData {
  string productID = 1;
  string productName = 2;
  string productCategory = 3;
  int32 productQuantity = 4;
  string productUnit = 5;
}

// Request payload
message WarehouseData {
  string warehouseID = 1;
  string warehouseAddress = 2;
  string warehousePostalCode = 3;
  string warehouseCity = 4;
  string warehouseCountry = 5;
  string timestamp = 6;
  //List in gRPC
  repeated ProductData productData = 8;
}



// Response payload
message StoreResponse {
  string text = 1;
  bool isSuccess = 2;
}
```

### After Creating the `.proto` File

Once the `warehouse.proto` file has been created or modified, the project must be **built**.
During the build process, Gradle automatically runs the Protobuf generation task.

The generated Java gRPC classes will then appear in:

![](C:\Users\aronh\AppData\Roaming\marktext\images\2025-11-25-09-06-48-image.png)

These files contain the compiled message classes and the gRPC service stubs that the server uses.

### Implementing the gRPC Server Logic

When implementing the `WarehouseService`, you extend the class in case of our program its:

```java
public class WarehouseServiceImpl extends WarehouseServiceGrpc.WarehouseServiceImplBase
```

You must override the RPC method defined in your `.proto` file.  In this case, the server needs to override: 

```java
public void storeWarehouse(Warehouse.WarehouseData request, StreamObserver&lt;Warehouse.StoreResponse&gt; responseObserver)
```

## Extended Requirements – Further Details

For the extended requirements, the DataWarehouse client must be implemented in another programming language. In our case, Python was used, as this language was specified in the task instructions.

First of all there are some commands that are being used to create the **Virtual-Enviroment**:

```batch
python -m venv resources/venv
resources\venv\Scripts\Activate.ps1
python -m pip install grpcio grpcio-tools protobuf
python -m grpc_tools.protoc -Iproto --python_out=resources --grpc_python_out=resources proto/warehouse.proto
```

Create the virtual environment, Activate the environment, Install required gRPC packages inside the venv, generate Python gRPC source files from the `.proto` definition.

These steps create an isolated Python environment, install all necessary dependencies, and produce the generated Python gRPC files (`warehouse_pb2.py` and `warehouse_pb2_grpc.py`) inside the `resources/` directory.

```groovy
tasks.register('runPythonClient', Exec) {
    group = 'application'
    description = 'Runs the Python gRPC client'
    commandLine 'python', 'src/main/resources/warehouseClient.py'
}
```

I also defined in Gradle a task to simplify the command

![](C:\Users\aronh\AppData\Roaming\marktext\images\2025-11-25-09-26-47-image.png)
