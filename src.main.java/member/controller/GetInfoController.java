package member.controller;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.Gson;
import member.vo.Member;
@WebServlet("/member/getInfo")
public class GetInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	HttpSession session = req.getSession();
		Member member =(Member) session.getAttribute("member");
		Gson gson = new Gson();
		String jsonStr = gson.toJson(member);
		Writer writer = resp.getWriter();
		writer.write(jsonStr);
		
		
		
	}
}