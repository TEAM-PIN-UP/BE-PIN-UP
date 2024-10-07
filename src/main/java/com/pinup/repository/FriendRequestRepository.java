package com.pinup.repository;

import com.pinup.entity.FriendRequest;
import com.pinup.entity.Member;
import com.pinup.global.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findById(Long id);

    List<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);

    List<FriendRequest> findBySender(Member sender);

    List<FriendRequest> findByReceiver(Member receiver);

    Optional<FriendRequest> findBySenderAndReceiverAndFriendRequestStatus(Member sender, Member receiver, FriendRequestStatus status);
}
