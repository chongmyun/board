import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
 
function Login() {
    const [inputId, setInputId] = useState('')
    const [inputPw, setInputPw] = useState('')
 
	// input data 의 변화가 있을 때마다 value 값을 변경해서 useState 해준다
    const handleInputId = (e) => {
        setInputId(e.target.value)
    }
 
    const handleInputPw = (e) => {
        setInputPw(e.target.value)
    }
    const params  ={
        userId:inputId,
        password:inputPw
    }

    const navigate = useNavigate();
 
	// login 버튼 클릭 이벤트
    const onClickLogin = () => {
        axios.post("/api/member/login",params,{
            withCredentials:true,
            headers: { "Content-Type": `application/json`}
        }).then(res => {
            
            if(res.status === 200){
                sessionStorage.setItem('userId', res.data.userId)
                sessionStorage.setItem("memberId",res.data.memberId)
                navigate("/board");
            }

            })
        .catch()
    }

 
    return(
        <div>
            <h2>Login</h2>
            <div>
                <label htmlFor='input_id'>ID : </label>
                <input type='text' name='input_id' value={inputId} onChange={handleInputId} />
            </div>
            <div>
                <label htmlFor='input_pw'>PW : </label>
                <input type='password' name='input_pw' value={inputPw} onChange={handleInputPw} />
            </div>
            <div>
                <button type='button' onClick={onClickLogin}>Login</button>
            </div>
        </div>
    )
}
 
export default Login;