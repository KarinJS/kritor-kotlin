package active.test

import io.grpc.Channel
import io.kritor.core.KritorServiceGrpcKt
import io.kritor.core.getCurrentAccountRequest
import io.kritor.core.getVersionRequest

suspend fun testCore(channel: Channel) {
    testGetVersion(channel)
    testGetCurrentAccount(channel)
}

private suspend fun testGetVersion(channel: Channel) {
    val stub = KritorServiceGrpcKt.KritorServiceCoroutineStub(channel)
    println("Version => " + stub.getVersion(getVersionRequest {  }))
}

private suspend fun testGetCurrentAccount(channel: Channel) {
    val stub = KritorServiceGrpcKt.KritorServiceCoroutineStub(channel)
    println("CurrentAccount => " + stub.getCurrentAccount(getCurrentAccountRequest {  }))
}



