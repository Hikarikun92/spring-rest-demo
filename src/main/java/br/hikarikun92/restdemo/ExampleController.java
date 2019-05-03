package br.hikarikun92.restdemo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

//All the requests to this web service must come from endpoint "/example". If the application is running in your local
//machine in port 8080, for example, they must be accessed via "http://localhost:8080/example".
@CrossOrigin
@RestController
@RequestMapping("example")
public class ExampleController {

    //No "value" or "path" specified = will be accessible in the root endpoint. For example, "http://localhost:8080/example".
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHello() {
        return "Hello, world!";
    }

    //The path is "json"; this method will be called when the user accesses "http://localhost:8080/example/json".
    @GetMapping(value = "json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getJson() {
        return "{\"property1\": \"value 1\"}";
    }

    //The path is "html"; this method will be called when the user accesses "http://localhost:8080/example/html".
    @GetMapping(value = "html", produces = MediaType.TEXT_HTML_VALUE)
    public String getHtml() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Example HTML page</title>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Hello! This is an HTML page!</h1>\n" +
                "    <p>This page was created via REST</p>\n" +
                "  </body>\n" +
                "</html>";
    }

    //The path is "hello/" followed by a name; this method will be called when the user accesses, for example, "http://localhost:8080/example/hello/Lucas".
    @GetMapping(value = "hello/{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHelloTo(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }

    //The path is "params" followed by a question mark (?) and the parameters defined here; this method will be called
    //when the user accesses, for example, "http://localhost:8080/example/params?name=Lucas&age=27". Both parameters "name"
    //and "age" are required, and not passing them will make Spring return an error page; parameter "optional" can be ignored.
    @GetMapping(value = "params", produces = MediaType.TEXT_PLAIN_VALUE)
    public String methodWithParams(@RequestParam("name") String name, @RequestParam("age") int age, @RequestParam(value = "optional", required = false) String optional) {
        String message = "Hello, " + name + "! You are " + age + " years old.";

        if (optional != null) {
            message += "\nYou provided an optional parameter with value = " + optional;
        }

        return message;
    }
}
