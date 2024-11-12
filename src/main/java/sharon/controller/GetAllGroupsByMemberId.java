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

import sharon.service.GroupService;
import sharon.service.impl.GroupServiceImpl;
import sharon.vo.Group;

@WebServlet("/api/group/getAllGroups")
public class GetAllGroupsByMemberId extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GroupService service;
    
    @Override
    public void init() throws ServletException {
        try {
            service = new GroupServiceImpl();
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
        
        String memberIdStr = jsonRequest.get("member_id").getAsString();
        
        if (memberIdStr != null && !memberIdStr.trim().isEmpty()) {
            try {
                int memberId = Integer.parseInt(memberIdStr);
                
                // 取得該會員的所有群組
                List<Group> groups = service.getAllGroupsByMemberId(memberId);
                
                JsonObject respBody = new JsonObject();
                JsonArray groupArray = new JsonArray();
                if (groups != null && !groups.isEmpty()) {
                    // 將每個群組資料加入陣列
                    for (Group group : groups) {
                        JsonObject groupObj = new JsonObject();
                        groupObj.addProperty("group_id", group.getGroupId());
                        groupObj.addProperty("name", group.getGroupName());
                        groupObj.addProperty("restaurant_name", group.getRestaurantName());
                        groupObj.addProperty("address", group.getRestaurantAddress());
                        groupObj.addProperty("is_public", group.getIsPublic());
                        if (group.getGroupDate() != null) {
                            groupObj.addProperty("time", group.getGroupDate().toString());
                        }
                        groupObj.addProperty("member_id", group.getMemberId());
                        groupArray.add(groupObj);
                    }
                    respBody.add("groups", groupArray);
                    respBody.addProperty("total", groups.size());
                } else {
                    // 查無群組
                    respBody.addProperty("message", "查無群組資料");
                    respBody.add("groups", groupArray);
                    respBody.addProperty("total", 0);
                }
                
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(respBody.toString());
                System.out.println("Response: " + respBody);
                
            } catch (NumberFormatException e) {
                JsonObject errorResp = new JsonObject();
                errorResp.addProperty("error", "會員 ID 格式錯誤");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(errorResp.toString());
            }
        } else {
            JsonObject errorResp = new JsonObject();
            errorResp.addProperty("error", "請提供會員 ID");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(errorResp.toString());
        }
    }
}