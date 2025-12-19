public class Setting {
	private static int row = 8; // 局面の行数
	private static int col = 5; // 局面の列数
	private String[] grids = new String [row * col];
	String[] name = {"king","general","pawn1","pawn2","Archer","kight"};
	int[] mySet;

	Setting(int[] mySet){
		for(int i = 0 ; i < 5 * col ; i++){ // 局面の初期化
			grids[i] = "attack,Board";
		}
		for(int i = 5 * col; i < row * col; i++) {
			grids[i] = "board";
		}
		grids[mySet[0] + 25]="my,King";
		grids[mySet[1] + 25]="my,General";
		grids[mySet[2] + 25]="my,Pawn1";
		grids[mySet[3] + 25]="my,Pawn2";
		grids[mySet[4] + 25]="my,Archer";
		grids[mySet[5] + 25]="my,Knight";

		this.mySet = mySet;
	}

	// 移動する
	public void move(int now, int next) {
		if(grids[next].equals("move,Board")) {
			grids[next] = grids[now];
			grids[now] = "board";
		}else {
			String[] strList; // 分割後のメッセージ保存
			String token = ","; // 分割記号

			strList = grids[next].split(token);

			grids[next] = grids[now];
			grids[now] = "my," + strList[1];
		}

		Reset();
	}

	// 移動表示を出す
	public void moveAble(String name) {
		for(int i = 0 ; i < row * col ; i++){
			if(grids[i].equals("my,King") && !name.equals("my,King")) {
				grids[i] = "a,King";
			}else if(grids[i].equals("my,General") && !name.equals("my,General")) {
				grids[i] = "a,General";
			}else if(grids[i].equals("my,Pawn1") && !name.equals("my,Pawn1")) {
				grids[i] = "a,Pawn1";
			}else if(grids[i].equals("my,Pawn2") && !name.equals("my,Pawn2")) {
				grids[i] = "a,Pawn2";
			}else if(grids[i].equals("my,Archer") && !name.equals("my,Archer")) {
				grids[i] = "a,Archer";
			}else if(grids[i].equals("my,Knight") && !name.equals("my,Knight")) {
				grids[i] = "a,Knight";
			}else if(grids[i].equals("board")) {
				grids[i] = "move,Board";
			}
		}
	}

	// 移動表示を消す
	public void Reset() {
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
			}else if(grids[i].equals("move,Board")) {
				grids[i] = "board";
			}
		}
	}

	public String[] getGrids(){
		return grids;
	}

	public int[] getMySet() {
		Reset();

		for(int i = 0 ; i < row * col ; i++){
			if(grids[i].equals("my,King")) {
				mySet[0] = i - 25;
			}else if(grids[i].equals("my,General")) {
				mySet[1] = i - 25;
			}else if(grids[i].equals("my,Pawn1")) {
				mySet[2] = i - 25;
			}else if(grids[i].equals("my,Pawn2")) {
				mySet[3] = i - 25;
			}else if(grids[i].equals("my,Archer")) {
				mySet[4] = i - 25;
			}else if(grids[i].equals("my,Knight")) {
				mySet[5] = i - 25;
			}
		}

		return mySet;
	}
}
