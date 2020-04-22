package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class EmpInfoDAO {
	/*
	 * 就業情報設定画面の入力情報を基にDB登録を行う。
	 * 第1引数：Employeeオブジェクト
	 */
	public void registerWorkConfig(Employee emp) {
		Connection conn = null;
		PreparedStatement st = null;
		WorkScheduleCommon common = new WorkScheduleCommon();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement("INSERT INTO MST_EMP_INFO (" +
					"EMP_ID, " +
					"WORK_PERIOD_FROM, " +
					"WORK_PERIOD_TO, " +
					"WORK_TIME_FROM, " +
					"WORK_TIME_TO, " +
					"NORMAL_OVERTIME_FROM, " +
					"NORMAL_OVERTIME_TO, " +
					"LATE_OVERTIME_FROM, " +
					"LATE_OVERTIME_TO, " +
					"LUNCH_BREAK_FROM, " +
					"LUNCH_BREAK_TO, " +
					"EVENING_BREAK_FROM, " +
					"EVENING_BREAK_TO, " +
					"NIGHT_BREAK_FROM, " +
					"NIGHT_BREAK_TO, " +
					"EARLY_APPEARANCE)" +
					"VALUES (" +
					"?, ?, ?,  ?, ?, ?, ?, 2200, 500, ?, ?, ?, ?, 200, 230, ?)");

			// 引数が時刻の場合、加工してからセットする。
			st.setInt(1, emp.getId());

			if (emp.getWorkPeriodFrom() == null) {
				st.setNull(2, java.sql.Types.NULL);
			} else {
				st.setString(2, emp.getWorkPeriodFrom());
			}

			if (emp.getWorkPeriodTo() == null) {
				st.setNull(3, java.sql.Types.NULL);
			} else {
				st.setString(3, emp.getWorkPeriodTo());
			}

			st.setInt(4, common.changeTimeToNum(emp.getWorkTimeFrom()));
			st.setInt(5, common.changeTimeToNum(emp.getWorkTimeTo()));
			st.setInt(6, common.changeTimeToNum(emp.getNormalOverTimeFrom()));
			st.setInt(7, common.changeTimeToNum(emp.getNormalOverTimeTo()));
			st.setInt(8, common.changeTimeToNum(emp.getLunchBreakFrom()));
			st.setInt(9, common.changeTimeToNum(emp.getLunchBreakTo()));
			st.setInt(10, common.changeTimeToNum(emp.getEveningBreakFrom()));
			st.setInt(11, common.changeTimeToNum(emp.getEveningBreakTo()));
			st.setInt(12, emp.getEarlyAppearance());
			st.executeUpdate();
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
	 * 同一社員IDが既に登録されているかチェック
	 * 第1引数：社員ID
	 */
	public boolean isRegistered(int employeeId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		boolean isRegistered = false;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement("SELECT EMP_ID FROM MST_EMP_INFO WHERE EMP_ID = ?");
			st.setInt(1, employeeId);
			rs = st.executeQuery();

			if (rs.next()) {
				isRegistered = true;
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

		return isRegistered;
	}
}
