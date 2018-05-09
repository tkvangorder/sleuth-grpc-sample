# Spring Cloud Sleuth GRPC

This project demonstrates how Spring Cloud Sleuth can be combined with gRpc to propagate the distributed tracing calls when using gRpc. This project
uses Spring Cloud Sleuth 2.x and takes advantage of client/server gRpc intercepters that are provided by the underlying brave tracing library.

This project uses [grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter) and this library provides infrastructure to quickly
get gRpc working in SpringBoot.

This project uses maven and can be built via "mvn clean package"

## Running without Zipkin:

1. Edit src/main/resources/application.yml and uncomment the "sample.zipkin.enable=false"
2. Import this maven project into your favorite IDE (note, I have not setup m2e to automatically build the stubs from within Eclipse)
3. Run the spring boot application.
4. In a browser "http://localhost:8080/hi/fred"


## Running with Zipkin:

1. For conveinence, there is a docker-compose file in the root project that can be used to launch zipkin locally. Otherwise, you will need to run zipkin separately.
2. Edit "src/main/resources/application.yml" and comment out the "sample.zipkin.enable=false"
3. Import this maven project into your favorite IDE (note, I have not setup m2e to automatically build the stubs from within Eclipse)
4. Run the spring boot application.
5. In a browser "http://localhost:8080/hi/fred"


## Notes:

- This sample hosts both the gRpc client and server, in a real environment these would be separate.
- The application wires up Brave's client and server tracing interceptors for gRpc and publishes them to the application context.
- The server interceptor is marked with `@GRpcGlobalInterceptor` and will automatically be applied to all beans marked with `@GRpcService`
- The client interceptor is autowired into the client and then used when creating the managed channel.



