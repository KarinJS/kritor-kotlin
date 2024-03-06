# 鉴权服务

Kritor提供的基础鉴权操作，可避免grpc泄露导致的被骇入操作。

## 基础信息

- **服务名**: `Authentication`
- **Java包名**: `io.kritor`
- **C#命名空间**: `Kritor`
- **[source proto file](https://github.com/whitechi73/kritor/blob/master/protos/src/main/proto/kritor/authenticate.proto)**

## 鉴权

服务端鉴权操作，用于验证客户端的合法性。在某个账号授权成功之后，接下来所有的请求都必须围绕该账号，且禁止再次发送授权包。

### 参数

- **方法名**: `Auth`
- **请求类型**: `AuthReq`
- **响应类型**: `AuthRsp`

### 请求

```protobuf
enum AuthCode {
  OK = 0;
  NO_ACCOUNT = 1;
  NO_TICKET = 2;
  LOGIC_ERROR = 3;
}

message AuthReq {
  string ticket = 1; // 客户端连接认证ticket
  string account = 2; // 客户端连接认证账号
}

message AuthRsp {
  AuthCode code = 1; // 认证结果
  string msg = 2; // 错误信息
}
```
## 获取鉴权Ticket （WebUI）

WebUI通过superTicket获取鉴权ticket，用于实现远程控制kritor。

### 参数

- **方法名**: `GetTicket`
- **请求类型**: `GetTicketReq`
- **响应类型**: `GetTicketRsp`
