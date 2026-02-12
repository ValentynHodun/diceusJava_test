package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class N11SearchGatlingSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.n11.com")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.9")
            .acceptLanguageHeader("tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
            .acceptEncodingHeader("gzip, deflate, br")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36");

    ScenarioBuilder searchScenario = scenario("N11 Search Scenario")
            .exec(http("Open Home Page")
                    .get("/")
                    .check(status().is(200)))
            .pause(2)
            .exec(http("Search for iPhone")
                    .get("/arama")
                    .queryParam("q", "iphone")
                    .check(status().is(200))
                    .check(substring("iphone").exists()))
            .pause(3)
            .exec(http("Navigate to Second Page")
                    .get("/arama")
                    .queryParam("q", "iphone")
                    .queryParam("pg", "2")
                    .check(status().is(200)))
            .pause(2);

    {
        setUp(
                searchScenario.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }

}
