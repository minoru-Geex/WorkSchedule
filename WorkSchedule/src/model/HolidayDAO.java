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

public class HolidayDAO {
	/*
	 * 祝祭日マスターより祝祭日を取得する。
	 */
	public List<Holidays> getHolidays() {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Holidays> holidayList = new ArrayList<Holidays>();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/mi-saito_db");
			conn = ds.getConnection();

			st = conn.prepareStatement("SELECT TO_CHAR(HOL_DATE, 'YYYY-MM-DD') AS HOL_DATE FROM MST_HOLIDAYS");
			rs = st.executeQuery();

			while (rs.next()) {
				Holidays holiday = new Holidays();
				holiday.setHolidayDate(rs.getString("HOL_DATE"));
				holidayList.add(holiday);
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
		return holidayList;
	}
}
