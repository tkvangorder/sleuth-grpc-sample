package sample;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;
import sample.grpc.HelloReply;
import sample.grpc.HelloRequest;
import sample.grpc.HelloServiceGrpc.HelloServiceImplBase;

/**
 * Implementation of the server-side grpc HelloService.
 * 
 * NOTE: GrpcService is a composite annotation that extends Service, so this class will be picked up in a component scan.
 * 
 * @author tyler.vangorder
 *
 */
@GRpcService
public class HelloGrpcService extends HelloServiceImplBase {

	private Logger logger = LoggerFactory.getLogger(HelloGrpcService.class);

	private final Random random = new Random();

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
		String message = "Hello " + request.getName();
		try {
			Thread.sleep(this.random.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.debug("In the grpc server stub.");
		HelloReply reply = HelloReply.newBuilder()
           .setMessage(message)
           .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
	}
}

