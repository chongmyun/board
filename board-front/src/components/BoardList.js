import React, { useState,useEffect } from 'react';
import axios from 'axios';
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TablePagination from "@mui/material/TablePagination";
import Paper from "@mui/material/Paper";
import {Link} from "react-router-dom";



export default function BoardList() {

    const [data, setData] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(20);
    const [totalElements,setTotalElements] = useState(0);

    function handleChangePage(event, newpage) {
      setPage(newpage);
    }

    function handleChangeRowsPerPage(event) {
      setSize(parseInt(event.target.value, 10));
      setPage(0);
    }

    

    useEffect(() => {
        axios.get(`/api/boards?page=${page}&size=${size}`)
        .then(res => {
          setData(res.data.content);
          setPage(res.data.pageable.pageNumber);
          setSize(res.data.pageable.pageSize);
          setTotalElements(res.data.totalElements);
        })
        .catch()
    },
    // 페이지 호출 후 처음 한번만 호출될 수 있도록 [] 추가
    [page,size])
  
    return (
      <Paper>
          <h1 style={{ textAlign: "center", color: "green" }}>
              게시판 목록
          </h1>
          <div style={{ textAlign: "right" }}>
          <Link to={`/board/modify`}><button>글쓰기</button></Link>
          </div>
          <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} 
                    aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="right">No</TableCell>
                            <TableCell align="right">제목</TableCell>
                            <TableCell align="right">작성자</TableCell>
                            <TableCell align="right">작성일</TableCell>
                            <TableCell align="right">조회수</TableCell>
                            <TableCell align="right">댓글수</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((row) => (
                            <TableRow
                                key={row.boardId}
                                sx={{ "&:last-child td,&:last-child th": { border: 0 } }}
                                hover={true}
                                >
                                <TableCell component="th" scope="row">{row.boardId}</TableCell>
                                <TableCell align="right">
                                  <Link to={`/board/${row.boardId}`}>{row.title}</Link>
                                  </TableCell>
                                <TableCell align="right">{row.userId}</TableCell>
                                <TableCell align="right">{row.createdDate}</TableCell>
                                <TableCell align="right">{row.viewCount}</TableCell>
                                <TableCell align="right">{row.commentCount}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[10, 20, 50]}
                component="div"
                count={totalElements}
                rowsPerPage={size}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
      </Paper>
    );
}

