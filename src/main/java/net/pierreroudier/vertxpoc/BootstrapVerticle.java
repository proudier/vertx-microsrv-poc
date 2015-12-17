package net.pierreroudier.vertxpoc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class BootstrapVerticle extends AbstractVerticle {
	public static final String DEFAULT_HOMEPAGE = "/doc/api.html";

	public void start(Future<Void> startFuture) {
		HttpServer server = vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true));
		Router router = Router.router(vertx);

		// Favicon
		router.route().handler(FaviconHandler.create());

		// Static resource handler for embedded documentation
		router.get("/doc/*").handler(StaticHandler.create());

		// Da real business - the API
		router.get("/api/v1/randomGenerator").handler(routingContext -> {
			String responseContent = new String();
			int length = RandomGeneratorService.DEFAULT_LENGTH;
			String lengthAsString = routingContext.request().getParam("length");
			if (lengthAsString != null && lengthAsString.length() > 0) {
				try {
					length = Integer.parseInt(lengthAsString);
				} catch (NumberFormatException e) {
					routingContext.fail(new BadRequestException(e));
					return;
				}
			}
			responseContent = new RandomGeneratorService().foo(length);

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "application/json");
			response.end(responseContent);
		});

		// Redirection to default homepage
		router.get("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.setStatusCode(301).putHeader("Location", DEFAULT_HOMEPAGE);
			response.end();
		});

		// Uniform error handlers
		router.route().failureHandler(routingContext -> {
			if (routingContext.failure() instanceof BadRequestException) {
				HttpServerResponse response = routingContext.response();
				response.putHeader("content-type", "text/plain");
				response.setStatusCode(400).end(routingContext.failure().getMessage());
			} else {
				HttpServerResponse response = routingContext.response();
				response.putHeader("content-type", "text/plain");
				response.setStatusCode(500).end("Sorry something went wrong");
			}
		});

		// Explicitly providing a default handler to provide a uniforme user
		// experience
		router.route().handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/plain");
			response.setStatusCode(404).end("not found, oh my");
		});

		server.requestHandler(router::accept).listen(8080);
		startFuture.complete();
	}

	public void stop() {

	}
}
