package com.kanlon.service;

import com.kanlon.model.CommonResponse;
import com.kanlon.model.ConsumerRequest;
import com.kanlon.proto.GreeterGrpc;
import com.kanlon.proto.GreeterOuterClass;
import io.grpc.Channel;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * rpc调用远程方法的service
 *
 * @author zhangcanlong
 * @since 2019-05-04
 **/
@Service
public class GrpcConsumerService {

    @Autowired
    private GrpcChannelFactory grpcChannelFactory;

    /**
     * 通过rpc调用远程方法
     *
     * @param request 请求参数实体类
     * @return com.kanlon.model.CommonResponse
     **/
    public CommonResponse callMethod(ConsumerRequest request) {
        //TODO 后期提取通道出来
        Channel channel = grpcChannelFactory.createChannel(request.getProviderName());
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        //构造远程调用的请求参数类
        GreeterOuterClass.ParamRequest paramRequest =
                GreeterOuterClass.ParamRequest.newBuilder().setParam1(request.getParam1()).setParam2(request.getParam2()).setType(request.getType()).build();
        // 远程请求
        GreeterOuterClass.CommonResponseReply response = stub.callMethod(paramRequest);
        // 如果执行正确的，返回正确的信息
        if (response.getCode() == 1) {
            return CommonResponse.succeedResult(response.getDataList());
        } else {
            //执行失败，返回失败的信息
            return CommonResponse.failedResult(response.getMessage(), response.getCode());
        }
    }
}
