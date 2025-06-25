package org.blackjack.repositories;

import org.blackjack.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, ObjectId> {

    Mono<Game> findById(ObjectId id);

    Mono<Void> deleteById(ObjectId id);
}