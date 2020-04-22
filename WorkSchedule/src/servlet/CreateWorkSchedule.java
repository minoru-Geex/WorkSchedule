package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Attendance;
import model.Employee;
import model.Holidays;
import model.JsonData;
import model.WorkScheduleLogic;


/**
 * Servlet implementation class CreateWorkSchedule
 */
@WebServlet("/CreateWorkSchedule")
public class CreateWorkSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateWorkSchedule() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		Calendar cal = Calendar.getInstance();
		WorkScheduleLogic logic = new WorkScheduleLogic();

		// セッションスコープより、Employeeオブジェクトを取得する。
		HttpSession session = request.getSession();
		Employee emp = (Employee)session.getAttribute("emp");

		// 初期設定：現在日付の年・月をコンボボックスに設定する。
		logic.setDate(request, cal);

		// 祝祭日マスターより祝祭日の日付と名前を取得する。
		List<Holidays> holidayList = logic.getHoliday();
		request.setAttribute("holidayList", holidayList);

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

		// jspに遷移。
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		Calendar cal = Calendar.getInstance();
		WorkScheduleLogic logic = new WorkScheduleLogic();

		List<Attendance> recordList = null;
		Map<String, String> attendanceSumMap = null;

		// 祝祭日マスターより祝祭日の日付と名前を取得する。
		List<Holidays> holidayList = logic.getHoliday();
		request.setAttribute("holidayList", holidayList);

		// セッションスコープより、Employeeオブジェクトを取得する。
		HttpSession session = request.getSession();
		Employee emp = (Employee)session.getAttribute("emp");

		// 初期設定：現在日付の年・月をコンボボックスに設定する。
		logic.setDate(request, cal);

		// 押下ボタンの値を取得する。(「前月」、「今月」、「次月」、「ログアウト」)
		String buttonCondition = request.getParameter("condition");

		// 簡単打刻ウィンドウにて、押下したボタン名を取得(出勤or退勤)。
		String[] embossConditions = request.getParameterValues("embossCondition");

		// 「前月」、「今月」、「次月」、「終了」押下時の処理
		if (buttonCondition != null) {
			// 「ログアウト」押下時は、セッションスコープを破棄してログイン画面に遷移。
			if ("ログアウト".equals(buttonCondition)) {
				session.invalidate();
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			// 押下ボタンによって、コンボボックスに年・月を設定する。
			logic.setCalendarDate(request, cal, buttonCondition);

			// 現在表示している日付の年・月・最終日を取得する。
			int year = Integer.parseInt(request.getAttribute("year").toString());
			int month = Integer.parseInt(request.getAttribute("month").toString());
			int lastDay = logic.setLastDay(cal, year, month);

			// 該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータを取得する。
			recordList = logic.getRecordList(year, month, lastDay, emp);
			request.setAttribute("recordList", recordList);

			// 該当年・月の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
			attendanceSumMap = logic.getAttendanceSumMap(recordList);
			request.setAttribute("attendanceSumMap", attendanceSumMap);

			// jspに遷移。
			request.getRequestDispatcher("schedule.jsp").forward(request, response);

		} else {
			// 登録する年・月・日を取得する。
			int year = Integer.parseInt(request.getParameter("year"));
			int month = Integer.parseInt(request.getParameter("month"));
			int dayOfMonth = Integer.parseInt(request.getParameter("dayOfMonth"));

			// 登録日、休日区分及び該当年・月の最終日を取得する。
			String date = year + "-" + month + "-" + dayOfMonth;
			int holidayCategory = logic.getHolidayCategory(year, month, dayOfMonth, holidayList);
			int lastDay = logic.setLastDay(cal, year, month);

			// 「簡単打刻」処理
			if (embossConditions != null) {

				for (String embossCondition : embossConditions) {

					// 簡単打刻ウィンドウにて、出勤or退勤ボタン押下時
					if ("出勤".equals(embossCondition) || "退社".equals(embossCondition)) {
						logic.embossExecute(request, embossCondition, date, emp, holidayCategory);

						// 該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータを取得する。
						recordList = logic.getRecordList(year, month, lastDay, emp);

						// 該当年・月の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
						attendanceSumMap = logic.getAttendanceSumMap(recordList);
					}
				}
			// 「休日区分」、「出勤」、「退社」、「欠勤区分」、「備考」変更時のajax処理
			} else {
				String action = logic.changeExecute(request, date, emp, holidayCategory);

				// actionが「holiday」、「attendance」、「leave」のとき、勤怠データ及びそれらの合計値を変更する。
				if ("holiday".equals(action) || "attendance".equals(action) || "leave".equals(action)) {
					// 該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータを取得する。
					recordList = logic.getRecordList(year, month, lastDay, emp);

					// 該当年・月の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
					attendanceSumMap = logic.getAttendanceSumMap(recordList);
				}
			}

			// 該当月の日数分のレコード及び合計値がある場合、json形式でajaxにreturnする。
			if (recordList != null && attendanceSumMap != null) {
				JsonData jsonData = new JsonData();

				// jsonDataオブジェクトにAttendanceオブジェクトを設定する。
				jsonData.setAttendance(recordList.get(dayOfMonth - 1));
				// jsonDataオブジェクトに勤怠合計マップを設定する。
				jsonData.setAttendanceMap(attendanceSumMap);

				// jsonDataオブジェクトを → jsonに変換してajaxに送信
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(jsonData);

				response.setContentType("UTF-8");
				PrintWriter out = response.getWriter();
				out.print(json);
			}
		}

	}
}
