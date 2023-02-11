package com.lion.pinepeople.service;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class SmsService {
    @Value("${SMS_KEY}")
    private String api_key;

    @Value("${SMS_SECRET}")
    private String api_secret;
    @Value("${SMS_PHONE}")
    private String from;

    public String randNum() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 4; i++) {
            sb.append(Integer.toString(rand.nextInt(10)));
        }

        return sb.toString();
    }

    public SingleMessageSentResponse sendOne(String to, String randNum) {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret, "https://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[pinepeople] 인증번호: "+ randNum);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}
