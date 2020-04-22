package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class EmpDAO {
	/*
	 * DBから指定された社員番号の「社員名」、「所属部署名」、「就業時間」、「普通残業時刻」、
	 * 「休憩時刻1」、「休憩時刻2」、「休憩時刻3」、「早出分前」を取得。
	 * 時刻については、NUMBER型のデータを取り出して、「00：00」の形に整形してモデルにセットする。
	 * 第1引数：社員番号
	 */
	public void setInfo(Employee emp) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement(
					"SELECT " +
							"a.EMP_NAME, " +
							"b.DEP_NAME, " +
							"CASE LENGTH(CAST(c.WORK_TIME_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.WORK_TIME_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.WORK_TIME_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.WORK_TIME_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.WORK_TIME_FROM, '0999'), 4, 2) " +
							"END AS WORK_TIME_FROM, " +
							"CASE LENGTH(CAST(c.WORK_TIME_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.WORK_TIME_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.WORK_TIME_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.WORK_TIME_TO, '9999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.WORK_TIME_TO, '9999'), 4, 2) " +
							"END AS WORK_TIME_TO, " +
							"CASE LENGTH(CAST(c.NORMAL_OVERTIME_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_FROM, '0999'), 4, 2) " +
							"END AS NORMAL_OVERTIME_FROM, " +
							"CASE LENGTH(CAST(c.NORMAL_OVERTIME_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_TO, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.NORMAL_OVERTIME_TO, '0999'), 4, 2) " +
							"END AS NORMAL_OVERTIME_TO, " +
							"CASE LENGTH(CAST(c.LATE_OVERTIME_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.LATE_OVERTIME_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.LATE_OVERTIME_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.LATE_OVERTIME_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.LATE_OVERTIME_FROM, '0999'), 4, 2) " +
							"END AS LATE_OVERTIME_FROM, " +
							"CASE LENGTH(CAST(c.LATE_OVERTIME_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.LATE_OVERTIME_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.LATE_OVERTIME_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.LATE_OVERTIME_TO, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.LATE_OVERTIME_TO, '0999'), 4, 2) " +
							"END AS LATE_OVERTIME_TO, " +
							"CASE LENGTH(CAST(c.LUNCH_BREAK_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.LUNCH_BREAK_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.LUNCH_BREAK_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.LUNCH_BREAK_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.LUNCH_BREAK_FROM, '0999'), 4, 2) " +
							"END AS LUNCH_BREAK_FROM, " +
							"CASE LENGTH(CAST(c.LUNCH_BREAK_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.LUNCH_BREAK_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.LUNCH_BREAK_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.LUNCH_BREAK_TO, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.LUNCH_BREAK_TO, '0999'), 4, 2) " +
							"END AS LUNCH_BREAK_TO, " +
							"CASE LENGTH(CAST(c.EVENING_BREAK_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.EVENING_BREAK_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.EVENING_BREAK_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.EVENING_BREAK_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.EVENING_BREAK_FROM, '0999'), 4, 2) " +
							"END AS EVENING_BREAK_FROM, " +
							"CASE LENGTH(CAST(c.EVENING_BREAK_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.EVENING_BREAK_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.EVENING_BREAK_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.EVENING_BREAK_TO, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.EVENING_BREAK_TO, '0999'), 4, 2) " +
							"END AS EVENING_BREAK_TO, " +
							"CASE LENGTH(CAST(c.NIGHT_BREAK_FROM AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.NIGHT_BREAK_FROM, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.NIGHT_BREAK_FROM, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.NIGHT_BREAK_FROM, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.NIGHT_BREAK_FROM, '0999'), 4, 2) " +
							"END AS NIGHT_BREAK_FROM, " +
							"CASE LENGTH(CAST(c.NIGHT_BREAK_TO AS VARCHAR(10))) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(c.NIGHT_BREAK_TO, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(c.NIGHT_BREAK_TO, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(c.NIGHT_BREAK_TO, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(c.NIGHT_BREAK_TO, '0999'), 4, 2) " +
							"END AS NIGHT_BREAK_TO, " +
							"c.EARLY_APPEARANCE " +
					"FROM " +
							"MST_EMPLOYEES a JOIN MST_DEPARTMENTS b ON a.DEP_ID = b.DEP_ID JOIN MST_EMP_INFO c ON a.EMP_ID = c.EMP_ID " +
					"WHERE " +
							"a.EMP_ID = ?");

			st.setInt(1, emp.getId());
			rs = st.executeQuery();

			if (rs.next()) {
				emp.setName(rs.getString("EMP_NAME"));
				emp.setDepartment(rs.getString("DEP_NAME"));
				emp.setWorkTimeFrom(rs.getString("WORK_TIME_FROM"));
				emp.setWorkTimeTo(rs.getString("WORK_TIME_TO"));
				emp.setNormalOverTimeFrom(rs.getString("NORMAL_OVERTIME_FROM"));
				emp.setNormalOverTimeTo(rs.getString("NORMAL_OVERTIME_TO"));
				emp.setLateOverTimeFrom(rs.getString("LATE_OVERTIME_FROM"));
				emp.setLateOverTimeTo(rs.getString("LATE_OVERTIME_TO"));
				emp.setLunchBreakFrom(rs.getString("LUNCH_BREAK_FROM"));
				emp.setLunchBreakTo(rs.getString("LUNCH_BREAK_TO"));
				emp.setEveningBreakFrom(rs.getString("EVENING_BREAK_FROM"));
				emp.setEveningBreakTo(rs.getString("EVENING_BREAK_TO"));
				emp.setNightBreakFrom(rs.getString("NIGHT_BREAK_FROM"));
				emp.setNightBreakTo(rs.getString("NIGHT_BREAK_TO"));
				emp.setEarlyAppearance(rs.getInt("EARLY_APPEARANCE"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (st != null) {
					st.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 登録フォームに入力した情報を基にDB登録を行う。
	 * 第1引数：RegisterEmployeeオブジェクト
	 */
	public void register(RegisterEmployee registerEmp) {
		Connection conn = null;
		PreparedStatement st = null;

		String name = registerEmp.getLastName() + " " + registerEmp.getFirstName();

		int department = 1;

		switch (registerEmp.getDepartment()) {
			case "システム開発部":
				department = 1;
				break;
			case "総務部":
				department = 2;
				break;
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();


			st = conn.prepareStatement("INSERT INTO MST_EMPLOYEES(EMP_ID, EMP_NAME, EMP_PASS, DEP_ID) VALUES (?, ?, ?, ?)");

			st.setString(1, registerEmp.getId());
			st.setString(2, name);
			st.setString(3, registerEmp.getPass());
			st.setInt(4, department);
			st.executeUpdate();

			// commit & ロールバックする？

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (st != null) {
					st.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * ログイン画面で入力した社員ID及びパスワードと一致する社員のidとパスワードを取得する。
	 */
	public Employee getEmployee(int id, String pass) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		Employee emp = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement("SELECT EMP_ID, EMP_PASS FROM MST_EMPLOYEES WHERE EMP_ID = ? AND EMP_PASS = ?");
			st.setInt(1, id);
			st.setString(2, pass);
			rs = st.executeQuery();

			if (rs.next()) {
				emp = new Employee();
				emp.setId(rs.getInt("EMP_ID"));
				emp.setPass(rs.getString("EMP_PASS"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (st != null) {
					st.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return emp;
	}
}
