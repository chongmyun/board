import '../css/board.css'
function Comment({comment}){
    return (
    <div className="comment" style={{marginLeft:"2%"}}>
        <div style={{display:"flex"}}><span>{comment.content}</span><span>작성자: {comment.userId}</span></div>
        <div className="child-comments">
          {comment.child.map(childComment => (
            <Comment key={childComment.commentId} comment={childComment} />
          ))}
        </div>
      </div>
    );
}



export default function CommentArea({comments}){
    console.log(comments);
    return (
      <>
        <div></div>
        <div className="comment-section">
          {comments.map(comment => (
            <Comment key={comment.commentId} comment={comment} />
          ))}
        </div>
      </>
    )

}