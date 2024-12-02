package org.example.lab8.domain.validators;

import org.example.lab8.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message message) throws ValidationException{
        if(message.getFrom() == null || message.getTo() == null || message.getData() == null){
            throw new ValidationException("Mesaj invalid");
        }
    }
}
