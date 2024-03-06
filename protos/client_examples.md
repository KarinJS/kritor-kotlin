# 客户端各语言示例

## Kotlin客户端代码示例

建立一个管道，实例化Grpc的服务，发送一个鉴权请求。

```kotlin
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
        println(Status.fromThrowable(it))
    }

}
```

