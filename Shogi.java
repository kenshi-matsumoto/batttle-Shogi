public class Shogi {
	private Piece piece; // 駒オブジェクト
	private static int row = 8; // 局面の行数
	private static int col = 5; // 局面の列数
	private String turn; // 先手後手情報
	private String[] grids = new String [row * col]; // 局面情報
	private boolean[] attackFlag = new boolean[6]; // 攻撃したかを判定する

	// コンストラクタ
	Shogi(int[] mySet,int[]eSet, Piece piece) { // 自分の初期配置, 相手の初期配置
		turn = "first";

		this.piece = piece;

		for(int i = 0 ; i < row * col ; i++){ // 局面の初期化
			grids[i] = "board";
		}

		grids[mySet[0] + 25]="my,King";
		grids[mySet[1] + 25]="my,General";
		grids[mySet[2] + 25]="my,Pawn1";
		grids[mySet[3] + 25]="my,Pawn2";
		grids[mySet[4] + 25]="my,Archer";
		grids[mySet[5] + 25]="my,Knight";
		grids[Math.abs(eSet[0] - 14)]="e,King";
		grids[Math.abs(eSet[1] - 14)]="e,General";
		grids[Math.abs(eSet[2] - 14)]="e,Pawn1";
		grids[Math.abs(eSet[3] - 14)]="e,Pawn2";
		grids[Math.abs(eSet[4] - 14)]="e,Archer";
		grids[Math.abs(eSet[5] - 14)]="e,Knight";
	}

	// 局面の取得
	public String [] getGrids(){
		return grids;
	}

	// 局面の判定
	public boolean Check(int now,int next) {
		if(0 > next || next >= 40) {
			return false;
		}

		now = now % 5;
		next = next % 5;

		if(Math.abs(now - next) >= 3) {
			return false;
		}

		return true;
	}

	// 移動範囲を消す
	public void ResetMoveable() {
		for(int i = 0 ; i < row * col ; i++){
			if(grids[i].equals("move,Board")) {
				grids[i] = "board";
			}
		}
	}

	// 移動範囲を局面に反映
	public void MoveRange(int now) {
		ResetMoveable();

		int next;

		for(int i = 0; i < piece.getMoveRange(grids[now]).length; i++){
			next = now + piece.getMoveRange(grids[now])[i];
			if(Check(now, next)) {
				if(grids[next].equals("board")) {
					grids[next] = "move,Board";
				}
			}
		}
	}

	// 移動
	public void Move(int now,int next) {
		grids[next] = grids[now];
		grids[now] = "board";

		ResetMoveable();
	}

	// 攻撃範囲を消す
	public void ResetAttackable() {
		for(int i = 0 ; i < row * col ; i++){
			if(grids[i].equals("t,King")) {
				grids[i] = "e,King";
			}else if(grids[i].equals("t,General")) {
				grids[i] = "e,General";
			}else if(grids[i].equals("t,Pawn1")) {
				grids[i] = "e,Pawn1";
			}else if(grids[i].equals("t,Pawn2")) {
				grids[i] = "e,Pawn2";
			}else if(grids[i].equals("t,Archer")) {
				grids[i] = "e,Archer";
			}else if(grids[i].equals("t,Knight")) {
				grids[i] = "e,Knight";
			}else if(grids[i].equals("attack,Board")) {
				grids[i] = "board";
			}
		}
	}

	//攻撃範囲を局面に反映
	public boolean AttackRange(int now) {
		ResetAttackable();

		boolean attackFlag = false;
		int target;

		for(int i = 0; i < piece.getAttackRange(grids[now]).length; i++){
			target = now + piece.getAttackRange(grids[now])[i];
			if(Check(now, target)) {
				if(grids[target].equals("e,King")) {
					grids[target] = "t,King";
					attackFlag = true;
				}else if(grids[target].equals("e,General")) {
					grids[target] = "t,General";
					attackFlag = true;
				}else if(grids[target].equals("e,Pawn1")) {
					grids[target] = "t,Pawn1";
					attackFlag = true;
				}else if(grids[target].equals("e,Pawn2")) {
					grids[target] = "t,Pawn2";
					attackFlag = true;
				}else if(grids[target].equals("e,Archer")) {
					grids[target] = "t,Archer";
					attackFlag = true;
				}else if(grids[target].equals("e,Knight")) {
					grids[target] = "t,Knight";
					attackFlag = true;
				}else if(grids[target].equals("board")) {
					grids[target] = "attack,Board";
				}
			}
		}

		return attackFlag;
	}

	// 攻撃
	public void Attack(int attacker,int target) {
		if(grids[attacker].equals("a,King") || grids[attacker].equals("e,King")) {  // 大将の範囲攻撃の場合
			String[] kingTarget = new String[piece.getAttackRange("a,King").length];
			int[] tempTarget = new int[piece.getAttackRange("a,King").length];

			if(grids[attacker].equals("a,King")) {
				for(int i = 0; i < piece.getAttackRange("a,King").length; i++) {
					tempTarget[i] = attacker + piece.getAttackRange("a,King")[i];

					if(Check(attacker, tempTarget[i])){
						if(grids[tempTarget[i]].equals("attack,Board")){
							kingTarget[i] = "null";
						}else {
							kingTarget[i] = grids[tempTarget[i]];
						}
					}else{
						kingTarget[i] = "null";
					}
				}
			}else {
				for(int i = 0; i < piece.getAttackRange("a,King").length; i++) {
					tempTarget[i] = attacker + piece.getAttackRange("a,King")[i] + 10;

					if(Check(attacker, tempTarget[i])){
						if(grids[tempTarget[i]].equals("attack,Board")){
							kingTarget[i] = "null";
						}else {
							kingTarget[i] = grids[tempTarget[i]];
						}
					}else{
						kingTarget[i] = "null";
					}
				}
			}

			boolean[] deadflag = new boolean[piece.getAttackRange("a,King").length];

			deadflag = piece.kingDamage(kingTarget);

			for(int i = 0; i < piece.getAttackRange("a,King").length; i++) {
				if(deadflag[i]) {
					lostPiece(tempTarget[i]);
				}
			}
		}else { // 大将以外の攻撃
			if(piece.damage(grids[attacker], grids[target])) {
				lostPiece(target);
			}
		}

		String[] strList; // 分割後のメッセージ保存
		String token = ","; // 分割記号
		String mypiece; // 自分の駒

		strList = grids[attacker].split(token);

		if(strList[0].equals("a")) {
			mypiece = "my" + "," + strList[1];
			grids[attacker] = mypiece;
		}

		ResetAttackable();
	}

	// 攻撃可能性初期化
	public void trueAttackFlag() {
		for(int i = 0; i < 6; i++) {
			attackFlag[i] = true;
		}
	}

	// 攻撃済みにする
	public void falseAttackFlag(String name) {
		if(name.equals("my,King")) {
			attackFlag[0] = false;
		}else if(name.equals("my,General")) {
			attackFlag[1] = false;
		}else if(name.equals("my,Pawn1")) {
			attackFlag[2] = false;
		}else if(name.equals("my,Pawn2")) {
			attackFlag[3] = false;
		}else if(name.equals("my,Archer")) {
			attackFlag[4] = false;
		}else if(name.equals("my,Knight")) {
			attackFlag[5] = false;
		}
	}

	// 攻撃可能か確認する
	public boolean checkAttackFlag(String name) {
		boolean flag = false;

		if(name.equals("my,King")) {
			flag = attackFlag[0];
		}else if(name.equals("my,General")) {
			flag = attackFlag[1];
		}else if(name.equals("my,Pawn1")) {
			flag = attackFlag[2];
		}else if(name.equals("my,Pawn2")) {
			flag = attackFlag[3];
		}else if(name.equals("my,Archer")) {
			flag = attackFlag[4];
		}else if(name.equals("my,Knight")) {
			flag = attackFlag[5];
		}

		return flag;
	}

	// 攻撃できる駒の表示を消す
		public void ResetAttackPiece() {
			for(int i = 0 ; i < row * col ; i++){
				if(grids[i].equals("a,King")) {
					grids[i] = "my,King";
				}else if(grids[i].equals("a,General")) {
					grids[i] = "my,General";
				}else if(grids[i].equals("a,Pawn1")) {
					grids[i] = "my,Pawn1";
				}else if(grids[i].equals("a,Pawn2")) {
					grids[i] = "my,Pawn2";
				}else if(grids[i].equals("a,Archer")) {
					grids[i] = "my,Archer";
				}else if(grids[i].equals("a,Knight")) {
					grids[i] = "my,Knight";
				}
			}
		}

	// 攻撃できる駒の数の取得
	public int countAttacker() {
		int countAttacker = 0;

		ResetAttackPiece();

		if(checkAttacker("my,King")) {
			countAttacker++;
		}
		if(checkAttacker("my,General")) {
			countAttacker++;
		}
		if(checkAttacker("my,Pawn1")) {
			countAttacker++;
		}
		if(checkAttacker("my,Pawn2")) {
			countAttacker++;
		}
		if(checkAttacker("my,Archer")) {
			countAttacker++;
		}
		if(checkAttacker("my,Knight")) {
			countAttacker++;
		}

		return countAttacker;
	}

	// 攻撃できる駒の判別
	public boolean checkAttacker(String name) {
		int place = 0;
		boolean flag = false;

		for(int i = 0; i < row * col; i++) {
			if(grids[i].equals(name)) {
				place = i;
				flag = true;
			}
		}

		if(flag && AttackRange(place) && checkAttackFlag(name)) {
			String[] strList; // 分割後のメッセージ保存
			String token = ","; // 分割記号
			String apiece; // 攻撃させる駒

			strList = grids[place].split(token);
			apiece = "a" + "," + strList[1];

			grids[place] = apiece;

			ResetAttackable();

			return true;
		}


		ResetAttackable();

		return false;
	}

	// HPが0になった駒の削除
	public void lostPiece(int place) {
		grids[place] = "board";
	}

	// 勝敗の判断
	public String checkWinner() {
		String result="";
		if(!CheckFinish()) {
			result = "not finished";
		}
		else if(piece.getHP(0) <= 0) {
			result = "You Lose";
		}
		else if(piece.getHP(6) <= 0){
			result = "You Win";
		}
		return result;
	}

	// 対局終了を判断
	public boolean CheckFinish() {
		if(piece.getHP(0) <= 0 || piece.getHP(6) <= 0) {
			return true;
		}
		return false;
	}

	// 手番情報を取得
	public String getTurn(){
		return turn;
	}

	// 手番を変更
	public void changeTurn(){
		if(turn.equals("first")) {
			turn = "second";
		}else {
			turn = "first";
		}
	}
}
