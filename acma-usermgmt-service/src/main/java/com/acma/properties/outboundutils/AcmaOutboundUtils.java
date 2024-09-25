package com.acma.properties.outboundutils;

import com.acma.properties.models.Users;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AcmaOutboundUtils {

    public AcmaOutboundUtils()
    {
    }

    public static HttpEntity getHttpEntity(Users user, String accessToken) {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("Authorization", "Bearer "+ accessToken);
        headersMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity requestBody = new HttpEntity<>(user,headersMap);
        return requestBody;
    }
}
