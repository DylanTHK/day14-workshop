package com.workshop14.addressBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workshop14.addressBook.model.Contact;
import com.workshop14.addressBook.service.ContactsRedis;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@Controller
public class AddressBookController {
    @Autowired
    private ContactsRedis ctcRedisSvc;

    @GetMapping(path="/")
    public String contactForm(Model model){
        model.addAttribute("contact", new Contact());
        System.out.println("REDISTHOST: " + System.getenv("REDISTHOST"));
        System.out.println("ALL " + System.getenv());

        return "contact";
    }

    @PostMapping("/contact")
    public String saveContact(@Valid Contact contact, BindingResult result, 
                Model model, HttpServletResponse response){
        if(result.hasErrors()){
            return "contact";
        }
        ctcRedisSvc.save(contact);
        model.addAttribute( "contact", contact);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "showContact";
    }

    @GetMapping("/contact")
    public String getAllContact(Model model, @RequestParam(name="startIndex") 
            Integer startIndex){
        List<Contact> result = ctcRedisSvc.findAll(startIndex);
        model.addAttribute("contacts", result);
        return "listContact";
    }

    @GetMapping(path="/contact/{contactId}")
    public String getContactInfoById(Model model, @PathVariable(value="contactId") String contactId){
        Contact ctc = ctcRedisSvc.findById(contactId);
        ctc.setId(contactId);
        model.addAttribute("contact", ctc);
        return "showContact";
    }

}

