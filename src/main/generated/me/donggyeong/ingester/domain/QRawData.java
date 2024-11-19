package me.donggyeong.ingester.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRawData is a Querydsl query type for RawData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRawData extends EntityPathBase<RawData> {

    private static final long serialVersionUID = 428670886L;

    public static final QRawData rawData = new QRawData("rawData");

    public final DateTimePath<java.time.ZonedDateTime> createdAt = createDateTime("createdAt", java.time.ZonedDateTime.class);

    public final MapPath<String, Object, SimplePath<Object>> data = this.<String, Object, SimplePath<Object>>createMap("data", String.class, Object.class, SimplePath.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isValid = createBoolean("isValid");

    public QRawData(String variable) {
        super(RawData.class, forVariable(variable));
    }

    public QRawData(Path<? extends RawData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRawData(PathMetadata metadata) {
        super(RawData.class, metadata);
    }

}

