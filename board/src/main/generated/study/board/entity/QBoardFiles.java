package study.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardFiles is a Querydsl query type for BoardFiles
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFiles extends EntityPathBase<BoardFiles> {

    private static final long serialVersionUID = 471083023L;

    public static final QBoardFiles boardFiles = new QBoardFiles("boardFiles");

    public final StringPath extension = createString("extension");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath rootPath = createString("rootPath");

    public final StringPath storedFileName = createString("storedFileName");

    public final StringPath storedFilePath = createString("storedFilePath");

    public QBoardFiles(String variable) {
        super(BoardFiles.class, forVariable(variable));
    }

    public QBoardFiles(Path<? extends BoardFiles> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardFiles(PathMetadata metadata) {
        super(BoardFiles.class, metadata);
    }

}

