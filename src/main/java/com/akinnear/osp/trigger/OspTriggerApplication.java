package com.akinnear.osp.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@SpringBootApplication
public class OspTriggerApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(OspTriggerApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(OspTriggerApplication.class)
				.listeners(
						(ApplicationReadyEvent e) -> handleApplicationReadyEvent(e),
						(EmbeddedServletContainerInitializedEvent e) -> handleEmbeddedServletContainerInitializedEvent(e))
				.run(args);
	}

	private static void handleApplicationReadyEvent(ApplicationReadyEvent e) {
		LOGGER.info("Application is ready to service requests.");
	}

	private static void handleEmbeddedServletContainerInitializedEvent(EmbeddedServletContainerInitializedEvent e) {
		LOGGER.info("Service on port: "+e.getEmbeddedServletContainer().getPort());
	}
}
