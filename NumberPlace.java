package jp.co.nbe.javabasis;

import java.util.Scanner;

public class NumberPlace {
	private int [] board;

	public NumberPlace() {
		this.board = new int[NumberPlaceCommon.MAX_BOARD];
		for (int i = 0; i < this.board.length; i++) {
			this.board[i] = 0;
		}
	}

	/**
	 * １行分の入力を解析する
	 * @param line 1行分の入力
	 * @return 1行分を解析したintの配列
	 * @throws NumberFormatException
	 */
	private int[] parse_line(String line) throws NumberFormatException {
		int[] result = new int[9];
		int numCnt = 0;
		
		for (int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == ' ' || line.charAt(i) == ',') {
				// 空白またはカンマなら無視する
			} else  {
				// そうでなければ数値に変換する
				result[numCnt] = Integer.parseInt(String.valueOf(line.charAt(i)));
				numCnt++;
			}
		}		
		return result;
	}

	/**
	 * 入力された問題で縦、横、同一ブロックに数字の重複がないか確認する
	 * @return 条件を満たす場合はtrue、満たさない場合はfalse
	 */
	private boolean check_input() {
		boolean result, chek_col, check_row, check_frame;
		int duplex = 0;
		chek_col = true;
		check_row = true;
		check_frame = true;
		
		// 列方向の重複チェック
		for (int num = 1; num <= 9; num++) {
			for (int col = 0; col < 9; col++) {
				for (int i = 0; i < 9; i++) {
					if (board[col + 9 * i] == num) {
						duplex++;
					}
				}
				if (duplex > 1) {
					chek_col = false;
					break;
				}
				duplex = 0;
			}
		}

		// 行方向のチェック
		for (int num = 1; num <= 9; num++) {
			for (int row = 0; row < NumberPlaceCommon.MAX_BOARD; row += 9) {
				for (int i = 0; i < 9; i++) {
					if (board[row + i] == num) {
						duplex++;
					}
				}
				if (duplex > 1) {
					check_row = false;
					break;
				}
				duplex = 0;
			}
		}


		// 行列方向に重複が無い場合だけ、ブロック内の重複をチェックする
		int[] frame_corner = {0, 3, 6, 27, 30, 33, 54, 57, 60};
		for (int num = 1; num <= 9; num++) {
			for (int frame_top : frame_corner) {
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						if (board[frame_top + 9 * i + j] == num) {
							duplex++;
						}
					}
				}
				if (duplex > 1) {
					check_frame = false;
					break;
				}
				duplex = 0;
			}
		}
		
		result = chek_col & check_row & check_frame;
		return result;
	}
	
	/**
	 * 問題を入力する
	 */
	public void input_board() {
		String line;
		Scanner scanner = new Scanner(System.in);

		// 入力が完了するまで繰り返す
		while(true) {
			// 1行分の入力を9回繰り返す
			for (int i = 0; i < 9; i++) {
				System.out.println(String.format("%d行目の入力", i + 1));
				line = scanner.nextLine();
				System.arraycopy(parse_line(line),  0,  board, i * 9, 9);
			}
			
			System.out.println("入力した盤面です");
			print_board();
			// 入力がルールを満たすかチェックする
			if (check_input()) {
				// 満たすなら繰り返しを終了する
				break;
			} else {
				System.out.println("縦、横、または同一ブロックに重複する数字が入力されています。");
			}
		}
		scanner.close();
	}
	
	/**
	 * 盤面を出力する
	 */
	public void print_board() {
		// TODO:出力処理を作成してください
		
		//初行だけアンダーバーを表示
		System.out.println("____________________________");
		
		//1～9行目を繰り返し処理する
		for (int r = 0; r < 9; r++) {
			//最初に左外枠を表示
			System.out.print("｜ ");
			
			//ボードの中身を表示する
			for (int c = 0; c < 9; c++) {
				//ボードの付き字は0～80まであるので、行数(0～8) * 9 + 列の数 で付き字を表現
				System.out.print(String.format("%d\s", board[9 * r + c]));
				
				//3,6行に縦棒を挿入
				if (c == 2|| c == 5) {
					System.out.print("｜ ");
				//9行目に縦棒と改行(println)を挿入
				}else if(c == 8) {
					System.out.println("｜");
				}
			}
			
			//3,6列に区切りを挿入
			if (r == 2||r == 5) {
				System.out.println("＋＝＝＝＝+＝＝＝＝+＝＝＝＝＋");
				
			//	
			}else if(r == 8) {
				System.out.println("￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣");
			}
		}
	}

	/**
	 * 行(ヨコ)方向に重複する数字が無いかチェックする
	 * @param n 調べたい数字
	 * @param x 調べる対象のマスを指定する添字
	 * @return true:重複が無い、fasle:重複がある
	 */
	private boolean row_ok(int n, int x) {
		int row_top = x / 9 * 9;

		// 行方向に調べて重複する数字があればfalseを返す
		for (int i = 0; i < 9; i++) {
			if (board[row_top + i] == n) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 列(タテ)方向に重複する数字が無いかチェックする
	 * @param n 調べたい数字
	 * @param x 調べる対象のマスを指定する添字
	 * @return true:重複が無い、fasle:重複がある
	 */
	private boolean column_ok(int n, int x) {
		int column_top = x % 9;

		// 列方向に調べて重複する数字があればfalseを返す
		for (int i = 0; i < 9; i++) {
			if (board[column_top + 9 * i] == n) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 3 x 3のブロック内に重複する数字が無いかチェックする
	 * @param n 調べたい数字
	 * @param x 調べる対象のマスを指定する添字
	 * @return true:重複が無い、fasle:重複がある
	 */
	private boolean frame_ok(int n, int x) {
		int frame_top = x - ((x / 9 * 9) % 27) - x % 3;
		
		// ブロックを調べて重複する数字があればfalseを返す
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				if (board[frame_top + 9 * i + j] == n) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 行、列、ブロック内すべてに重複する数字が無いかチェックする
	 * @param n 調べたい数字
	 * @param x 調べる対象のマスを指定する添字
	 * @return true:重複が無い、fasle:重複がある
	 */
	private boolean check_ok(int n, int x) {
		return (row_ok(n, x) && column_ok(n, x) && frame_ok(n, x));
	}

	/**
	 * ナンバープレイスを解く処理の本体
	 * @param x 数字を入れる場所
	 * @return true:解き終わった、false:処理の途中
	 */
	public boolean solve(int x) {
		// TODO:ナンバープレイスを解く処理を作成してください
		if(x == 80) {
			return true;
		}
		else {
		//盤面のマスが0かどうか
			
			//盤面が0でない
			if(board[x] != 0) {
				if(solve(x + 1)) {
					return true;
				}else {
					return false;
				}
				
			//盤面が0である	
			}else {
				
				//数字を1～9まで代入を試みる
				for (int n = 1; n <= 9; n++) {
					
					//数字のチェックが通過できたら代入
					if(check_ok(n,x) == true) {
						board[x] = n;
						if(solve(x + 1)) {
							return true;
							
						} else {
							board[x] = 0;
						}
						
					}
					
				}return false;
				
				
			}
			
			
		}
		
	}

	/**
	 * メイン処理
	 * @param args
	 */
	public static void main(String[] args) {
		NumberPlace np = new NumberPlace();
		
		try {
			// 問題の入力
			np.input_board();
			// 初期状態の出力
			System.out.println("次の盤面を解きます");
			np.print_board();
			
			// ナンバープレイスを解く
			np.solve(0);
		} catch (Exception e) {
			System.out.println("エラーが発生しました。もう一度やり直してください。");
		}
		// 結果を出力する
		System.out.println("答え");
		np.print_board();
		System.out.println("終了");
	}


}
