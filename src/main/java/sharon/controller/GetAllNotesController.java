package sharon.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import sharon.service.NoteService;
import sharon.service.impl.NoteServiceImpl;
import sharon.vo.Note;

@WebServlet("/api/note/getAllNotes")
public class GetAllNotesController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoteService service;
    
    @Override
    public void init() throws ServletException {
        try {
            service = new NoteServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        
        JsonObject jsonRequest;
        try (BufferedReader reader = req.getReader()) {
            jsonRequest = new Gson().fromJson(reader, JsonObject.class);
        }
        
        // 從 JSON 中獲取 member_id
        String memberIdStr = jsonRequest.get("member_id").getAsString();
        
        if (memberIdStr != null && !memberIdStr.trim().isEmpty()) {
            try {
                int memberId = Integer.parseInt(memberIdStr);
                
                // 根據 member_id 取得該會員的所有筆記
                List<Note> notes = service.getAllNotes(memberId);
                
                JsonObject respBody = new JsonObject();
                JsonArray noteArray = new JsonArray();
                if (notes != null && !notes.isEmpty()) {
                    // 將每筆筆記資料加入陣列
                    for (Note note : notes) {
                        JsonObject noteObj = new JsonObject();
                        noteObj.addProperty("note_id", note.getNoteId());
                        noteObj.addProperty("title", note.getTitle());
                        noteObj.addProperty("content", note.getContent());
                        noteObj.addProperty("restaurant_id", note.getRestaurantId());
                        noteObj.addProperty("member_id", note.getMemberId());
                        if (note.getSelectedDate() != null) {
                            noteObj.addProperty("selected_date", note.getSelectedDate().toString());
                        }
                        noteArray.add(noteObj);
                    }
                    respBody.add("notes", noteArray);
                    respBody.addProperty("total", notes.size());
                } else {
                    // 查無筆記
                    respBody.addProperty("message", "目前沒有任何筆記");
                    respBody.add("notes", noteArray);
                    respBody.addProperty("total", 0);
                }
                
                // 設置回應
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(respBody.toString());
                System.out.println("Response: " + respBody);
                
            } catch (NumberFormatException e) {
                // member_id 格式錯誤
                JsonObject errorResp = new JsonObject();
                errorResp.addProperty("error", "會員 ID 格式錯誤");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(errorResp.toString());
            }
        } else {
            // 未提供 member_id
            JsonObject errorResp = new JsonObject();
            errorResp.addProperty("error", "請提供會員 ID");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(errorResp.toString());
        }
    }
}
