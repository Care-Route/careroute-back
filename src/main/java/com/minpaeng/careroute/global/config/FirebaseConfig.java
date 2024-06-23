package com.minpaeng.careroute.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.sdk.path}")
    private String firebaseSdkPath;

    @Bean
    public FirebaseMessaging initFirebaseMessaging() throws IOException {
        InputStream file = FirebaseConfig.class.getClassLoader().getResourceAsStream(firebaseSdkPath);
        log.info("됨? " + file);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(file))
                .build();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        file.close();
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
