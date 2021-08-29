package fakegram.adapter.http.controller.v1;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Controller("/api/v1/test")
@Secured(IS_ANONYMOUS)
public class TestController {

    @Get("/ok")
    public HttpResponse<String> HttpOk() {
        return HttpResponse.ok("Test body for metrics");
    }

    @Get("/redirect")
    public HttpResponse<Void> HttpRedirect() {
        return HttpResponse.status(HttpStatus.PERMANENT_REDIRECT);
    }

    @Get("/bad-request")
    public HttpResponse<Void> HttpBadRequest() {
        return HttpResponse.badRequest();
    }

    @Get("/not-found")
    public HttpResponse<Void> HttpNotFound() {
        return HttpResponse.notFound();
    }

    @Get("/internal")
    public HttpResponse<Void> HttpError() {
        return HttpResponse.serverError();
    }

}
