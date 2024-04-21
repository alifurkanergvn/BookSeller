//package com.afe.bookseller.util;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import com.afe.bookseller.model.Book;
//import com.afe.bookseller.model.User;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
//public class PojoToJsonConverter {
//    @Bean
//    public void makeBookConverter() throws JsonProcessingException {
//        Book book = new Book();
//        book.setTitle("Function");
//        book.setDescription("This is Description");
//        book.setAuthor("Ali Furkan ERGUVEN");
//        book.setPrice(100d);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String bookJson = objectMapper.writeValueAsString(book);
//        System.out.println("Book JSON: "+bookJson);
//    }
//
//    @Bean
//    public void makeUserConverter() throws JsonProcessingException {
//        User user = new User();
//        user.setUsername("alifurkan159");
//        user.setPassword("12345");
//        user.setName("Ali Furkan");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userJson = objectMapper.writeValueAsString(user);
//        System.out.println("User JSON: "+userJson);
//    }
//}
