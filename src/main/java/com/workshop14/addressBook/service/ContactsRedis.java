package com.workshop14.addressBook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.workshop14.addressBook.model.Contact;

@Service
public class ContactsRedis {
    private static final String CONTACT_ENTITY = "contactlist";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void save(final Contact ctc) {
        redisTemplate.opsForList().leftPush(CONTACT_ENTITY, ctc.getId());
        redisTemplate.opsForHash().put(CONTACT_ENTITY + "_MAP", ctc.getId(), ctc);
    }

    public Contact findById(final String contactId) {
        Contact result = (Contact)redisTemplate.opsForHash().get(CONTACT_ENTITY + "_Map", contactId);
        return result;
    }

    public List<Contact> findAll(int startIndex) {
        List<Object> fromContactList = redisTemplate.opsForList().range(CONTACT_ENTITY, startIndex, 10);

        List<Contact> ctcs = redisTemplate.opsForHash()
            .multiGet(CONTACT_ENTITY + "_Map", fromContactList)
            .stream()
            .filter(Contact.class::isInstance)
            .map(Contact.class::cast)
            .toList();
    
        return ctcs;
    }
}
