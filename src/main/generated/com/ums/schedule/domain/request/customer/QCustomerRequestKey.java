package com.ums.schedule.domain.request.customer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerRequestKey is a Querydsl query type for CustomerRequestKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCustomerRequestKey extends BeanPath<CustomerRequestKey> {

    private static final long serialVersionUID = 1600712356L;

    public static final QCustomerRequestKey customerRequestKey = new QCustomerRequestKey("customerRequestKey");

    public final StringPath customerId = createString("customerId");

    public final StringPath customerRequestId = createString("customerRequestId");

    public QCustomerRequestKey(String variable) {
        super(CustomerRequestKey.class, forVariable(variable));
    }

    public QCustomerRequestKey(Path<? extends CustomerRequestKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerRequestKey(PathMetadata metadata) {
        super(CustomerRequestKey.class, metadata);
    }

}

