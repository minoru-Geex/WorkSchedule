package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.EmpInfoDAO;
import model.Employee;
import model.WorkScheduleLogic;

/**
 * Servlet implementation class WorkConfigRegister
 */
@WebServlet("/WorkConfigRegister")
public class WorkConfigRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkConfigRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 就業時間設定画面より各入力値の取得。
		request.setCharacterEncoding("UTF-8");
		String workPeriodFrom = request.getParameter("workPeriodFrom");						// 就業期間開始
		String workPeriodTo = request.getParameter("workPeriodTo");							// 就業期間終了
		String workTimeFrom = request.getParameter("workTimeFrom");							// 就業開始時刻
		String workTimeTo = request.getParameter("workTimeTo");								// 就業終了時刻
		String normalOverTimeFrom = request.getParameter("normalOverTimeFrom");				// 普通残業開始時刻
		String normalOverTimeTo = request.getParameter("normalOverTimeTo");					// 普通残業終了時刻
		String lunchBreakFrom = request.getParameter("lunchBreakFrom");						// 休憩1開始時刻(昼休憩)
		String lunchBreakTo = request.getParameter("lunchBreakTo");							// 休憩1終了時刻(昼休憩)
		String eveningBreakFrom = request.getParameter("eveningBreakFrom");					// 休憩2開始時刻(業後休憩)
		String eveningBreakTo = request.getParameter("eveningBreakTo");						// 休憩2終了時刻(業後休憩)
		int earlyAppearance = Integer.parseInt(request.getParameter("earlyAppearance"));	// 早出分前

		// セッションスコープにセットされている社員IDを取得。できない場合はログイン画面にリダイレクト。
		HttpSession session = request.getSession();
		Employee emp = (Employee)session.getAttribute("emp");
		if (emp == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		// Employeeオブジェクトに取得した値をセット。
		emp = new Employee(emp.getId(), workPeriodFrom, workPeriodTo, workTimeFrom, workTimeTo, normalOverTimeFrom, normalOverTimeTo, lunchBreakFrom, lunchBreakTo, eveningBreakFrom, eveningBreakTo, earlyAppearance);

		// 既に同一社員IDが登録されているか確認し、登録されている場合は、処理を中断する。
		EmpInfoDAO empInfoDAO = new EmpInfoDAO();
		if (empInfoDAO.isRegistered(emp.getId())) {
			return;
		}

		// DB登録処理。
		empInfoDAO.registerWorkConfig(emp);

		// 登録したデータを社員情報に設定して、セッションスコープにセット。
		WorkScheduleLogic logic = new WorkScheduleLogic();
		logic.setEmpInfo(emp);
		session.setAttribute("emp", emp);

		// Employeeオブジェクトを → jsonに変換してajaxに送信
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(emp);
		response.setContentType("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
	}

}
