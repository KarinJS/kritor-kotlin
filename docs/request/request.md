# 请求响应相关

## 请求与响应

Grpc提供了`挂起/堵塞`和`异步`的两种请求方式。

### 异步 (流式传输)

```kotlin
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
            // doSomething
        }

        override fun onError(e: Throwable?) {

        }
    }

    AuthenticationGrpc.newStub(channel).auth(authReq {
        account = "1145141919810"
        ticket = "A123456"
    }, observer)
}
```

### 挂起

```kotlin
suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .enableRetry() // 允许尝试
        .executor(Dispatchers.IO.asExecutor()) // 使用协程的调度器
        .build()

    val stub = AuthenticationGrpcKt.AuthenticationCoroutineStub(channel)
    val rsp = stub.auth(authReq {
        account = "1145141919810"
        ticket = "A123456"
    })
}
```

### 其它语言的请求示例

- [Java](https://grpc.io/docs/languages/java/basics/#calling-service-methods)（同步请求）
- [Java](https://grpc.io/docs/languages/java/basics/#client-side-streaming-rpc)（流式异步）
- [C#](https://learn.microsoft.com/zh-cn/aspnet/core/tutorials/grpc/grpc-start?view=aspnetcore-8.0&tabs=visual-studio#create-the-grpc-client-in-a-net-console-app) （微软文档）
- [C++](https://grpc.io/docs/languages/cpp/basics/#calling-service-methods)
- [Golang](https://grpc.io/docs/languages/go/basics/#calling-service-methods)
- [Node](https://grpc.io/docs/languages/node/basics/#calling-service-methods)
- [Object-C](https://grpc.io/docs/languages/objective-c/basics/#calling-service-methods)
- [PHP](https://grpc.io/docs/languages/php/basics/#calling-service-methods)
- [Python](https://grpc.io/docs/languages/python/basics/#calling-service-methods)

## 请求错误处理

当Kritor端无法正常处理某个请求，或者请求失败的时候，将会使用Grpc的错误码来返回错误信息。 其中大量错误信息的描述通常在``status.description``中。

除去Grpc用于保证传输稳定和网络波动的那几个状态码外，Kritor端还会使用以下状态码：

- `OK`: 一切正常。
- `INVALID_ARGUMENT`: 参数错误，例如群禁言没提供群号。
- `UNAUTHENTICATED`: 未认证，通常是鉴权失败或者越级调用。
- `PERMISSION_DENIED`: 权限不足，例如没有权限解除群禁言或者无权使用某个服务。
- `INTERNAL`: Kritor内部出现问题，例如数据库连接失败或者其他异常。

我们通过几个简单的Kotlin代码示例来演示如何处理请求错误。

如果需要查看错误码的详细信息，可以查看[官方文档](https://github.com/grpc/grpc/blob/master/doc/statuscodes.md)。

### Kotlin

```kotlin
suspend fun main() {
    val channel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext()
        .enableRetry() // 允许尝试
        .executor(Dispatchers.IO.asExecutor()) // 使用协程的调度器
        .build()
    
    val stub = AuthenticationGrpcKt.AuthenticationCoroutineStub(channel)
    runCatching {
        val rsp = stub.auth(authReq {
            account = "1145141919810"
            ticket = "A123456"
        })
        println(rsp.code)
    }.onFailure {
        // 如果错误，打印错误信息
        val status = Status.fromThrowable(it)
        println(status) // 直接打印code + cause + description
        println(status.code) // 打印错误码
        println(status.description) // 打印错误描述
    }
}
```

更多语言请查看[Grpc官方错误处理示例](https://grpc.io/docs/guides/error/)。

## 实现接口

Kritor提供了多种接口供客户端调用，包括但不限于以下服务：

> - ### [鉴权服务](/docs/request/authentication.md)
>
> - ### [核心服务](/docs/request/core.md)
>
> - ### [群聊服务](/docs/request/group.md)
> 
> - ### [好友服务](/docs/request/friend.md)
> 
> - ### [消息服务](/docs/request/message.md)
> 
> - ### [文件服务](/docs/request/file.md)
> - ### [Web服务](/docs/request/web.md)
