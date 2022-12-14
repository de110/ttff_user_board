package com.example.user.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.user.domain.ChatMessage;
import com.example.user.domain.ChatRoom;
import com.example.user.domain.User;
import com.example.user.repository.ChatRepository;
import com.example.user.repository.MessageRepository;
import com.example.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    // 채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        // 채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = chatRepository.findAll();
        log.info("result: {}", result);
        return result;
    }

    // 채팅방 하나 불러오기
    public List<ChatRoom> findByRoomId(Long roomId) {
        return chatRepository.findByRoomId(roomId);
    }

    @Transactional
    public ChatRoom save(String inviteUrl) {
        ChatRoom chatRoom = chatRepository.findById(1L).get();
        return chatRepository.save(chatRoom);
    }

    @Transactional
    public ChatRoom create(ChatRoom chatRoom, String username) {
        validateDuplicateChatRoom(chatRoom);
        User user = userRepository.findByUsername(username).get();
        chatRoom.setUser(user);
        chatRepository.save(chatRoom); // room 정보 저장
        return chatRoom;
    }

    public void validateDuplicateChatRoom(ChatRoom chatRoom) {
        Boolean findChatRoom = chatRepository.existsByRoomName(chatRoom.getRoomName());
        log.info("find: {}", findChatRoom);
        if (findChatRoom == true) {
            throw new IllegalStateException("exist");
        }
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        // ChatMessage chatMessage = new ChatMessage();
        // chatRooms.put(chatRoom.getRoomId(), chatMessage);
        String username = chatMessage.getSender().getUsername();
        User user = userRepository.findByUsername(username).get();
        chatMessage.setSender(user);
        // chatMessage.setRoomId(chatMessage.getRoomId());
        log.info("msg: {}", chatMessage);
        return messageRepository.save(chatMessage);
    } /// 수정 필요

    public List<ChatMessage> findAllMsgs() {
        List<ChatMessage> result = messageRepository.findAll();
        return result;
    }

    @Transactional
    public void deleteChatRoom(Long roomId) {
        chatRepository.deleteByRoomId(roomId);
    }

}