package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AttendanceDAO {
	WorkScheduleCommon common = new WorkScheduleCommon();
	/*
	 * 指定日付のレコードを従業員毎に取得する。
	 * 第一引数：打刻日
	 */
	public String getRecord(String embossData, int employeeId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		String record = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement("SELECT AT_DATE FROM TRN_ATTENDANCE WHERE EMP_ID = ? AND AT_DATE = ?");
			st.setInt(1, employeeId);
			st.setString(2, embossData);
			rs = st.executeQuery();

			if (rs.next()) {
				record = rs.getString("AT_DATE");
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

		return record;
	}

	/*
	 * 出勤時間もしくは退社時間をDBに新規登録する。
	 * 第1引数：打刻日, 第2引数：社員ID, 第3引数：休日区分, 第4引数：出退勤時間, 第5引数：アクション(出勤or退社)
	 */
	public void registerAttendanceData(String embossData, int employeeId, int holidayCategory, int attendanceTime, String action) {
		Connection conn = null;
		PreparedStatement st = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			if ("attendance".equals(action)) {
				st = conn.prepareStatement(
						"INSERT INTO TRN_ATTENDANCE (AT_DATE, EMP_ID, HOLIDAY, ATTENDANCE_TIME, LEAVING_TIME, ABSENCE_CATEGORY, REMARK) VALUES (?, ?, ?, ?, NULL, 0, NULL)");
			} else if ("leave".equals(action)) {
				st = conn.prepareStatement(
						"INSERT INTO TRN_ATTENDANCE (AT_DATE, EMP_ID, HOLIDAY, ATTENDANCE_TIME, LEAVING_TIME, ABSENCE_CATEGORY, REMARK) VALUES (?, ?, ?, NULL, ?, 0, NULL)");
			}

			st.setString(1, embossData);
			st.setInt(2, employeeId);
			st.setInt(3, holidayCategory);
			st.setInt(4, attendanceTime);
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
	 * 「休日区分」、「欠勤区分」、「備考」のいずれかをDBに新規登録する。
	 * 第1引数：登録日, 第2引数：社員ID, 第3引数：休日区分, 第4引数：登録データ, 第5引数：アクション(出勤or退社)
	 */
	public void registerData(String date, int employeeId, int holidayCategory, String data, String action) {
		Connection conn = null;
		PreparedStatement st = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			if ("absence".equals(action)) {
				st = conn.prepareStatement(
						"INSERT INTO TRN_ATTENDANCE (AT_DATE, EMP_ID, HOLIDAY, ABSENCE_CATEGORY) VALUES (?, ?, ?, ?)");
				st.setString(1, date);
				st.setInt(2, employeeId);
				st.setInt(3, holidayCategory);
				st.setInt(4, Integer.parseInt(data));
			} else if ("remark".equals(action)) {
				st = conn.prepareStatement(
						"INSERT INTO TRN_ATTENDANCE (AT_DATE, EMP_ID, HOLIDAY, ABSENCE_CATEGORY, REMARK) VALUES (?, ?, ?, 0, ?)");
				st.setString(1, date);
				st.setInt(2, employeeId);
				st.setInt(3, holidayCategory);
				st.setString(4, data);
			} else if ("holiday".equals(action)) {
				st = conn.prepareStatement(
						"INSERT INTO TRN_ATTENDANCE (AT_DATE, EMP_ID, HOLIDAY, ABSENCE_CATEGORY) VALUES (?, ?, ?, 0)");
				st.setString(1, date);
				st.setInt(2, employeeId);
				st.setInt(3, Integer.parseInt(data));
			}


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
	 * DBの出勤時間もしくは退社時間を更新する。
	 * 第1引数：打刻日, 第2引数：出退勤時間, 第3引数：アクション(出勤or退社)
	 */
	public void updateAttendanceData(String embossDate, int attendanceTime, String action, int employeeId) {
		Connection conn = null;
		PreparedStatement st = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			if ("attendance".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET ATTENDANCE_TIME = ? WHERE EMP_ID = ? AND AT_DATE = ?");
			} else if ("leave".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET LEAVING_TIME = ? WHERE EMP_ID = ? AND AT_DATE = ?");
			}

			st.setInt(1, attendanceTime);
			st.setInt(2, employeeId);
			st.setString(3, embossDate);
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
	 * DBの「出勤」、「退社」「休日区分」、「欠勤区分」、「備考」のいずれかを更新する。
	 * ※「出勤」および「退社」はnullの時のみ。
	 * 第1引数：打刻日, 第2引数：出退勤時間, 第3引数：アクション()
	 */
	public void updateData(String date, String updateData, String action, int employeeId) {
		Connection conn = null;
		PreparedStatement st = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			if ("remark".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET REMARK = ? WHERE EMP_ID = ? AND AT_DATE = ?");
				st.setString(1, updateData);
				st.setInt(2, employeeId);
			} else if ("absence".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET ABSENCE_CATEGORY = ? WHERE EMP_ID = ? AND AT_DATE = ?");
				st.setInt(1, Integer.parseInt(updateData));
				st.setInt(2, employeeId);
			} else if ("holiday".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET HOLIDAY = ? WHERE EMP_ID = ? AND AT_DATE = ?");
				st.setInt(1, Integer.parseInt(updateData));
				st.setInt(2, employeeId);
			} else if ("attendance".equals(action)) {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET ATTENDANCE_TIME = ? WHERE EMP_ID = ? AND AT_DATE = ?");
				st.setNull(1, java.sql.Types.NULL);
				st.setInt(2, employeeId);
			} else {
				st = conn.prepareStatement("UPDATE TRN_ATTENDANCE SET LEAVING_TIME = ? WHERE EMP_ID = ? AND AT_DATE = ?");
				st.setNull(1, java.sql.Types.NULL);
				st.setInt(2, employeeId);
			}

			st.setString(3, date);
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
	 * DBに登録済の該当する年・月の勤怠データを全て取得する。
	 * 第1引数: 年, 第2引数：月, 第3引数：最終日
	 */
	public List<Attendance> getAttendanceData(int year, int month, int day, int employeeId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Attendance> attendanceList = new ArrayList<Attendance>();

		//該当日付の初日と最終日を作成。
		String firstDay = year + "-" + month + "-" + "1";
		String lastDay = year + "-" + month + "-" + day;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement(
					"SELECT " +
							"TO_CHAR(AT_DATE, 'YYYY-MM-DD') AS AT_DATE, " +
							"HOLIDAY, " +
							"CASE LENGTH(CAST(ATTENDANCE_TIME AS VARCHAR(10))) " +
								"WHEN 1 THEN '0:0' || SUBSTR(TO_CHAR(ATTENDANCE_TIME, '0'), 2, 1) " +
								"WHEN 2 THEN '0:' || SUBSTR(TO_CHAR(ATTENDANCE_TIME, '99'), 2, 2) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(ATTENDANCE_TIME, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(ATTENDANCE_TIME, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(ATTENDANCE_TIME, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(ATTENDANCE_TIME, '0999'), 4, 2) " +
							"END AS ATTENDANCE_TIME, " +
							"CASE LENGTH(CAST(LEAVING_TIME AS VARCHAR(10))) " +
								"WHEN 1 THEN '0:0' || SUBSTR(TO_CHAR(LEAVING_TIME, '0'), 2, 1) " +
								"WHEN 2 THEN '0:' || SUBSTR(TO_CHAR(LEAVING_TIME, '99'), 2, 2) " +
								"WHEN 3 THEN SUBSTR(TO_CHAR(LEAVING_TIME, '999'), 2, 1) || ':' || SUBSTR(TO_CHAR(LEAVING_TIME, '999'), 3, 2) " +
								"ELSE SUBSTR(TO_CHAR(LEAVING_TIME, '0999'), 2, 2) || ':' || SUBSTR(TO_CHAR(LEAVING_TIME, '0999'), 4, 2) " +
							"END AS LEAVING_TIME, " +
							"ABSENCE_CATEGORY, " +
							"REMARK " +
					"FROM " +
							"TRN_ATTENDANCE " +
					"WHERE " +
							"EMP_ID = ? AND AT_DATE BETWEEN ? AND ?");

			st.setInt(1, employeeId);
			st.setString(2, firstDay);
			st.setString(3, lastDay);
			rs = st.executeQuery();

			while (rs.next()) {
				Attendance attendance = new Attendance();
				attendance.setDate(rs.getString("AT_DATE"));
				attendance.setHoliday(rs.getInt("HOLIDAY"));
				attendance.setAttendanceTime(rs.getString("ATTENDANCE_TIME"));
				attendance.setLeavingTime(rs.getString("LEAVING_TIME"));
				attendance.setAbsence(rs.getInt("ABSENCE_CATEGORY"));
				attendance.setRemark(rs.getString("REMARK"));
				attendanceList.add(attendance);
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

		return attendanceList;
	}

}
