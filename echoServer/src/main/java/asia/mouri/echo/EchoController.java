package asia.mouri.echo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class EchoController {
    @GetMapping("/hello")
    public String hello() {
        return "world";
    }

    @PostMapping(value = "echo", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] echo(@RequestBody byte[] req) throws IOException {
        log.debug("Got a request of size {}", req.length);
        return req;
    }
}
