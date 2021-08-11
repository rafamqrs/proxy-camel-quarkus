// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestOperationResponseMsgDefinition;
import org.apache.camel.model.rest.RestBindingMode;

public class ProxyRoute extends RouteBuilder {
  @Override
  public void configure() throws Exception {
    restConfiguration()
    .component("servlet")
    .bindingMode(RestBindingMode.json)
    .dataFormatProperty("prettyPrint", "true")
    .enableCORS(true)
    .apiContextPath("/api-doc")
    .apiProperty("api.title", "User API")
    .apiProperty("api.version", "1.0.0");

    rest("/api").description("Proxy")
    .consumes("application/json")
    .produces("application/json")
    
    .get("/invoices")
    .description("GET Invoices")
    .responseMessage().code(200).message("All invoices successfully returned")
    .endResponseMessage()
    .responseMessage().code(503).message("Server error").endResponseMessage()
    .to("direct:get-invoices");


      from("direct:get-invoices")
        .routeId("invoices")
        .to("rest:get:/boletos?host={{env:SERVICE_URL}}&throwExceptionOnFailure=false")
        .convertBodyTo(String.class)
        .log("${body}");

  }
}