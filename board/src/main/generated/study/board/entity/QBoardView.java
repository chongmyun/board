package study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardView is a Querydsl query type for BoardView
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardView extends EntityPathBase<BoardView> {

    private static final long serialVersionUID = 1262598669L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardView boardView = new QBoardView("boardView");

    public final QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QBoardView(String variable) {
        this(BoardView.class, forVariable(variable), INITS);
    }

    public QBoardView(Path<? extends BoardView> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardView(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardView(PathMetadata metadata, PathInits inits) {
        this(BoardView.class, metadata, inits);
    }

    public QBoardView(Class<? extends BoardView> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

