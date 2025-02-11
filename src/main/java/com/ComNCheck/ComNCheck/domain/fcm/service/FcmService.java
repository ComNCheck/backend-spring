package com.ComNCheck.ComNCheck.domain.fcm.service;

import com.ComNCheck.ComNCheck.domain.fcm.model.entity.FcmToken;
import com.ComNCheck.ComNCheck.domain.fcm.repository.FcmRepository;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Member;
import com.ComNCheck.ComNCheck.domain.member.repository.MemberRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FcmService {

    private final MemberRepository memberRepository;
    private final FcmRepository fcmRepository;

    public void sendMessageToToken(String token, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
    }
    @Transactional
    public void registerFcmToken(Long memberId, String token) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        FcmToken existing = fcmRepository.findByToken(token).orElse(null);
        if(existing != null) return; // 학습 후 추가 기능 구현해야함

        FcmToken newFcmToken = new FcmToken(token);
        member.addFcmToken(newFcmToken);

        memberRepository.save(member);
    }
}
