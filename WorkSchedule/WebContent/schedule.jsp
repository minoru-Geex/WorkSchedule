<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="servlet.CreateWorkSchedule, model.Employee, model.Holidays, model.Attendance, java.util.List,
java.text.SimpleDateFormat, java.util.Calendar, java.util.IllegalFormatException, java.util.Map"

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>勤務表</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="css/schedule.css"/>
<!-- JavaScript -->
<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="js/schedule.js"></script>
<script type="text/javascript" src="js/scheduleCommon.js"></script>
</head>
<body>
<%
	int year = Integer.parseInt(request.getAttribute("year").toString());
	int month = Integer.parseInt(request.getAttribute("month").toString());
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("E");
	List<Holidays> holidayList = (List<Holidays>)request.getAttribute("holidayList");
	List<Attendance> recordList = (List<Attendance>)request.getAttribute("recordList");
	Map<String, String> attendanceSumMap = (Map<String, String>)request.getAttribute("attendanceSumMap");
%>
	<form name="scheduleForm" action="CreateWorkSchedule" method="post">
		<h1 class="center">勤務表</h1>

		<div id="personal">
			<ul>
				<li class="right under">${emp.department }</li>
				<li class="under">${emp.name }</li>
			</ul>
		</div>


		<table class="center">
			<tr>
				<th colspan="2">就業時間</th>
				<th colspan="2">普通残業時刻</th>
				<th colspan="2">深夜残業時刻</th>
				<th colspan="2">休憩時刻1</th>
				<th colspan="2">休憩時刻2</th>
				<th colspan="2">休憩時刻3</th>
				<th rowspan="2">早出分前</th>
			</tr>
			<tr>
				<th>開始</th>
				<th>終了</th>
				<th>開始</th>
				<th>終了</th>
				<th>開始</th>
				<th>終了</th>
				<th>開始</th>
				<th>終了</th>
				<th>開始</th>
				<th>終了</th>
				<th>開始</th>
				<th>終了</th>
			</tr>
			<tr>
				<td id="workFrom">${emp.workTimeFrom }</td>
				<td id="workTo">${emp.workTimeTo }</td>
				<td id="normalOverFrom">${emp.normalOverTimeFrom }</td>
				<td id="normalOverTo">${emp.normalOverTimeTo }</td>
				<td id="lateOverFrom">${emp.lateOverTimeFrom }</td>
				<td id="lateOverTo">${emp.lateOverTimeTo }</td>
				<td id="lunchFrom">${emp.lunchBreakFrom }</td>
				<td id="lunchTo">${emp.lunchBreakTo }</td>
				<td id="eveningFrom">${emp.eveningBreakFrom }</td>
				<td id="eveningTo">${emp.eveningBreakTo }</td>
				<td id="nightFrom">${emp.nightBreakFrom }</td>
				<td id="nightTo">${emp.nightBreakTo }</td>
				<td id="early">${emp.earlyAppearance }</td>
			</tr>
		</table>
		<br />

		<div  class="center date">
		<input type="submit" id="this" name="condition" value="今月">
		<input type="submit" id="last" name="condition" value="前月">
		<select id="year" name="year">
		<%
			for (int i = year-2; i <= year+2; i++) {
		%>
			<option <% if(i == year) { %>selected <% } %>><%=i %></option>
		<%
			}
		%>
		</select>年
		<select id="month" name="month">
		<%
			for (int i = 1; i <= 12; i++) {
		%>
			<option <% if (i == month) { %> selected <% } %>><%=i %></option>
		<%
			}
		%>
		</select>月
		<input type="submit" id="next" name="condition" value="次月"/>
		<input type="button" id="emboss" value="簡単打刻"/>
		<input type="button" id="workConfig" value="就業時間設定">
		<input type="submit" id="end" name="condition" value="ログアウト"/>
		</div>

		<table class="center">
			<tr>
				<th>日</th>
				<th>曜</th>
				<th>休日区分</th>
				<th>出勤</th>
				<th>退社</th>
				<th>就業</th>
				<th>早出残</th>
				<th>普通残</th>
				<th>深夜残</th>
				<th>休出</th>
				<th>遅刻</th>
				<th>早退</th>
				<th>欠勤</th>
				<th>備考</th>
			</tr>
			<%
			if (recordList != null && recordList.size() > 0) {
				for (int i = 1; i <= recordList.size(); i++) {
			%>
			<tr>
				<td><%=i %></td>
			<%
					// 該当日付の曜日を取得
					cal.set(year, month-1, i);
					String dayOfWeek = sdf.format(cal.getTime());
			%>
				<td><%=dayOfWeek %></td>
			    <td>
			   		<select id="holiday<%=i %>" name="holiday">
			   			<option value="0"
			<%
							if (recordList.get(i-1).getHoliday() == 0) {
			%>
					selected
			<%
							}

			%>
			   			></option>
			   			<option value="1"
			<%
			  			 if ("土".equals(dayOfWeek) || "日".equals(dayOfWeek)) {
			%>
					selected
			<%
			 			  }

					for (int h = 0; h < holidayList.size(); h++) {
						String holidayDate = holidayList.get(h).getHolidayDate();

						try {
							String monthDate = String.format("%tm", cal.getTime());
							String dayOfMonthDate = String.format("%td", cal.getTime());

							// 打刻日を取得する。
							String today = year + "-" + monthDate + "-" + dayOfMonthDate;

							if (today.equals(holidayDate)) {
			 %>
					 selected
			 <%
							}
						} catch (IllegalFormatException e) {
							e.printStackTrace();
						}
					}
			 %>
			 <%
						if (recordList.get(i-1).getHoliday() == 1) {
			%>
					selected
			<%
						}
			%>
				  >休日</option>
				  </select>
			    </td>
			<%
				String a = recordList.get(i-1).getAttendanceTime();
				String l = recordList.get(i-1).getLeavingTime();
			%>
				<td><input type="text" id="go<%=i%>"  name="go" size="5" value="<%=a %>"/></td>
				<td><input type="text" id="leave<%=i%>" name="leave" size="5" value="<%=l %>"/></td>
				<td id="workTime<%=i %>"><%=recordList.get(i-1).getWorkTime() %></td>
				<td id="earlyTime<%=i %>"><%=recordList.get(i-1).getEarlyTime() %></td>
				<td id="normalOverTime<%=i %>"><%=recordList.get(i-1).getNormalOverTime() %></td>
				<td id="lateOverTime<%=i %>"><%=recordList.get(i-1).getLateOverTime() %></td>
				<td id="breakTime<%=i %>"><%=recordList.get(i-1).getBreakTime() %></td>
				<td id="lateTime<%=i %>"><%=recordList.get(i-1).getLateTime() %></td>
				<td id="leavingEarlyTime<%=i %>"><%=recordList.get(i-1).getLeavingEarlyTime() %></td>
				<td>
			   		<select id="absence<%=i %>" name="absence">
			   			<option value="0"
			<%
						if (recordList.get(i-1).getAbsence() == 0) {
			%>
							selected
			<%
						}
			%>
			   			></option>
			   			<option value="1"
			<%
						if (recordList.get(i-1).getAbsence() == 1) {
			%>
							selected
			<%
						}
			%>
						>有休</option>
			   			<option value="2"
			<%
						if (recordList.get(i-1).getAbsence() == 2) {
			%>
							selected
			<%
						}
			%>
			   			>特休</option>
			   			<option value="3"
						<%
						if (recordList.get(i-1).getAbsence() == 3) {
			%>
							selected
			<%
						}
			%>
			   			>代休</option>
			   			<option value="4"
						<%
						if (recordList.get(i-1).getAbsence() == 4) {
			%>
							selected
			<%
						}
			%>
			   			>欠勤</option>
			   		</select>
				</td>
				<td><input type="text" id="remark<%=i %>" name="remark" size="25" value="<%=recordList.get(i-1).getRemark() %>"/></td>
			<%

				}
			}
			%>
		    </tr>
		</table>
		<br/>
		<table class="center">
			<tr>
				<th rowspan="2" colspan="2">合 計</th>
				<th>出勤日数</th>
				<th>休出日数</th>
				<th>就業</th>
				<th>早出残</th>
				<th>普通残</th>
				<th>深夜残</th>
				<th>休出</th>
				<th>遅刻</th>
				<th>早退</th>
				<th>有休</th>
				<th>特休</th>
				<th>代休</th>
				<th>欠勤</th>
			</tr>
			<tr>
				<td id="attendanceCount"></td>
				<td id="breakCount"></td>
				<td id="sumWorkTime"><%=attendanceSumMap.get("sumWorkTime") %></td>
				<td id="sumEarlyTime"><%=attendanceSumMap.get("sumEarlyTime") %></td>
				<td id="sumNormalOverTime"><%=attendanceSumMap.get("sumNormalOverTime") %></td>
				<td id="sumLateOverTime"><%=attendanceSumMap.get("sumLateOverTime") %></td>
				<td id="sumBreakTime"><%=attendanceSumMap.get("sumBreakTime") %></td>
				<td id="sumLateTime"><%=attendanceSumMap.get("sumLateTime") %></td>
				<td id="sumLeavingEarlyTime"><%=attendanceSumMap.get("sumLeavingEarlyTime") %></td>
				<td id="closed"></td>
				<td id="special"></td>
				<td id="compensatory"></td>
				<td id="absence"></td>
			</tr>
		</table>


	</form>

</body>
</html>