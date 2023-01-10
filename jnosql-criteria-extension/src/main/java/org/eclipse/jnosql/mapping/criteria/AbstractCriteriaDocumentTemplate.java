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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.mapping.criteria;

import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.jnosql.mapping.criteria.api.CriteriaQueryResult;
import org.eclipse.jnosql.mapping.criteria.api.EntityQuery;
import org.eclipse.jnosql.mapping.criteria.api.ExecutableQuery;
import org.eclipse.jnosql.mapping.criteria.api.ExpressionQuery;
import org.eclipse.jnosql.mapping.criteria.api.SelectQuery;
import org.eclipse.jnosql.mapping.document.AbstractDocumentTemplate;
import org.eclipse.jnosql.mapping.criteria.api.CriteriaTemplate;

/**
 * This class provides a skeletal implementation of the {@link CriteriaTemplate}
 * interface, to minimize the effort required to implement this interface.
 */
public abstract class AbstractCriteriaDocumentTemplate extends AbstractDocumentTemplate implements CriteriaTemplate {

    @Override
    public <T, R extends CriteriaQueryResult<T>, Q extends ExecutableQuery<T, R, Q, F>, F> R executeQuery(ExecutableQuery<T, R, Q, F> criteriaQuery) {
        requireNonNull(criteriaQuery, "query is required");
        if (criteriaQuery instanceof SelectQuery) {
            SelectQuery<T, ?, ?, ?> selectQuery = SelectQuery.class.cast(criteriaQuery);
            DocumentQuery documentQuery = CriteriaQueryUtils.convert(selectQuery);
            getPersistManager().firePreQuery(documentQuery);
            Stream<DocumentEntity> entityStream = getManager().select(
                    documentQuery
            );

            if (selectQuery instanceof EntityQuery) {
                EntityQuery.class.cast(selectQuery).feed(
                        entityStream.map(
                                documentEntity -> getConverter().toEntity(
                                        documentEntity
                                )
                        )
                );
            } else if (selectQuery instanceof ExpressionQuery) {
                ExpressionQuery.class.cast(selectQuery).feed(
                        entityStream.map(
                                documentEntity -> documentEntity.getDocuments().stream().map(
                                        document -> document.getValue()
                                ).collect(
                                        Collectors.toList()
                                )
                        )
                );
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return criteriaQuery.getResult();
    }

}
