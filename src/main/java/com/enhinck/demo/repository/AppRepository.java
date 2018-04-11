package com.enhinck.demo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.enhinck.demo.entity.App;

@Repository
public interface AppRepository extends MongoRepository<App, ObjectId>
{
    App findOneByApiKey(String apiKey);
}
