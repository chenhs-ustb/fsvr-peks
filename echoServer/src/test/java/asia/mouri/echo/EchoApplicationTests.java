package asia.mouri.echo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class EchoApplicationTests {

	@Test
	void contextLoads() {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.basicAuthentication("mouri", "ridowan_pain")
				.build();
		byte[] data = "This is a test data\n and we are \t loving it".getBytes();
		byte[] response = restTemplate.postForObject("http://localhost:8080/echo", data, byte[].class);
		Assertions.assertArrayEquals(data, response);
	}

}
