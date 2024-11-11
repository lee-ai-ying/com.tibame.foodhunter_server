package sharon.controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import sharon.service.NoteService;
import sharon.service.impl.NoteServiceImpl;
import sharon.vo.Note;

@WebServlet("/api/note/getNoteById")
public class GetNoteByIdController extends HttpServlet {
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
        
     // 從 JSON 中獲取 note_id
        String noteIdStr = jsonRequest.get("note_id").getAsString();
        
        if (noteIdStr != null && !noteIdStr.trim().isEmpty()) {
            try {
                int noteId = Integer.parseInt(noteIdStr);
                Note note = service.getNoteById(noteId);
                
                JsonObject respBody = new JsonObject();
                if (note != null) {
                    // 找到筆記，回傳筆記資料
                    respBody.addProperty("note_id", note.getNoteId());
                    respBody.addProperty("title", note.getTitle());
                    respBody.addProperty("content", note.getContent());
                    respBody.addProperty("restaurant_id", note.getRestaurantId());
                    respBody.addProperty("member_id", note.getMemberId());
                    if (note.getSelectedDate() != null) {
                        respBody.addProperty("selected_date", note.getSelectedDate().toString());
                    }
                } else {
                    // 查無筆記
                    respBody.addProperty("NotFind", "查無此筆記");
                }
                
                // 設置回應
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(respBody.toString());
                System.out.println("Response: " + respBody);
                
            } catch (NumberFormatException e) {
                // note_id 格式錯誤
                JsonObject errorResp = new JsonObject();
                errorResp.addProperty("error", "筆記編號格式錯誤");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(errorResp.toString());
            }
        } else {
            // 未提供 note_id
            JsonObject errorResp = new JsonObject();
            errorResp.addProperty("error", "請提供筆記編號");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(errorResp.toString());
        }
    }
}