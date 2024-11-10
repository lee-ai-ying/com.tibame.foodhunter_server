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

@WebServlet("/api/note/deleteNoteById")
public class DeleteNoteByIdController extends HttpServlet {
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
        // 設置編碼
        req.setCharacterEncoding("UTF-8");

        // 解析 JSON 主體
        JsonObject jsonRequest;
        try (BufferedReader reader = req.getReader()) {
            jsonRequest = new Gson().fromJson(reader, JsonObject.class);
        }

        // 從 JSON 中獲取 note_id
        String noteIdStr = jsonRequest.get("note_id").getAsString();

        JsonObject respBody = new JsonObject();
        if (noteIdStr != null && !noteIdStr.trim().isEmpty()) {
            try {
                int noteId = Integer.parseInt(noteIdStr);
                int deleteResult = service.deleteNoteById(noteId);

                if (deleteResult > 0) {
                    respBody.addProperty("result", noteId + "刪除成功");
                } else {
                    respBody.addProperty("error", "查無此筆記，無法刪除");
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
