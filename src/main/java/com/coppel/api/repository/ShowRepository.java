package com.coppel.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.coppel.api.model.CommentDocument;
import com.coppel.api.model.ShowDocument;

public interface ShowRepository extends MongoRepository<ShowDocument,Long> {

}
