package services

import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import io.kritor.*

object Authentication: AuthenticationGrpcKt.AuthenticationCoroutineImplBase() {
    override suspend fun auth(request: AuthReq): AuthRsp {
        val account = request.account
        if (account == "114514") {
            return authRsp {
                code = AuthCode.OK
            }
        }
        throw StatusException(Status.UNAUTHENTICATED.withDescription("6666"))
        return authRsp {
            code = AuthCode.NO_ACCOUNT
        }
    }

}