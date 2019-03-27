package com.framework.q1.framework.dao;

import utils.DBConnect;
import utils.SessionUtils;
import vo.BoardViewVo;
import vo.BoardVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoardDao {

    DBConnect dbconnect = new DBConnect();

    /**
     * SQLCount 쿼리를 위한 메소드
     */

    public int sqlCount() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        int total = 0;
        try {
            String sqlCount = "SELECT COUNT(*) FROM board";
            pstm = conn.prepareStatement(sqlCount);
            rs = pstm.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return total;
    }

    public int sqlBoardMax() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        int max = 0;
        try {
            String sqlCount = "SELECT MAX(id) FROM board";
            pstm = conn.prepareStatement(sqlCount);
            rs = pstm.executeQuery();

            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return max;
    }

    /**
     * 게시판 글 패스워드를 확인 하기위한 메소드
     */
    public String sqlPasswd(int idx) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        String password = "";
        try {
            String sqlPasswd = "SELECT passwd FROM board WHERE id = ?";
            pstm = conn.prepareStatement(sqlPasswd);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            if (rs.next()) {
                password = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return password;
    }

    /**
     * 게시판 삭제
     * @param idx
     */
    public void sqlDelete(int idx) {
        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlDelete = "DELETE FROM board WHERE id = ?";
            System.out.println(sqlDelete);
            pstm = conn.prepareStatement(sqlDelete);
            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }
    }

    public String sqlTitleSelect(int idx) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        String title = null;

        try {
            String sqlTitle = "SELECT title FROM board WHERE id=?";
            pstm = conn.prepareStatement(sqlTitle);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            if (rs.next()) {
                title = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return title;
    }

    public ArrayList<BoardVo> sqlModifyBoardList(int idx) {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();

        ArrayList<BoardVo> boardList = new ArrayList<BoardVo>();

        try {
            String sqlSelect = "SELECT author, title, content, passwd FROM board WHERE id=?";
            pstm = conn.prepareStatement(sqlSelect);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            if (rs.next()) {
                BoardVo boardVo = new BoardVo();
                boardVo.setAuthor(rs.getString(1));
                boardVo.setTitle(rs.getString(2));
                boardVo.setContent(rs.getString(3));
                boardVo.setPassword(rs.getString(4));

                boardList.add(boardVo);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardList;
    }


    /**
     * 게시판 글 리스트를 읽어드리기 위한 메소드
     */
    public ArrayList<BoardVo> sqlBoardList(HttpServletRequest request, int bc) {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();

        int opt = 0;
        String condition = "";
        if(request.getParameter("opt") != null) {
            opt = Integer.parseInt(request.getParameter("opt"));
        }
        if(request.getParameter("condition") != null) {
            condition = request.getParameter("condition");
        }
        System.out.println("optf : " + opt);
        ArrayList<BoardVo> boardList = new ArrayList<BoardVo>();
        try {
            if (opt == 0) {
                String sqlList = "SELECT id, author, title, todate, indent, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt, (SELECT status FROM board WHERE id = A.id) AS postStatus from board A where boardCode = ? order by parent DESC, step ASC";
                pstm = conn.prepareStatement(sqlList);
                pstm.setInt(1, bc);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(rs.getInt(1));
                    boardVo.setAuthor(rs.getString(2));
                    boardVo.setTitle(rs.getString(3));
                    boardVo.setTodate(rs.getDate(4));
                    boardVo.setIndent(rs.getInt(5));
                    boardVo.setCommentCnt(rs.getInt(6));
                    boardVo.setPostStatus(rs.getString(7));

                    boardList.add(boardVo);
                }
            } else if (opt == 1) {
                String sqlTitle = "SELECT id, author, title, todate, indent, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt, (SELECT status FROM board WHERE id = A.id) AS postStatus from board A where boardCode = ? and title like ? order by parent DESC, step ASC";
                System.out.println(sqlTitle);
                pstm = conn.prepareStatement(sqlTitle);
                pstm.setInt(1, bc);
                pstm.setString(2,"%" + condition + "%");
                System.out.println(condition);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(rs.getInt(1));
                    boardVo.setAuthor(rs.getString(2));
                    boardVo.setTitle(rs.getString(3));
                    boardVo.setTodate(rs.getDate(4));
                    boardVo.setIndent(rs.getInt(5));
                    boardVo.setCommentCnt(rs.getInt(6));
                    boardVo.setPostStatus(rs.getString(7));

                    boardList.add(boardVo);
                }
            } else if (opt == 2) {
                String sqlContent = "SELECT id, author, title, todate, indent, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt, (SELECT status FROM board WHERE id = A.id) AS postStatus from board A where boardCode = ? and content like ? order by parent DESC, step ASC";
                pstm = conn.prepareStatement(sqlContent);
                pstm.setInt(1, bc);
                pstm.setString(2, "%" + condition + "%");
                rs = pstm.executeQuery();
                while (rs.next()) {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(rs.getInt(1));
                    boardVo.setAuthor(rs.getString(2));
                    boardVo.setTitle(rs.getString(3));
                    boardVo.setTodate(rs.getDate(4));
                    boardVo.setIndent(rs.getInt(5));
                    boardVo.setCommentCnt(rs.getInt(6));
                    boardVo.setPostStatus(rs.getString(7));

                    boardList.add(boardVo);
                }
            } else if (opt == 3) {
                String sqlTitleAndContent = "SELECT id, author, title, todate, indent, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt, (SELECT status FROM board WHERE id = A.id) AS postStatus from board A where boardCode = ? and title like ? or content like ? order by parent DESC, step ASC";
                pstm = conn.prepareStatement(sqlTitleAndContent);
                pstm.setInt(1, bc);
                pstm.setString(2, "%" + condition + "%");
                pstm.setString(3, "%" + condition + "%");
                rs = pstm.executeQuery();
                while (rs.next()) {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(rs.getInt(1));
                    boardVo.setAuthor(rs.getString(2));
                    boardVo.setTitle(rs.getString(3));
                    boardVo.setTodate(rs.getDate(4));
                    boardVo.setIndent(rs.getInt(5));
                    boardVo.setCommentCnt(rs.getInt(6));
                    boardVo.setPostStatus(rs.getString(7));
                    boardList.add(boardVo);
                }
            } else if (opt == 4) {
                String sqlAuthor = "SELECT id, author, title, todate, indent, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt, (SELECT status FROM board WHERE id = A.id) AS postStatus from board A where boardCode = ? and author = ? order by parent DESC, step ASC";
                pstm = conn.prepareStatement(sqlAuthor);
                pstm.setInt(1, bc);
                pstm.setString(2, "%" + condition + "%");
                rs = pstm.executeQuery();
                while (rs.next()) {
                    BoardVo boardVo = new BoardVo();
                    boardVo.setId(rs.getInt(1));
                    boardVo.setAuthor(rs.getString(2));
                    boardVo.setTitle(rs.getString(3));
                    boardVo.setTodate(rs.getDate(4));
                    boardVo.setIndent(rs.getInt(5));
                    boardVo.setCommentCnt(rs.getInt(6));
                    boardVo.setPostStatus(rs.getString(7));
                    boardList.add(boardVo);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardList;
    }

    /**
     * 데이터를 불러오기 위한 쿼리
     */
    public ArrayList<BoardVo> sqlGetList(int idx) {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();

        ArrayList<BoardVo> boardGetList = new ArrayList<BoardVo>();

        try {
            String sqlList = "SELECT author, title, passwd, content, todate, indent, parent, step from board where id=?";
            pstm = conn.prepareStatement(sqlList);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            while(rs.next()) {
                BoardVo boardVo = new BoardVo();
                boardVo.setAuthor(rs.getString(1));
                boardVo.setTitle(rs.getString(2));
                boardVo.setPassword(rs.getString(3));
                boardVo.setContent(rs.getString(4));
                boardVo.setTodate(rs.getDate(5));
                boardVo.setIndent(rs.getInt(6));
                boardVo.setParent(rs.getInt(7));
                boardVo.setStep(rs.getInt(8));
                boardGetList.add(boardVo);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardGetList;
    }

    /**
     *  상세 게시판 리스트 쿼리
     * @return
     */
    public ArrayList<BoardVo> sqlBoardDetailList(int idx) {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();

        ArrayList<BoardVo> boardDetailList = new ArrayList<BoardVo>();

        try {
            String sqlList = "SELECT author, title, content, (SELECT COUNT(*) FROM comment WHERE parent = A.id) AS commentCnt from board A where id=?";
            pstm = conn.prepareStatement(sqlList);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            if(rs.next()) {
                BoardVo boardVo = new BoardVo();
                boardVo.setAuthor(rs.getString(1));
                boardVo.setTitle(rs.getString(2));
                boardVo.setContent(rs.getString(3));
                boardVo.setCommentCnt(rs.getInt(4));

                boardDetailList.add(boardVo);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardDetailList;

    }

    /**
     *
     * 게시판 글 제목 및 내용을 수정하기위한 메소드
     */
    public void sqlUpdate(HttpServletRequest request, int idx) {
        BoardVo boardVo = new BoardVo();
        boardVo.setTitle(request.getParameter("title"));
        boardVo.setContent(request.getParameter("content"));
        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlUpdate = "UPDATE board SET title=?, content=? WHERE id=?";
            pstm = conn.prepareStatement(sqlUpdate);
            pstm.setString(1, boardVo.getTitle());
            pstm.setString(2, boardVo.getContent());
            pstm.setInt(3, idx);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }
    }

    /**
     *
     * 게시판 글을 쓰기 위한 메소드
     */
    public void sqlInsert(HttpServletRequest request, HttpServletResponse response, String kindType) throws IOException {
        int insertCnt = 0;
        int parentInsert = sqlBoardMax();

        int id = Integer.parseInt(request.getParameter("id"));
        int pg = Integer.parseInt(request.getParameter("pg"));
        int bc = Integer.parseInt(request.getParameter("bc"));

        BoardVo boardVo = new BoardVo();
        String userId = new SessionUtils().getSession(request, "sessionId");

        if (!userId.equals("vistor")) {
            boardVo.setAuthor(userId);
        } else {
            boardVo.setAuthor(request.getParameter("author"));
        }

        boardVo.setPassword(request.getParameter("password"));
        boardVo.setTitle(request.getParameter("title"));
        boardVo.setContent(request.getParameter("content"));
        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlInsert = "INSERT INTO board(author,passwd,title,content,type,parent,todate,boardCode) VALUES(?,?,?,?,?,?,NOW(),?)";
            pstm = conn.prepareStatement(sqlInsert);
            pstm.setString(1, boardVo.getAuthor());
            pstm.setString(2, boardVo.getPassword());
            pstm.setString(3, boardVo.getTitle());
            pstm.setString(4, boardVo.getContent());
            if (kindType == "post") {
                pstm.setString(5, kindType);
            } else if (kindType == "comment") {
                pstm.setString(5, kindType);
            }
            pstm.setInt(6, parentInsert + 1);
            pstm.setInt(7, bc);
            insertCnt = pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }

        PrintWriter out = response.getWriter();
        if (insertCnt == 1) {
            out.print("<script language=javascript>");
            out.print("self.window.alert(\"입력한 글을 저장하였습니다.\");");
                    out.print("location.href = \"index.jsp?id=" + id + "&pg=" + pg + "&bc=" + bc + "\";");
            out.print("</script>");
            out.close();
        } else {
            out.print("<script language=javascript>");
            out.print(" self.window.alert(\"실패\");");
            out.print("location.href=\"javascript:history.back()\";");
            out.print("</script>");
        }
        out.close();
    }

    public void sqlReplySortUpdate(int parent, int step) {
        Connection conn = dbconnect.connDb();
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            String sqlUpdate = "UPDATE board SET step=? where parent=? and step > ?";
            pstm = conn.prepareStatement(sqlUpdate);
            pstm.setInt(1, step+1);
            pstm.setInt(2, parent);
            pstm.setInt(3, step);
            pstm.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }

    }


    /**
     * 답글 달기
     */
    public void sqlReplyInsert(HttpServletRequest request, HttpServletResponse response, String kindType, int parentInsert, int indentInsert, int stepInsert, int idx) throws IOException {
        int replyCnt = 0;
        int pg = Integer.parseInt(request.getParameter("pg"));

        BoardVo boardVo = new BoardVo();
        String userId = new SessionUtils().getSession(request, "sessionId");

        if (userId != null && userId.equals("vistor")) {
            boardVo.setAuthor(request.getParameter("author"));
        } else {
            boardVo.setAuthor(userId);

        }

        boardVo.setPassword(request.getParameter("password"));
        boardVo.setTitle(request.getParameter("title"));
        boardVo.setContent(request.getParameter("content"));
        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlInsert = "INSERT INTO board(author,passwd,title,content,type,parent,indent,step,todate) VALUES(?,?,?,?,?,?,?,?,NOW())";
            pstm = conn.prepareStatement(sqlInsert);
            pstm.setString(1, boardVo.getAuthor());
            pstm.setString(2, boardVo.getPassword());
            pstm.setString(3, boardVo.getTitle());
            pstm.setString(4, boardVo.getContent());
            if (kindType == "post") {
                pstm.setString(5, kindType);
            } else if (kindType == "comment") {
                pstm.setString(5, kindType);
            }
            pstm.setInt(6, parentInsert);
            pstm.setInt(7, indentInsert + 1);
            pstm.setInt(8, stepInsert + 1);
            replyCnt = pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }

        PrintWriter out = response.getWriter();
        if (replyCnt == 1) {
            out.print("<script language=javascript>");
            out.print("self.window.alert(\"답변을 저장하였습니다.\");");
            out.print("location.href = \"lists.jsp?id=" + idx + "&pg=" + pg + "\";");
            out.print("</script>");
            out.close();
        } else {
            out.print("<script language=javascript>");
            out.print(" self.window.alert(\"실패 했습니다.\");");
            out.print("location.href=\"javascript:history.back()\";");
            out.print("</script>");
        }
        out.close();

    }

    /**
     * 삭제된 Post 값 상태를 변경하기 위한 메소드
     */
    public void sqlPostDelete(int idx) {
        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlUpdate = "UPDATE board SET author='', title='삭제된 글입니다', status='deleted' WHERE id = ?";
            pstm = conn.prepareStatement(sqlUpdate);
            pstm.setInt(1, idx);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }
    }

    /**
     * 삭제된 글을 읽어드리기 위한 메소드
     * @param idx
     * @return
     */
    public String sqlPostStatusSelect(int idx) {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        String statusSelect = "";
        try {
            String sqlSelect = "SELECT status FROM board WHERE id = ?";
            pstm = conn.prepareStatement(sqlSelect);
            pstm.setInt(1, idx);
            rs = pstm.executeQuery();

            if (rs.next()) {
                BoardVo boardVo = new BoardVo();
                boardVo.setStatus(rs.getString(1));
                statusSelect = boardVo.getStatus();
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return statusSelect;
    }

    public void modifyCheckDo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String password = "";
            int idx = Integer.parseInt(request.getParameter("id"));
            int pg = Integer.parseInt(request.getParameter("pg"));
            String pass = request.getParameter("password");
            password = sqlPasswd(idx);
            if (password == null) {
                PrintWriter out = response.getWriter();
                out.print("<script language=javascript>");
                out.print(" self.window.alert(\"자기글이 아닙니다.\");");
                out.print("location.href=\"javascript:history.back()\";");
                out.print("</script>");
                out.close();
            } else if (password.equals(pass)) {
                response.sendRedirect("modify.jsp?id=" + idx + "&pg=" + pg );
            } else {
                PrintWriter out = response.getWriter();
                out.print("<script language=javascript>");
                out.print(" self.window.alert(\"비밀번호를 틀렸습니다.\");");
                out.print("location.href=\"javascript:history.back()\";");
                out.print("</script>");
                out.close();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void addBoardConfigEdit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int addCnt = 0;
        String boardName = request.getParameter("boardName");
        int login = Integer.parseInt(request.getParameter("login"));
        int admin = Integer.parseInt(request.getParameter("admin"));
        int comment = Integer.parseInt(request.getParameter("comment"));
        int reply = Integer.parseInt(request.getParameter("reply"));

        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlInsert = "INSERT INTO boardConfig(boardName,login,admin,comment,reply) VALUES(?,?,?,?,?)";
            pstm = conn.prepareStatement(sqlInsert);
            pstm.setString(1, boardName);
            pstm.setInt(2, login);
            pstm.setInt(3, admin);
            pstm.setInt(4, comment);
            pstm.setInt(5, reply);
            addCnt = pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }

        PrintWriter out = response.getWriter();
        if (addCnt == 1) {
            out.print("<script language=javascript>");
            out.print("self.window.alert(\"게시판이 생성되었습니다..\");");
            out.print("location.href = \"index.jsp\";");
            out.print("</script>");
            out.close();
        } else {
            out.print("<script language=javascript>");
            out.print(" self.window.alert(\"실패\");");
            out.print("location.href=\"javascript:history.back()\";");
            out.print("</script>");
        }
        out.close();
    }

    public void updateBoardConfigEdit(HttpServletRequest request, int boardCode) {
        String boardName = request.getParameter("boardName");
        int login = Integer.parseInt(request.getParameter("login"));
        int admin = Integer.parseInt(request.getParameter("admin"));
        int comment = Integer.parseInt(request.getParameter("comment"));
        int reply = Integer.parseInt(request.getParameter("reply"));

        PreparedStatement pstm = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlUpdate = "UPDATE boardConfig SET boardName=?, login=?, admin=?, comment=?, reply=? where boardCode =?";
            pstm = conn.prepareStatement(sqlUpdate);
            pstm.setString(1, boardName);
            pstm.setInt(2, login);
            pstm.setInt(3, admin);
            pstm.setInt(4, comment);
            pstm.setInt(5, reply);
            pstm.setInt(6, boardCode);
            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
        }
    }

    public int checkBoardConfigCode(HttpServletRequest request){
        String boardName = request.getParameter("boardName");
        int boardCode = 0;

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        try {
            String sqlPasswd = "SELECT boardCode FROM boardConfig WHERE boardName = ?";
            pstm = conn.prepareStatement(sqlPasswd);
            pstm.setString(1, boardName);
            rs = pstm.executeQuery();

            if (rs.next()) {
                boardCode = rs.getInt("1");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardCode;
    }

    public ArrayList<BoardViewVo> boardConfigList() {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();

        ArrayList<BoardViewVo> boardConfigList = new ArrayList<BoardViewVo>();

        try {
            String configList = "SELECT boardCode, boardName, login, admin, comment, reply from boardConfig";
            pstm = conn.prepareStatement(configList);
            rs = pstm.executeQuery();
            while(rs.next()) {
                BoardViewVo boardViewVo = new BoardViewVo();
                boardViewVo.setBoardCode(rs.getInt(1));
                boardViewVo.setBoardName(rs.getString(2));
                boardViewVo.setLogin(rs.getInt(3));
                boardViewVo.setAdmin(rs.getInt(4));
                boardViewVo.setComment(rs.getInt(5));
                boardViewVo.setReply(rs.getInt(6));
                boardConfigList.add(boardViewVo);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return boardConfigList;
    }

    public int boardListCount() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = dbconnect.connDb();
        int total = 0;
        try {
            String sqlCount = "SELECT COUNT(*) FROM boardConfig";
            pstm = conn.prepareStatement(sqlCount);
            rs = pstm.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            dbconnect.close(pstm, conn);
            dbconnect.resultClose(rs);
        }
        return total;
    }
}
