package model;

public class InputLogic {
	/*
	 * 「ログイン」の入力チェック
	 *  第1引数：社員ID入力値、 第2引数：パスワード入力値
	 */
	public boolean isLogin(String id, String pass) {

		// ID入力は1～3文字の半角数字のみ
		boolean result1 = isCheckedId(id);

		// パスワードは8文字以上15文字以下で
		//小文字半角英字、大文字半角英字、半角数字がそれぞれ1個以上使われている。
		boolean result2 = isCheckedPass(pass);

		// どちらか一方でも入力チェックがエラーならtrueを返す。
		if (result1 || result2) {
			return true;
		}

		return false;
	}

	/*
	 * 社員登録の「社員ID」入力チェック
	 * 第1引数：社員ID
	 */
	public boolean isCheckedId(String id) {
		// IDは1～3文字の半角数字のみ
		if (!(id.matches("^\\d{1,3}$"))) {
			return true;
		}
		return false;
	}

	/*
	 * 社員登録の「パスワード」入力チェック
	 * 第1引数：ログインパスワード
	 */
	public boolean isCheckedPass(String pass) {
		// パスワードは8文字以上15文字以下で
		//小文字半角英字、大文字半角英字、半角数字がそれぞれ1個以上使われている。
		if (!(pass.matches("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)[a-zA-Z\\d]{8,15}$"))) {
			return true;
		}
		return false;
	}

	/*
	 * 社員登録の「名前」入力チェック
	 * 第1引数：姓or名
	 */
	public boolean isCheckedName(String name) {
		// 名前が未入力(空文字)でない。
		if ("".equals(name)) {
			return true;
		}
		return false;
	}
}
