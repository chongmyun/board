import React from 'react';
import { BrowserRouter,Routes,Route } from 'react-router-dom';
import Login from './components/Login';
import MainBoard from './components/MainBoard'
import BoardDetail from './components/BoardDetail';

function App () {
 
  return (
    <div>
      <BrowserRouter>
				<Routes>
					<Route path="/" element={<Login />}></Route>
					<Route path="/board" element={<MainBoard />}></Route>
          <Route path="/board/:no" element={<BoardDetail />}></Route>
					{/* 상단에 위치하는 라우트들의 규칙을 모두 확인, 일치하는 라우트가 없는경우 처리 */}
				</Routes>
			</BrowserRouter>
    </div>
  )
}
 
export default App;