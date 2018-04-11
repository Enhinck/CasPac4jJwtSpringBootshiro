package com.enhinck.demo.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Document(collection = "t_app")
@Getter
@Setter
public class App
{
    @Id
    private ObjectId id;
    @Field("api_key")
    private String apiKey;
    private String appname;
    private List<Object> activities;
    
}
