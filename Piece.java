public class Piece {
	private int[] hitPoint = new int[12];
	private static int kingPower = 40, generalPower = 50, pawnPower = 30, archerPower = 25, knightPower = 45;
	private static int[] kingAttackRange = {-6, -5, -4};
	private static int[] generalAttackRange = {-6, -5, -4};
	private static int[] pawnAttackRange = {-6, -5, -4};
	private static int[] archerAttackRange = {-11, -10, -9, -6, -5, -4};
	private static int[] knightAttackRange = {-6, -5, -4, -1, 1};

	private static int[] kingMoveRange = {-6, -5, -4, -1, 1, 4, 5, 6};
	private static int[] generalMoveRange = {-10, -6, -5, -4, -2, -1, 1, 2, 4, 5, 6, 10};
	private static int[] pawnMoveRange = {-6, -5, -4, 5};
	private static int[] archerMoveRange = {-5, -1, 1, 5};
	private static int[] knightMoveRange = {-12, -8, -6, -5, -4, -1, 1, 4, 5, 6, 8, 12};

	Piece(){
		hitPoint[0] = 300; // kingHP
		hitPoint[1] = 200; // generalHP
		hitPoint[2] = 200; // pawn1HP
		hitPoint[3] = 150; // pawn2HP
		hitPoint[4] = 150; // archerHP
		hitPoint[5] = 125; // knightHP

		hitPoint[6] = 300; // eKingHP
		hitPoint[7] = 200; // eGeneralHP
		hitPoint[8] = 200; // ePawn1HP
		hitPoint[9] = 150; // ePawn2HP
		hitPoint[10] = 150; // eArcherHP
		hitPoint[11] = 125; // eKnightHP
	}

	// HPの取得
	public int getHP(int hitNum) {
		int hp = 0;
		if(hitNum >= 0 && hitNum <= 11) {
			hp = hitPoint[hitNum];
		}else {
			System.out.println("getHPError!");
		}

		return hp;
	}

	// ダメージの計算
	public boolean damage(String attacker,String target) {
		boolean deadflag = false;

		if(target.equals("my,King")) {
			if(hitPoint[0] > getPower(attacker)) {
				hitPoint[0] -= getPower(attacker);
			}else {
				hitPoint[0] = 0;
				deadflag = true;
			}
		}else if(target.equals("my,General")) {
			if(hitPoint[1] > getPower(attacker)) {
				hitPoint[1] -= getPower(attacker);
			}else {
				hitPoint[1] = 0;
				deadflag = true;
			}
		}else if(target.equals("my,Knight")) {
			if(hitPoint[2] > getPower(attacker)) {
				hitPoint[2] -= getPower(attacker);
			}else {
				hitPoint[2] = 0;
				deadflag = true;
			}
		}else if(target.equals("my,Pawn1")) {
			if(hitPoint[3] > getPower(attacker)) {
				hitPoint[3] -= getPower(attacker);
			}else {
				hitPoint[3] = 0;
				deadflag = true;
			}
		}else if(target.equals("my,Pawn2")) {
			if(hitPoint[4] > getPower(attacker)) {
				hitPoint[4] -= getPower(attacker);
			}else {
				hitPoint[4] = 0;
				deadflag = true;
			}
		}else if(target.equals("my,Archer")) {
			if(hitPoint[5] > getPower(attacker)) {
				hitPoint[5] -= getPower(attacker);
			}else {
				hitPoint[5] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,King")) {
			if(hitPoint[6] > getPower(attacker)) {
				hitPoint[6] -= getPower(attacker);
			}else {
				hitPoint[6] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,General")) {
			if(hitPoint[7] > getPower(attacker)) {
				hitPoint[7] -= getPower(attacker);
			}else {
				hitPoint[7] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,Knight")) {
			if(hitPoint[8] > getPower(attacker)) {
				hitPoint[8] -= getPower(attacker);
			}else {
				hitPoint[8] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,Pawn1")) {
			if(hitPoint[9] > getPower(attacker)) {
				hitPoint[9] -= getPower(attacker);
			}else {
				hitPoint[9] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,Pawn2")) {
			if(hitPoint[10] > getPower(attacker)) {
				hitPoint[10] -= getPower(attacker);
			}else {
				hitPoint[10] = 0;
				deadflag = true;
			}
		}else if(target.equals("t,Archer")) {
			if(hitPoint[11] > getPower(attacker)) {
				hitPoint[11] -= getPower(attacker);
			}else {
				hitPoint[11] = 0;
				deadflag = true;
			}
		}else {
			System.out.println("damageError");
		}

		return deadflag;
	}


	// 大将の範囲ダメージ
	public boolean[] kingDamage(String[] target) {
		boolean[] deadflag = new boolean[getAttackRange("a,King").length];

		for(int i = 0; i < getAttackRange("a,King").length; i++) {
			deadflag[i] = false;
			if(!target[i].equals("null")) {
				if(damage("a,King", target[i])) {
					deadflag[i] = true;
				}
			}
		}

		return deadflag;
	}

	// 攻撃力の取得
	public int getPower(String name) {
		int power=0;

		if(name.equals("a,King")) {
			power = kingPower;
		}else if(name.equals("a,General")) {
			power = generalPower;
		}else if(name.equals("a,Pawn1")) {
			power = pawnPower;
		}else if(name.equals("a,Pawn2")) {
			power = pawnPower;
		}else if(name.equals("a,Archer")) {
			power = archerPower;
		}else if(name.equals("a,Knight")) {
			power = knightPower;
		}else if(name.equals("e,King")) {
			power = kingPower;
		}else if(name.equals("e,General")) {
			power = generalPower;
		}else if(name.equals("e,Pawn1")) {
			power = pawnPower;
		}else if(name.equals("e,Pawn2")) {
			power = pawnPower;
		}else if(name.equals("e,Archer")) {
			power = archerPower;
		}else if(name.equals("e,Knight")) {
			power = knightPower;
		}else {
			System.out.println("getPowerError!");
		}

		return power;
	}

	// 攻撃範囲の取得
	public int[] getAttackRange(String name) {
		if(name.equals("my,King")) {
			return kingAttackRange;
		}else if(name.equals("my,General")) {
			return generalAttackRange;
		}else if(name.equals("my,Pawn1")) {
			return pawnAttackRange;
		}else if(name.equals("my,Pawn2")) {
			return pawnAttackRange;
		}else if(name.equals("my,Archer")) {
			return archerAttackRange;
		}else if(name.equals("my,Knight")) {
			return knightAttackRange;
		}else if(name.equals("a,King")) {
			return kingAttackRange;
		}else if(name.equals("a,General")) {
			return generalAttackRange;
		}else if(name.equals("a,Pawn1")) {
			return pawnAttackRange;
		}else if(name.equals("a,Pawn2")) {
			return pawnAttackRange;
		}else if(name.equals("a,Archer")) {
			return archerAttackRange;
		}else if(name.equals("a,Knight")) {
			return knightAttackRange;
		}else {
			int [] dummyRange= {0,0,0,0,0,0};
			System.out.println("getAttackRangeError!");

			return dummyRange;
		}
	}

	// 移動範囲の取得
	public int[] getMoveRange(String name) {
		if(name.equals("my,King")) {
			return kingMoveRange;
		}else if(name.equals("my,General")) {
			return generalMoveRange;
		}else if(name.equals("my,Pawn1")) {
			return pawnMoveRange;
		}else if(name.equals("my,Pawn2")) {
			return pawnMoveRange;
		}else if(name.equals("my,Archer")) {
			return archerMoveRange;
		}else if(name.equals("my,Knight")) {
			return knightMoveRange;
		}else {
			int[] dummyRange= {0,0,0,0,0,0,0,0,0,0,0,0};
			System.out.println("getMoveRangeError!");

			return dummyRange;
		}
	}

}
