package com.ComNCheck.ComNCheck.domain.global.config.gcp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("app.oauth2.google")
public class GoogleOauthProperties {

    private List<String> clientIds = new ArrayList<>();
}
