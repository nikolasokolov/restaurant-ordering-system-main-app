package com.graduation.mainapp.service;

public interface EmailService {
    void sendInvoiceViaMail(String to, String subject, String body, byte[] pdfReport, String fileName);
}
