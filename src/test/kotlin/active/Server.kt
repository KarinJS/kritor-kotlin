package active

import io.grpc.ServerBuilder
import services.Authentication

/**
 * 创建一个主动Grpc服务端
 */
class KritorServer(
    port: Int,
) {
    private val server = ServerBuilder
        .forPort(port)
        .addService(Authentication)
        .build()!!

    fun start(block: Boolean = false) {
        server.start()
        if (block) {
            server.awaitTermination()
        }
    }
}

fun main() {
    KritorServer(8080)
        .start(true)
}