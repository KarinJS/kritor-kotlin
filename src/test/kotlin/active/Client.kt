package active

import active.test.messageTest
import active.test.testContact
import active.test.testCore
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.kritor.AuthRsp
import io.kritor.AuthenticationGrpc
import io.kritor.AuthenticationGrpcKt
import io.kritor.authReq
import io.kritor.event.EventServiceGrpcKt
import io.kritor.event.EventType
import io.kritor.event.requestPushEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

fun async(channel: Channel) {
    val observer: StreamObserver<AuthRsp> = object: StreamObserver<AuthRsp> {
        override fun onCompleted() {

        }

        override fun onNext(rsp: AuthRsp?) {
            println(rsp)
        }

        override fun onError(e: Throwable?) {

        }
    }

    AuthenticationGrpc.newStub(channel).auth(authReq {
        account = "1145141919810"
        ticket = "A123456"
    }, observer) // 主动grpc请求
}

suspend fun await(channel: Channel) {
    val stub = AuthenticationGrpcKt.AuthenticationCoroutineStub(channel)
    runCatching {
        val rsp = stub.auth(authReq {
            account = "1145141919810"
            ticket = "A123456"
        })
        println(rsp)
    }.onFailure {
        val status = Status.fromThrowable(it)
        println(status)
        println(status.code)
        println(status.description)
    }
}

suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("192.168.1.214", 5700)
        .usePlaintext()
        .enableRetry() // 允许尝试
        .executor(Dispatchers.IO.asExecutor()) // 使用协程的调度器
        .build()


    async(channel) // 异步grpc请求

    await(channel) // 同步grpc请求

    testCore(channel)
    testContact(channel)
    //messageTest(channel)

    EventServiceGrpcKt.EventServiceCoroutineStub(channel).registerActiveListener(requestPushEvent {
        type = EventType.EVENT_TYPE_MESSAGE
    }).collect {
        println(it)
    }
}