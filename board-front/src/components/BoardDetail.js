import { useParams } from 'react-router-dom';
import React, { useState,useEffect  } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import CommentArea from './CommentArea';

export default function BoardDetail() {
    const params = useParams();
    const memberId = sessionStorage.getItem("memberId");

    const [data,setData] = useState({});
    const [comments,setComments] = useState([]);

    useEffect(() => {
        axios.get(`/api/board/${params.no}?memberId=${memberId}`)
        .then(res => {
          setData(res.data);
          setComments(res.data.comments);
        })
        .catch()
    },
    // 페이지 호출 후 처음 한번만 호출될 수 있도록 [] 추가
    [params.no,memberId])

    const navigate = useNavigate();
    const backHandleClick = () => {
        navigate('/board')
    }


    return (
        (
            <>
              <h1 style={{ textAlign: "center", color: "green" }}>
              게시판 상세정보
              </h1>
        
              <div className="post-view-wrapper">
                {
                  data ? (
                    <>
                      <div className="post-view-row">
                        <label>게시글 번호</label>
                        <label>{ data.boardId }</label>
                      </div>
                      <div className="post-view-row">
                        <label>제목 : </label>
                        <label>{ data.title }</label>
                      </div>
                      <div className="post-view-row">
                        <label>작성일: </label>
                        <label>{ data.createdDate }</label>
                      </div>
                      <div className="post-view-row">
                        <label>조회수</label>
                        <label>{ data.viewCount }</label>
                      </div>
                      <div className="post-view-row">
                        <label>내용 </label>
                        <div dangerouslySetInnerHTML={{__html: data.content}}>
                        </div>
                      </div>
                      {comments ? (<CommentArea comments={comments}></CommentArea>) : '댓글이 없습니다.'}
                    </>
                  ) : '해당 게시글을 찾을 수 없습니다.'
                }
                
                <button className="post-view-go-list-btn" onClick={backHandleClick}>목록으로 돌아가기</button>
              </div>
            </>
          )
    );   
}


