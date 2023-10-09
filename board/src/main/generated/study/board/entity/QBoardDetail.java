package study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardDetail is a Querydsl query type for BoardDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardDetail extends EntityPathBase<BoardDetail> {

    private static final long serialVersionUID = 1657953721L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardDetail boardDetail = new QBoardDetail("boardDetail");

    public final QBoard board;

    public final StringPath detailContent = createString("detailContent");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBoardDetail(String variable) {
        this(BoardDetail.class, forVariable(variable), INITS);
    }

    public QBoardDetail(Path<? extends BoardDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardDetail(PathMetadata metadata, PathInits inits) {
        this(BoardDetail.class, metadata, inits);
    }

    public QBoardDetail(Class<? extends BoardDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

