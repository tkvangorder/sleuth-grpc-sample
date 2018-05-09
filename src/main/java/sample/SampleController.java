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

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author tyler.vangorder
 *
 */
@RestController
public class SampleController {

	private Logger logger = LoggerFactory.getLogger(SampleController.class);
	
	private final Random random = new Random();
	
	private final HelloServiceClient helloClient;

	public SampleController(HelloServiceClient client) {
		helloClient = client;
	}

	@GetMapping("/hi/{name}")
	public String hi(@PathVariable String name) throws Exception {

		logger.debug("In the controller.");
		Thread.sleep(this.random.nextInt(1000));
		return helloClient.sayHello(name);
	}

}
