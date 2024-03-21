package passive

import io.grpc.ManagedChannelBuilder
import io.kritor.AuthCode
import io.kritor.AuthReq
import io.kritor.ReverseServiceGrpcKt
import io.kritor.authRsp
import io.kritor.reverse.ReqCode
import io.kritor.reverse.Response
import io.kritor.reverse.response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import java.util.*

suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .enableRetry() // 允许尝试
        .executor(Dispatchers.IO.asExecutor()) // 使用协程的调度器
        .build()

    val packetList = Collections.synchronizedList(mutableListOf<Response>()) // 用于存储返回包

    val stub = ReverseServiceGrpcKt.ReverseServiceCoroutineStub(channel)
    val receiver = stub.reverseStream(channelFlow {
        while (true) {
            if (packetList.isNotEmpty()) {
                send(packetList.removeFirst()) // 发送返回包
            } // 如果有返回包，就发送
            delay(10) // 没有就等待
        } // 循环
    }) // 开启一个协程去接收来自客户端的请求包
    receiver.collect { request ->
        val command = request.cmd
        println("收包：$command")
        if (command == "Authentication.Auth") {
            val authReq = AuthReq.parser()
                .parseFrom(request.buf)
            println("鉴权请求：{ $authReq }")
            packetList.add(response {
                cmd = request.cmd
                buf = authRsp {
                    code = AuthCode.OK
                }.toByteString()
                seq = request.seq
                code = ReqCode.SUCCESS
                msg = "success"
            })
        }
    }
}