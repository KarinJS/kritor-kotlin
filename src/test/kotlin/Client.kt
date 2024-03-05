import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import io.grpc.Status
import io.grpc.stub.AbstractAsyncStub
import io.kritor.Authenticate
import io.kritor.AuthenticationGrpc
import io.kritor.AuthenticationGrpc.AuthenticationStub
import io.kritor.AuthenticationGrpcKt
import io.kritor.authReq
import services.Authentication

suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .build()

    val stub = AuthenticationGrpcKt.AuthenticationCoroutineStub(channel)
    runCatching {
        val rsp = stub.auth(authReq {
            account = "1145141919810"
            ticket = "A123456"
        })
        println(rsp.code)
    }.onFailure {
        val status = Status.fromThrowable(it)
        println(status)
    }

}