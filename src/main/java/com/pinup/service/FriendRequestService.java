package com.pinup.service;

import com.pinup.PinupApplication;
import com.pinup.dto.response.FriendRequestResponse;
import com.pinup.entity.FriendRequest;
import com.pinup.entity.Member;
import com.pinup.global.enums.FriendRequestStatus;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.kafka.KafkaProduceService;
import com.pinup.repository.FriendRequestRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.enums.FriendRequestStatus.PENDING;
import static com.pinup.global.exception.PinUpException.*;

@RequiredArgsConstructor
@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;
    private final FriendShipService friendShipService;
    private final KafkaProduceService kafkaProduceService;

    @Transactional
    public FriendRequestResponse sendFriendRequest(Long receiverId) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member sender = memberRepository.findByEmail(senderEmail)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> MEMBER_NOT_FOUND);

        validateSelfFriendRequest(sender, receiver);
        validateDuplicateFriendRequest(sender, receiver);

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
        friendRequestRepository.save(friendRequest);

        kafkaProduceService.sendNotification(receiver.getEmail(),
                sender.getName() + "님이 친구 요청을 보냈습니다.");

        return FriendRequestResponse.from(friendRequest);
    }

    private void validateSelfFriendRequest(Member sender, Member receiver) {
        if (sender.getEmail().equals(receiver.getEmail())) {
            throw SELF_FRIEND_REQUEST;
        }
    }

    private void validateDuplicateFriendRequest(Member sender, Member receiver) {
        friendRequestRepository.findBySenderAndReceiverAndFriendRequestStatus(sender, receiver, PENDING)
                .ifPresent(request -> {
                    throw ALREADY_EXIST_FRIEND_REQUEST;
                });
    }

    @Transactional
    public FriendRequestResponse acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> FRIEND_REQUEST_NOT_FOUND);
        validateRequestReceiverIsCurrentUser(friendRequest);
        validateFriendRequestStatus(friendRequest);

        friendRequest.accept();
        friendRequestRepository.save(friendRequest);
        friendShipService.createFriendShip(friendRequest.getSender(), friendRequest.getReceiver());

        kafkaProduceService.sendNotification(friendRequest.getSender().getEmail(),
                friendRequest.getReceiver().getName() + "님이 친구 요청을 수락했습니다.");

        return FriendRequestResponse.from(friendRequest);
    }

    @Transactional
    public FriendRequestResponse rejectFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> FRIEND_REQUEST_NOT_FOUND);
        validateRequestReceiverIsCurrentUser(friendRequest);
        validateFriendRequestStatus(friendRequest);

        friendRequest.reject();
        friendRequestRepository.save(friendRequest);

        kafkaProduceService.sendNotification(friendRequest.getSender().getEmail(),
                friendRequest.getReceiver().getName() + "님이 친구 요청을 거절했습니다.");

        return FriendRequestResponse.from(friendRequest);
    }

    private void validateFriendRequestStatus(FriendRequest friendRequest) {
        if(friendRequest.getFriendRequestStatus() != PENDING){
            throw ALREADY_PROCESSED_FRIEND_REQUEST;
        }
    }

    private void validateRequestReceiverIsCurrentUser(FriendRequest friendRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String friendRequestReceiverEmail = friendRequest.getReceiver().getEmail();

        if (!currentUserEmail.equals(friendRequestReceiverEmail)) {
            throw FRIEND_REQUEST_RECEIVER_MISMATCH;
        }
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getReceivedFriendRequests() {
        String receiverEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member receiver = memberRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> MEMBER_NOT_FOUND);

        return friendRequestRepository.findByReceiver(receiver)
                .stream()
                .filter(request -> request.getFriendRequestStatus() == PENDING)
                .map(FriendRequestResponse::from)
                .collect(Collectors.toList());
    }
}