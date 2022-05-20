/*
 *  Copyright (c) 2022 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */

package org.eclipse.jnosql.mapping.mongodb;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface MongoDBTemplate extends DocumentTemplate {

    /**
     * Removes all documents from the collection that match the given query filter.
     * If no documents match, the collection is not modified.
     *
     * @param collectionName the collection name
     * @param filter         the delete filter
     * @return the number of documents deleted.
     * @throws NullPointerException when filter or collectionName is null
     */
    long delete(String collectionName, Bson filter);

    /**
     *  Removes all documents from the collection that match the given query filter.
     *  If no documents match, the collection is not modified.
     * @param entity the entity to take the collection name
     * @param filter the delete filter
     * @param <T> the entity type
     * @return the number of documents deleted.
     * @throws NullPointerException when there is any null parameter
     */
    <T> long delete(Class<T> entity, Bson filter);


    /**
     * Finds all documents in the collection.
     *
     * @param collectionName the collection name
     * @param filter         the query filter
     * @param <T> the entity type
     * @return the stream result
     * @throws NullPointerException when filter or collectionName is null
     */
    <T> Stream<T> select(String collectionName, Bson filter);

    /**
     * Finds all documents in the collection.
     *
     * @param entity the collection name
     * @param filter         the query filter
     * @param <T> the entity type
     * @return the stream result
     * @throws NullPointerException when filter or collectionName is null
     */
    <T> Stream<T> select(Class<T> entity, Bson filter);

    /**
     * Aggregates documents according to the specified aggregation pipeline.
     *
     * @param collectionName the collection name
     * @param pipeline the aggregation pipeline
     * @return the number of documents deleted.
     * @throws NullPointerException when filter or collectionName is null
     */
    Stream<Map<String, BsonValue>> aggregate(String collectionName, List<Bson> pipeline);
}
