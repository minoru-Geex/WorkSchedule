package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.EmpDAO;
import model.EmpInfoDAO;
import model.InputLogic;
import model.RegisterEmployee;

/**
 * Servlet implementation class RegisterEmployee
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// セッションスコープの値を取得する。
		HttpSession session = request.getSession();
		RegisterEmployee registerEmp = (RegisterEmployee)session.getAttribute("registerEmp");

		// 押下ボタン名を取得
		String button = request.getParameter("action");

		switch (button) {
			case "戻る":
				// セッションスコープの削除
				session.invalidate();
				request.getRequestDispatcher("registerForm.jsp").forward(request, response);
				break;
			case "登録":
				EmpDAO empDAO = new EmpDAO();
				empDAO.register(registerEmp);
				// セッションスコープの削除
				session.invalidate();
				request.getRequestDispatcher("registerDone.jsp").forward(request, response);
				break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		// 入力項目の取得
		String id = request.getParameter("id");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String department = request.getParameter("departments");
		String password = request.getParameter("pass");
		String msg = null;

		// RegisterEmployeeオブジェクトにセット
		RegisterEmployee registerEmp = new RegisterEmployee(id, lastName, firstName, department, password);

		// 入力チェック
		InputLogic inputLogic = new InputLogic();
		boolean idCheck = inputLogic.isCheckedId(id);
		boolean lastNameCheck = inputLogic.isCheckedName(lastName);
		boolean firstNameCheck = inputLogic.isCheckedName(firstName);
		boolean passCheck = inputLogic.isCheckedPass(password);

		// 入力チェックが不正の時、不正な入力欄を赤くし、上部にエラーメッセージを表示。
		// そして、社員ID、名前に関しては正常な入力値を残す。
		if (idCheck|| lastNameCheck|| firstNameCheck|| passCheck) {
			msg = "値が不正な箇所があります。";

			if (idCheck) {
				request.setAttribute("id", msg);
			}

			if (lastNameCheck) {
				request.setAttribute("lastName", msg);
			}

			if (firstNameCheck) {
				request.setAttribute("firstName", msg);
			}

			if (passCheck) {
				request.setAttribute("pass", msg);
			}

			request.setAttribute("department", department);
			request.setAttribute("msg", msg);
			request.setAttribute("registerEmp", registerEmp);
			request.getRequestDispatcher("registerForm.jsp").forward(request, response);
			return;
		}

		int employeeId = Integer.parseInt(id);

		// 既に同一社員IDが登録されているか確認し、登録されている場合は、処理を中断する。
		EmpInfoDAO empInfoDAO = new EmpInfoDAO();
		if (empInfoDAO.isRegistered(employeeId)) {
			msg = "既に登録されている社員IDです。";
			request.setAttribute("msg", msg);
			request.setAttribute("department", department);
			request.setAttribute("registerEmp", registerEmp);
			request.getRequestDispatcher("registerForm.jsp").forward(request, response);
			return;
		}

		// セッションスコープにセット
		HttpSession session = request.getSession();
		session.setAttribute("registerEmp", registerEmp);

		// 登録確認画面に遷移
		request.getRequestDispatcher("registerConfirm.jsp").forward(request, response);
	}

}
