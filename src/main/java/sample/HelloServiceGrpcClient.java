package sample;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sample.grpc.HelloReply;
import sample.grpc.HelloRequest;
import sample.grpc.HelloServiceGrpc;
import sample.grpc.HelloServiceGrpc.HelloServiceFutureStub;

/**
 * This client assumes that any client interceptors in the context are "global" and will apply those interceptors to the message channel.
 * In this, specific example, the grpc client tracing interceptor is the only one in the context.
 * 
 * This sample assumes only a single client, it should be straightfoward to add multiple clients and/or use ribbon.
 *  
 * @author tyler.vangorder
 *
 */
@Component
@EnableConfigurationProperties(GrpcClientProperties.class)
public class HelloServiceGrpcClient implements HelloServiceClient {

	private Logger logger = LoggerFactory.getLogger(HelloServiceGrpcClient.class);
	
	@Autowired
	private GrpcClientProperties clientProperties;
	
	private final List<ClientInterceptor> clientInterceptorList;
	
	public HelloServiceGrpcClient(List<ClientInterceptor> clientInterceptorList) {
		this.clientInterceptorList = clientInterceptorList;
	} 
	
	/* (non-Javadoc)
	 * @see sample.HelloServiceClient#sayHello(java.lang.String)
	 */
	@Override
	public String sayHello(String name) throws Exception {
	
		logger.debug("In grpc client.");
		HelloServiceFutureStub futureStub = HelloServiceGrpc.newFutureStub(getManagedChannel());
		
		ListenableFuture<HelloReply> future = futureStub.sayHello(HelloRequest.newBuilder().setName(name).build());
		
		HelloReply reply = future.get(2000, TimeUnit.MILLISECONDS);
		return reply.getMessage();
		
	}


	private ManagedChannel getManagedChannel() {
        return ManagedChannelBuilder
        		.forAddress(clientProperties.getHost(), clientProperties.getPort())
                .usePlaintext()
                .intercept(clientInterceptorList)
                .build();
	}
}
