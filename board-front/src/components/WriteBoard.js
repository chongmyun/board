import React, { useState } from 'react';
import { Editor } from 'react-draft-wysiwyg';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import styled from 'styled-components';
import { AtomicBlockUtils, EditorState,convertToRaw } from 'draft-js';
import draftToHtml from 'draftjs-to-html';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const MyBlock = styled.div`
    .wrapper-class{
        margin-bottom: 4rem;
    }
  .editor {
    height: 500px !important;
    border: 1px solid #f1f1f1 !important;
    padding: 5px !important;
    border-radius: 2px !important;
  }
`;



export default function WriteBoard() {
  const [editorState, setEditorState] = useState(EditorState.createEmpty());
  const [title, setTitle] = useState('');
  const [fileList, setFileList] = useState([]);

  const onEditorStateChange = (editorState) => {
    // editorState에 값 설정
    setEditorState(editorState);
  };

  const enterTitle = (e) =>{
    setTitle(e.target.value);
  }

  const handlePastedFiles = (files) => {
    const formData = new FormData();
    formData.append('file',files[0]);

    axios.post('/api/file/upload',formData,{
        headers :{"Content-Type" : "multipart/form-data"}
    }).then(
        res => res.data
    ).then(fileName => {
        if(fileName){
            setEditorState(insertImage(fileName))
        }
    }).catch(err => {
        console.log(err);
    })   
}
const navigate = useNavigate();

const saveBoard = () =>{
  const param = {};
  param.memberId = sessionStorage.getItem("memberId");
  param.title = title;
  param.content = draftToHtml(convertToRaw(editorState.getCurrentContent()));
  param.contentFileList = fileList.join(',');
  axios.post('/api/board/save',param)
  .then(res => navigate('/board/'+res.data.boardId))
  .catch(e => console.log(e));
}

const insertImage = ( fileName) => {
  setFileList(fileList => [...fileList,fileName]);
  const fileUrl = "/api/file/download?fileName="
  const contentState = editorState.getCurrentContent();
  const contentStateWithEntity = contentState.createEntity(
        'IMAGE',
        'IMMUTABLE',
        { src: fileUrl+fileName },)
  const entityKey = contentStateWithEntity.getLastCreatedEntityKey();
  const newEditorState = EditorState.set( editorState, { currentContent: contentStateWithEntity });
  return AtomicBlockUtils.insertAtomicBlock(newEditorState, entityKey, ' ');
};

  return (
    <MyBlock>
      <input id="title" type="text" value={title} onChange={enterTitle}></input>
      <Editor
        // 에디터와 툴바 모두에 적용되는 클래스
        wrapperClassName="wrapper-class"
        // 에디터 주변에 적용된 클래스
        editorClassName="editor"
        // 툴바 주위에 적용된 클래스
        toolbarClassName="toolbar-class"
        // 툴바 설정
        toolbar={{
          // inDropdown: 해당 항목과 관련된 항목을 드롭다운으로 나타낼것인지
          list: { inDropdown: true },
          textAlign: { inDropdown: true },
          link: { inDropdown: true },
          history: { inDropdown: false },
        }} 
        placeholder="내용을 작성해주세요."
        // 한국어 설정
        localization={{
          locale: 'ko',
        }}
        // 초기값 설정
        editorState={editorState}
        // 에디터의 값이 변경될 때마다 onEditorStateChange 호출
        onEditorStateChange={onEditorStateChange}
        handlePastedFiles={handlePastedFiles}
      />
      <button onClick={saveBoard}>저장</button>
    </MyBlock>
  );
}
