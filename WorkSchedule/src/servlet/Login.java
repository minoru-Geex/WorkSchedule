package servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Attendance;
import model.Employee;
import model.Holidays;
import model.InputLogic;
import model.WorkScheduleLogic;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Calendar cal = Calendar.getInstance();
		WorkScheduleLogic logic = new WorkScheduleLogic();

		// 祝祭日マスターより祝祭日の日付と名前を取得する。
		List<Holidays> holidayList = logic.getHoliday();
		request.setAttribute("holidayList", holidayList);

		// セッションスコープから取得。
		HttpSession session = request.getSession();
		Employee emp = (Employee)session.getAttribute("emp");

		// 社員情報を設定して、セッションスコープにセット。
		logic.setEmpInfo(emp);
		session.setAttribute("emp", emp);

		// 初期設定：現在日付の年・月をコンボボックスに設定する。
		logic.setDate(request, cal);

		// 現在表示している日付の年・月・最終日を取得する。
		int year = Integer.parseInt(request.getAttribute("year").toString());
		int month = Integer.parseInt(request.getAttribute("month").toString());
		int lastDay = logic.setLastDay(cal, year, month);

		// 該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータを取得する。
		List<Attendance> recordList = logic.getRecordList(year, month, lastDay, emp);
		request.setAttribute("recordList", recordList);

		// 該当年・月の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
		Map<String, String> attendanceSumMap = logic.getAttendanceSumMap(recordList);
		request.setAttribute("attendanceSumMap", attendanceSumMap);

		// 勤務表に遷移。
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		Calendar cal = Calendar.getInstance();
		WorkScheduleLogic logic = new WorkScheduleLogic();

		// 祝祭日マスターより祝祭日の日付と名前を取得する。
		List<Holidays> holidayList = logic.getHoliday();
		request.setAttribute("holidayList", holidayList);

		// ログイン画面の入力値を取得する。
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String errorMsg = "社員IDもしくはパスワードが不正です。" +
				"<br>もう一度ログイン情報を入力するか社員情報を新規登録してください。";

		// ログインチェック1：入力チェックを実施し、NGの場合はログイン画面に戻りエラーメッセージを表示する。
		InputLogic inputLogic = new InputLogic();

		if (inputLogic.isLogin(id, pass)) {
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// ログイン時に入力した社員ID及びパスワードとマッチする社員情報を取得する。
		Employee emp = logic.getEmp(Integer.parseInt(id), pass);

		// ログインチェック2: 登録済みのデータがなければログイン画面に戻り、エラーメッセージを表示する。
		if (emp == null) {
			request.setAttribute("errorMsg", errorMsg);
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// 社員情報を設定して、セッションスコープにセット。
		logic.setEmpInfo(emp);
 		HttpSession session = request.getSession();
		session.setAttribute("emp", emp);

		// 初期設定：現在日付の年・月をコンボボックスに設定する。
		logic.setDate(request, cal);

		// 現在表示している日付の年・月・最終日を取得する。
		int year = Integer.parseInt(request.getAttribute("year").toString());
		int month = Integer.parseInt(request.getAttribute("month").toString());
		int lastDay = logic.setLastDay(cal, year, month);

		// 該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータを取得する。
		List<Attendance> recordList = logic.getRecordList(year, month, lastDay, emp);
		request.setAttribute("recordList", recordList);

		// 該当年・月の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
		Map<String, String> attendanceSumMap = logic.getAttendanceSumMap(recordList);
		request.setAttribute("attendanceSumMap", attendanceSumMap);

		// 勤務表に遷移
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
	}

}
