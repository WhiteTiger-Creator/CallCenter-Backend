package org.comcom.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class NtfyService {
    private final RestTemplate restTemplate;

    @Autowired
    public NtfyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String topic, String message) {
        String url = "https://ntfy.sh/" + topic;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> request = new HttpEntity<>(message, headers);
        restTemplate.postForObject(url, request, String.class);
    }

}
