package com.api.v1.alumind.scheduler;

import com.api.v1.alumind.dtos.reponses.SemanalMetricsDTO;
import com.api.v1.alumind.services.EmailService;
import com.api.v1.alumind.services.FeedbacksService;
import com.api.v1.alumind.utils.EmailsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class SchuedulerAPI {

    @Value("${stakeholders.emails}")
    private String stakeholdersEmails;

    @Autowired
    private FeedbacksService feedbacksService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailsTemplate emailsTemplate;

    @Scheduled(cron = "0 0 7 * * SAT")
    // @EventListener(ApplicationReadyEvent.class)
    public void executarBuscaPeriodica() {
        List<String> emails = Arrays.asList(stakeholdersEmails.split(";"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dtEnd = LocalDate.now();
        LocalDate dtStart = LocalDate.now().minusDays(7);
        SemanalMetricsDTO data = feedbacksService.semanalMetrics(dtStart.format(formatter),  dtEnd.format(formatter));
        String messageHtml = emailsTemplate.weeklyReportEmailTemplate(data, dtStart.format(formatter), dtEnd.format(formatter));
        emailService.sendEmailWeekReports(emails, "Relatório Semanal - AluMind", messageHtml);
    }
}
