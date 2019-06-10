package com.kanlon.service;


import com.kanlon.common.ConstantUtils;
import com.kanlon.job.EmailOnceJob;
import com.kanlon.job.HttpOnceJob;
import com.kanlon.job.ShellOnceJob;
import com.kanlon.model.CommonResponse;
import com.kanlon.proto.GreeterGrpc;
import com.kanlon.proto.GreeterOuterClass;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * grpc服务端提供的service
 *
 * @author zhangcanlong
 * @since 2019/4/25 9:12
 **/
@GrpcService
public class GrpcServerService extends GreeterGrpc.GreeterImplBase {

    @Autowired
    private EmailOnceJob emailOnceJob;
    @Autowired
    private HttpOnceJob httpOnceJob;
    @Autowired
    private ShellOnceJob shellOnceJob;

    /**
     * 提供的方法
     *
     * @param request          请求
     * @param responseObserver 返回
     **/
    @Override
    public void callMethod(GreeterOuterClass.ParamRequest request,
            StreamObserver<GreeterOuterClass.CommonResponseReply> responseObserver) {
        String param1 = request.getParam1();
        String param2 = request.getParam2();
        String type = request.getType();
        //设置返回数据
        GreeterOuterClass.CommonResponseReply.Builder builder = GreeterOuterClass.CommonResponseReply.newBuilder();
        //邮件任务
        CommonResponse commonResponse;
        if (ConstantUtils.EMAIL_STR.equals(type)) {
            commonResponse = emailOnceJob.executeInternal(param1, param2);
        } else if (ConstantUtils.HTTP_STR.equals(type)) {
            commonResponse = httpOnceJob.executeInternal(param1);
        } else if (ConstantUtils.SHELL_STR.equals(type)) {
            commonResponse = shellOnceJob.executeInternal(param1);
        } else {
            commonResponse = CommonResponse.failedResult("发送的执行任务类型有误！");
        }
        builder.setCode(commonResponse.getCode());
        builder.setMessage(commonResponse.getMessage());
        if (commonResponse.getResultData() != null) {
            builder.addData(commonResponse.getResultData().toString());
        }
        GreeterOuterClass.CommonResponseReply responseReply = builder.build();
        responseObserver.onNext(responseReply);
        responseObserver.onCompleted();
        super.callMethod(request, responseObserver);
    }

}
