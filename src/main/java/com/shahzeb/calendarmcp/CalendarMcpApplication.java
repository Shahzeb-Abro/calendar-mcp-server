package com.shahzeb.calendarmcp;

import com.shahzeb.calendarmcp.service.CalendarTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CalendarMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarMcpApplication.class, args);
    }

    @Bean
    public List<ToolCallback> calendarToolCallbacks(CalendarTools  calendarTools) {
       return List.of(ToolCallbacks.from(calendarTools));
    }
}
