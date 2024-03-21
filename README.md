# Kritor-kotlin

**Kritor** （OneBotX）是一个聊天机器人应用接口标准，
旨在统一腾讯QQ IM平台上的机器人应用开发接口 ，
使开发者只需编写一次业务逻辑代码即可应用到多种机器人平台。

**Kritor-kotlin** 是一个**Kritor**在Kotlin的实现。

## 客户端代码示例

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

## 使用项目

- [Shamrock](https://github.com/whitechi73/OpenShamrock)