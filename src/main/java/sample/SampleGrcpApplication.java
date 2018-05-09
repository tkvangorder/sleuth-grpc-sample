/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.ClientInterceptor;
import io.grpc.ServerInterceptor;
import zipkin2.Span;
import zipkin2.reporter.Reporter;

/**
 * 
 * @author tyler.vangorder
 */
@SpringBootApplication
public class SampleGrcpApplication {

	private static final Log logger = LogFactory.getLog(SampleGrcpApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SampleGrcpApplication.class, args);
	}

	@Bean
	public GrpcTracing grpcTracing(Tracing tracing) {
		return GrpcTracing.create(tracing);
	}

	@Bean
	@GRpcGlobalInterceptor
	ServerInterceptor grpcServerSleuthInterceptor(GrpcTracing grpcTracing) {
		//Install the server-side gRpc tracing interceptor provide brave, we rely on sleuth for configuring the tracing object.
		return grpcTracing.newServerInterceptor();
	}
	
	@Bean
	ClientInterceptor grpcClientSleuthInterceptor(GrpcTracing grpcTracing) {
		return grpcTracing.newClientInterceptor();
	}
	
	// Use this for debugging (or if there is no Zipkin server running on port 9411)
	@Bean
	@ConditionalOnProperty(value = "sample.zipkin.enabled", havingValue = "false")
	public Reporter<Span> spanReporter() {
		return new Reporter<Span>() {
			@Override
			public void report(Span span) {
				logger.info(span);
			}
		};
	}

}
