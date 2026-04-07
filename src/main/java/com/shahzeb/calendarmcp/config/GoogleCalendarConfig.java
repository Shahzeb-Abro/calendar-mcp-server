package com.shahzeb.calendarmcp.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class GoogleCalendarConfig {
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR);
    private static final String PROJECT_DIR = "C:/Users/Shahzeb/calendar-mcp";
    private static final String CREDENTIALS_PATH = PROJECT_DIR + "/src/main/resources/credentials.json";
    private static final String TOKENS_DIR = PROJECT_DIR + "/tokens";

    @Bean
    public Calendar googleCalendarService() throws Exception {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();


        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                jsonFactory,
                new FileReader(CREDENTIALS_PATH)
        );


        GoogleAuthorizationCodeFlow flow  = new GoogleAuthorizationCodeFlow.Builder(
                transport, jsonFactory, clientSecrets, SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIR)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Calendar MCP")
                .build();

    }
}
