# Kritor Proto文件解释

## 主动RPC

什么是主动RPC？
即**Kritor**作为Server， Client作为GRPC CLIENT，客户端可以通过构造Stub对象来对Kritor端进行请求操作。

### 代码示例

以下是主动RPC的代码示例，不是被动RPC的哦！

- [Kotlin]()
- [Java]()
- [Python]()
- [Go]()
- [C#]()
- [C++]()
- [Rust]()
- [JavaScript]()

## 被动RPC

因为Grpc并没有明显的服务端直接rpc调用客户端的方法。
这里**Kritor**采用双向流来实现这一操作。

