import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.kritor.AuthRsp
import io.kritor.AuthenticationGrpc
import io.kritor.AuthenticationGrpc.AuthenticationStub
import io.kritor.AuthenticationGrpcKt
import io.kritor.authReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .enableRetry() // 允许尝试
        .executor(Dispatchers.IO.asExecutor()) // 使用协程的调度器
        .build()


    val observer: StreamObserver<AuthRsp> = object: StreamObserver<AuthRsp> {
        override fun onCompleted() {

        }

        override fun onNext(rsp: AuthRsp?) {

        }

        override fun onError(e: Throwable?) {

        }
    }

    AuthenticationGrpc.newStub(channel).auth(authReq {
        account = "1145141919810"
        ticket = "A123456"
    }, observer)

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
        println(status.code)
        println(status.description)
    }


}