package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class CPMSRequestSender {
    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public CPMSRequestSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzMmYyZDFjN2UwNWJiMDUwY2RkYTRjYiIsImlhdCI6MTY2NDcxMTI1OSwiZXhwIjoxNjY0NzExMzQ1fQ.4nI9KCat1GJZaMp1RkRHhn99STBg17RA6EwLknMxDO4");
    }


}
