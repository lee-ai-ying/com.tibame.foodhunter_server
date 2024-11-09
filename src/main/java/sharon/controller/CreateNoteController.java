package sharon.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import sharon.service.NoteService;
import sharon.service.impl.NoteServiceImpl;
import sharon.vo.Note;

@WebServlet("/api/note/create")
public class CreateNoteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoteService service;

    @Override
    public void init() throws ServletException {
        try {
            service = new NoteServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("初始化 NoteService 失敗", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
    	// 設置編碼
        req.setCharacterEncoding("UTF-8");
    	
		Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
    	Note note = gson.fromJson(req.getReader(), Note.class);
    	
    	System.out.println("Title: " + note.getTitle());
    	System.out.println("Content: " + note.getContent());
    	System.out.println("Restaurant ID: " + note.getRestaurantId());
    	System.out.println("Selected Date: " + note.getSelectedDate());
    	System.out.println("Member ID: " + note.getMemberId());

        System.out.println("Parsed Note Object: " + note);
        System.out.println("Selected Date: " + note.getSelectedDate()); //檢查 selectedDate
    	
    	String errMsg = service.createNote(note);
    	JsonObject respBody = new JsonObject();
    	
    	respBody.addProperty("result", errMsg == null);  // 沒有errMsg 代表返回成功
    	respBody.addProperty("errMsg", errMsg);
    	
    	// 創建成功返回 noteId
    	if (errMsg == null) {
            respBody.addProperty("noteId", note.getNoteId());  
        }
    	
    	resp.getWriter().write(respBody.toString());

    }
}
