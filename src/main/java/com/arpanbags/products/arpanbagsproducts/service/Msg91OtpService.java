package com.arpanbags.products.arpanbagsproducts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Msg91OtpService {

    @Value("${msg91.authkey}")
    private String authKey;

    @Value("${msg91.sender}")
    private String sender;

    @Value("${msg91.otp_template_id}")
    private String otpTemplateId;

    @Value("${msg91.flow_id}")
    private String flowId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendOtp(String mobileNumber, String otp) {
        String formattedNumber = "+91" + mobileNumber;
        String url = "https://control.msg91.com/api/v5/otp";

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("template_id", otpTemplateId);
        bodyMap.put("mobile", formattedNumber);
        bodyMap.put("authkey", authKey);
        bodyMap.put("otp", otp);

        try {
            String jsonBody = objectMapper.writeValueAsString(bodyMap);

            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    assert response.body() != null;
                    throw new RuntimeException("Failed to send OTP via MSG91: " + response.body().string());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while sending OTP", e);
        }
    }

    public void sendSms(String mobileNumber, String message) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://control.msg91.com/api/v5/flow/";

        Map<String, Object> body = new HashMap<>();
        body.put("authkey", authKey); // your MSG91 auth key
        body.put("flow_id", flowId);  // your MSG91 flow/template ID
        body.put("mobiles", "+91" + mobileNumber);
        body.put("VAR1", message);    // Assuming your template has VAR1

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(body);

            RequestBody requestBody = RequestBody.create(
                    json,
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    assert response.body() != null;
                    throw new RuntimeException("Failed to send SMS: " + response.body().string());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error sending SMS via MSG91", e);
        }
    }

    public void sendPasswordResetConfirmation(String mobileNumber, String fullName) {
        String message = String.format(
                "Hi %s, your password has been successfully reset. If you did not do this, contact support.",
                fullName
        );

        // MSG91 API call here
        sendSms(mobileNumber, message);
    }

}
