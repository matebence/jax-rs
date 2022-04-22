package com.bence.mate.services;

import com.bence.mate.daos.ApplicationDAO;
import com.bence.mate.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageService {

    private Map<Long, Message> messages = ApplicationDAO.MESSAGES;

    public List<Message> getAllMessagesForYear(int year) {
        List<Message> messagesForYear = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getYear() == year) {
                messagesForYear.add(message);
            }
        }
        return messagesForYear;
    }

    public Message addMessage(Message message) {
        message.setId((long) (messages.size() + 1));
        messages.put(message.getId(), message);

        return messages.get(message.getId());
    }

    public Message updateMessage(Message message) {
        if(message.getId() <= 0) return null;
        messages.put(message.getId(), message);

        return messages.get(message.getId());
    }

    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }

    public void removeMessage(Long id){
        messages.remove(id);
    }

    public Message getMessage(long id){
        return messages.get(id);
    }
}
