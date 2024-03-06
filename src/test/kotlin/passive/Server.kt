package passive

import io.grpc.ServerBuilder
import io.kritor.ReverseServiceGrpcKt
import io.kritor.authReq
import io.kritor.reverse.Request
import io.kritor.reverse.Response
import io.kritor.reverse.request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.util.*

class PassiveKritorServer(
    port: Int,
) {
    private val requestList = Collections.synchronizedList(mutableListOf<Request>()) // 用于存储请求包

    private val server = ServerBuilder
        .forPort(port)
        .addService(object: ReverseServiceGrpcKt.ReverseServiceCoroutineImplBase() {
            override fun reverseStream(requests: Flow<Response>): Flow<Request> {
                // 这里Kritor端建立了一个连接, 并且给予了一个Flow
                // 这个flow用于接收来自Kritor端的返回包
                requestList.add(request {
                    cmd = "Authentication.Auth"
                    buf = authReq {
                        account = "admin"
                        ticket = "admin"
                    }.toByteString()
                    seq = 1
                })

                // 这里给一个flow给Kritor端，让Kritor端可以获取到客户端发的请求包
                return channelFlow {
                    GlobalScope.launch {
                        requests.collect {
                            println("收到请求返回包：$it")
                        }
                        // 开启一个协程去接收来自kritor端的返回包
                    }

                    while (true) {
                        if (requestList.isNotEmpty()) {
                            send(requestList.removeFirst()) // 发送请求包
                        } // 如果有请求包，就发送
                        delay(10) // 没有就等待
                    }
                }
            }
        })
        .build()!!

    fun start(block: Boolean = false) {
        server.start()
        if (block) {
            server.awaitTermination()
        }
    }
}

fun main() {
    PassiveKritorServer(8080)
        .start(true)
}