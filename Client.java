//パッケージのインポート
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Client extends JFrame implements MouseListener, ActionListener {
	// 盤面用変数
	private int row = 8; // 盤面の行数
	private int col = 5; // 盤面の列数
	private String[] grids; // 盤面配列
	private JButton buttonArray[]; // 盤面用ボタン配列

	// 自分の先手後手情報
	private String myTurn;

	// 自分の行動用変数
	private String phase = "move"; // ターンフェーズ
	private int myplace = 0; // 移動, 攻撃させる駒の現在位置
	private int countAttacker = -1; // 攻撃できる駒の数
	private boolean countFlag = false; // 攻撃できる駒の数を数えたか判定する

	// 初期配置変数
	private int[] mySet = {12, 11, 6, 8, 13, 7};
	private int[] eSet = {12, 11, 6, 8, 13, 7};

	// 盤面表示用
	private Container c; // コンテナ
	private ImageIcon kingIcon, generalIcon, pawnIcon1, pawnIcon2, archerIcon, knightIcon; // 自分の駒のアイコン
	private ImageIcon eKingIcon, eGeneralIcon, ePawnIcon1, ePawnIcon2, eArcherIcon, eKnightIcon; // 相手の駒のアイコン
	private ImageIcon aKingIcon, aGeneralIcon, aPawnIcon1, aPawnIcon2, aArcherIcon, aKnightIcon; // 攻撃させる駒のアイコン
	private ImageIcon tKingIcon, tGeneralIcon, tPawnIcon1, tPawnIcon2, tArcherIcon, tKnightIcon; // 攻撃対象のアイコン
	private ImageIcon NewAccountIcon, loginIcon, returnIcon, startIcon, ruleIcon, rankIcon, titleIcon, setFinishIcon, questionIcon;
	private ImageIcon boardIcon, moveIcon, attackIcon; // 盤面のアイコン
	private Color color = new Color(204,153,0); // 背景色
	private JLabel my_nameLabel, opp_nameLabel; // 自分・相手の駒
	private JButton ruleButton2;
	private JLabel turnLabel;
	private JLabel firstLabel, secondLabel;
	private JLabel logLabel;
	private boolean setFlag;
	private JButton finishSetButton;
	private String finishSet = "finishSet";

	// 体力(HP)表示用
	private JLabel[] hptext = new JLabel[12]; // 体力表示用ラベル(数値)
	private JLabel[] hpBar = new JLabel[12]; // 体力表示用バー(可視化用)
	private JLabel[] pieceImage = new JLabel[12]; // HPの横に表示する駒のラベル
	private String[] hpLine = new String[12]; // 体力表示用バーの構成要素
	private int[] maxHP = new int[12]; // 最大HP(初期体力)
	private int[] nowHP = new int[12]; // 現在のHP
	private ImageIcon[] HPIcon = new ImageIcon[6]; // HPの横に表示する駒のアイコン

	// オブジェクト
	private Shogi shogi; // 将棋クラスオブジェクト
	private Piece piece; // 駒クラスオブジェクト
	private Setting set; // 設定クラスオブジェクト

	// 通信用変数
	private PrintWriter out; // データ送信用オブジェクト
	private Receiver receiver; // データ受信用オブジェクト

	// 画面遷移用
	private CardLayout layout;

	//開始画面の要素
	private JButton selectAccountButton;
	private JButton selectLoginButton;
	private String selectAccount = "selectAccount";
	private String selectLogin = "selectLogin";
	private JLabel homeLabel;

	//ログイン画面の要素
	private JButton backButtonAtLogin;
	private JButton loginButtonAtLogin;
	private JTextField nameBoxAtLogin;
	private JTextField passwordBoxAtLogin;
	private JLabel nameLabelAtLogin;
	private JLabel passwordLabelAtLogin;
	private JLabel errorLabelAtLogin;
	private String backAtLogin = "backAtLogin";
	private String loginAtLogin = "loginAtLogin";

	//アカウント画面の要素
	private JButton backButtonAtAccount;
	private JButton makeButtonAtAccount;
	private JTextField nameBoxAtAccount;
	private JTextField passwordBoxAtAccount;
	private JTextField passwordCheckBoxAtAccount;
	private JLabel nameLabelAtAccount;
	private JLabel passwordLabelAtAccount;
	private JLabel passwordCheckLabelAtAccount;
	private JLabel errorLabelAtAccount;
	private String backAtAccount = "backAtAccount";
	private String makeAtAccount = "makeAtAccount";

	//ホーム画面の要素
	private JButton startButton;
	private JButton ruleButton;
	private JButton rankButton;
	private String startAtHome = "startAtHome";
	private String rule = "rule";
	private String rankAtHome = "rankAtHome";

	// マッチング中画面の要素
	private JLabel matchingLabel;

	// 対戦開始前画面の要素
	private JLabel readyLabel;

	//ランク画面の要素
	private JTextField myrankBox;
	private JLabel myrankLabel;
	private JTextField toprankBox;
	private JLabel onerankLabel;
	private JLabel tworankLabel;
	private JLabel threerankLabel;
	private JButton backButtonAtRank;
	private String backAtRank = "backAtRank";
	private String myName;
	private String top1Name;
	private String top2Name;
	private String top3Name;
	private String myRank;
	private String myScore;
	private String top1Score;
	private String top2Score;
	private String top3Score;

	// コンストラクタ
	public Client() {
		// ウィンドウ設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ウィンドウを閉じる場合の処理
		setTitle("バトル将棋"); // ウィンドウのタイトル
		setSize(1050, 1000); // ウィンドウのサイズを設定
		setLocation(0, 0); // ウインドウの位置を設定
		c = getContentPane(); // フレームのペインを取得
		layout = new CardLayout();
		c.setLayout(layout);

		// 先攻の表示
		firstLabel = new JLabel("あなたは先攻です");
		firstLabel.setBounds(325, 15, 400,50);
		firstLabel.setHorizontalAlignment(JLabel.CENTER);
		firstLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		firstLabel.setForeground(Color.BLACK);
		firstLabel.setBorder(new LineBorder(Color.BLACK, 2, false));

		// 後攻の表示
		secondLabel = new JLabel("あなたは後攻です");
		secondLabel.setBounds(325, 15, 400,50);
		secondLabel.setHorizontalAlignment(JLabel.CENTER);
		secondLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		secondLabel.setForeground(Color.BLACK);
		secondLabel.setBorder(new LineBorder(Color.BLACK, 2, false));

		// 自分の表示
		my_nameLabel = new JLabel("自分の駒");
		my_nameLabel.setBounds(75, 15, 150, 75);
		my_nameLabel.setHorizontalAlignment(JLabel.CENTER);
		my_nameLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		my_nameLabel.setForeground(Color.BLACK);
		my_nameLabel.setBorder(new LineBorder(Color.BLACK, 2, false));

		// 相手の表示
		opp_nameLabel = new JLabel("相手の駒");
		opp_nameLabel.setBounds(775, 15, 150, 75);
		opp_nameLabel.setHorizontalAlignment(JLabel.CENTER);
		opp_nameLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		opp_nameLabel.setForeground(Color.BLACK);
		opp_nameLabel.setBorder(new LineBorder(Color.BLACK, 2, false));

		// 手番の表示
		turnLabel = new JLabel("");
		turnLabel.setBounds(325, 15, 400, 50);
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		turnLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		turnLabel.setForeground(Color.BLACK);
		turnLabel.setBorder(new LineBorder(Color.BLACK, 2, false));

		// ログの表示
		logLabel = new JLabel("");
		logLabel.setBounds(225, 800, 600, 50);
		logLabel.setHorizontalAlignment(JLabel.CENTER);
		logLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
		logLabel.setForeground(Color.BLACK);

		//ユーザ名入力インターフェース用テキストフィールドの作成
		nameBoxAtAccount = new JTextField(8);
		nameBoxAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		nameBoxAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 20));//書式設定：文字サイズを拡大

		//パスワード入力インターフェース用テキストフィールドの作成
		passwordBoxAtAccount = new JTextField(8);
		passwordBoxAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordBoxAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 20));//書式設定：文字サイズを拡大

		//パスワード確認インターフェース用テキストフィールドの作成
		passwordCheckBoxAtAccount = new JTextField(8);
		passwordCheckBoxAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordCheckBoxAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 20));//書式設定：文字サイズを拡大

		//エラー表示用ラベルの作成
		errorLabelAtAccount = new JLabel("");
		errorLabelAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		errorLabelAtAccount.setForeground(Color.RED);//書式設定：文字色を赤に
		errorLabelAtAccount.setFont(new Font("MSゴシック", Font.PLAIN, 20));//書式設定：文字サイズを拡大

		//パスワード入力インターフェース用テキストフィールドの作成
		nameBoxAtLogin = new JTextField(8);
		nameBoxAtLogin.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		nameBoxAtLogin.setFont(new Font("MSゴシック", Font.PLAIN, 20));//書式設定：文字サイズを拡大

		//パスワード入力インターフェース用テキストフィールドの作成
		passwordBoxAtLogin = new JTextField(8);
		passwordBoxAtLogin.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordBoxAtLogin.setFont(new Font("MSゴシック", Font.PLAIN, 20));//書式設定：文字サイズを拡大

		//エラー表示用ラベルの作成
		errorLabelAtLogin = new JLabel("");
		errorLabelAtLogin.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		errorLabelAtLogin.setForeground(Color.RED);//書式設定：文字色を赤に
		errorLabelAtLogin.setFont(new Font("MSゴシック", Font.PLAIN, 20));//書式設定：文字サイズを拡大

		// マッチング中表示用ラベルの作成
		matchingLabel = new JLabel("マッチング中...");
		matchingLabel.setBounds(275, 450, 500, 100);
		matchingLabel.setHorizontalAlignment(JLabel.CENTER);
		matchingLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 50));
		matchingLabel.setForeground(Color.BLACK);

		// 対戦開始中表示用ラベルの作成
		readyLabel = new JLabel("対戦開始中...");
		readyLabel.setBounds(275, 450, 500, 100);
		readyLabel.setHorizontalAlignment(JLabel.CENTER);
		readyLabel.setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 50));
		readyLabel.setForeground(Color.BLACK);

		connectServer("localhost",10000);
		//connectServer("192.168.1.9",10000);

		//アイコン設定(画像ファイルをアイコンとして使う)
		NewAccountIcon = new ImageIcon("newAccount.png");
		loginIcon = new ImageIcon("login.png");
		returnIcon = new ImageIcon("return.jpg");
		startIcon = new ImageIcon("battleStart.png");
		ruleIcon = new ImageIcon("ruleButton.png");
		rankIcon = new ImageIcon("rank.png");
		titleIcon = new ImageIcon("title.png");
		setFinishIcon = new ImageIcon("setFinish.png");
		questionIcon = new ImageIcon("question.jpg");

		kingIcon = new ImageIcon("king.jpg");
		generalIcon = new ImageIcon("general.jpg");
		pawnIcon1 = new ImageIcon("pawn1.jpg");
		pawnIcon2 = new ImageIcon("pawn2.jpg");
		archerIcon = new ImageIcon("archer.jpg");
		knightIcon = new ImageIcon("knight.jpg");

		eKingIcon = new ImageIcon("eKing.jpg");
		eGeneralIcon = new ImageIcon("eGeneral.jpg");
		ePawnIcon1 = new ImageIcon("ePawn1.jpg");
		ePawnIcon2 = new ImageIcon("ePawn2.jpg");
		eArcherIcon = new ImageIcon("eArcher.jpg");
		eKnightIcon = new ImageIcon("eKnight.jpg");

		aKingIcon = new ImageIcon("aKing.jpg");
		aGeneralIcon = new ImageIcon("aGeneral.jpg");
		aPawnIcon1 = new ImageIcon("aPawn1.jpg");
		aPawnIcon2 = new ImageIcon("aPawn2.jpg");
		aArcherIcon = new ImageIcon("aArcher.jpg");
		aKnightIcon = new ImageIcon("aKnight.jpg");

		tKingIcon = new ImageIcon("tKing.jpg");
		tGeneralIcon = new ImageIcon("tGeneral.jpg");
		tPawnIcon1 = new ImageIcon("tPawn1.jpg");
		tPawnIcon2 = new ImageIcon("tPawn2.jpg");
		tArcherIcon = new ImageIcon("tArcher.jpg");
		tKnightIcon = new ImageIcon("tKnight.jpg");

		boardIcon = new ImageIcon("board.jpg");
		moveIcon = new ImageIcon("mBoard.jpg");
		attackIcon = new ImageIcon("aBoard.jpg");

		HPIcon[0] = new ImageIcon("king.png");
		HPIcon[1] = new ImageIcon("general.png");
		HPIcon[2] = new ImageIcon("knight.png");
		HPIcon[3] = new ImageIcon("pawn1.png");
		HPIcon[4] = new ImageIcon("pawn2.png");
		HPIcon[5] = new ImageIcon("archer.png");

		// 初期設定
		initialSettings();
	}

	// メソッド
	// 初期設定
	public void initialSettings() {
		piece = new Piece();
		set = new Setting(mySet);
		grids = set.getGrids();
		setFlag = true;

		myTurn = "first";

		for(int i = 0; i < 12; i++) {
			maxHP[i] = piece.getHP(i);
		}

		startPanelInit();
	}

	// サーバに接続
	public void connectServer(String ipAddress, int port){
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port); //サーバ(ipAddress, port)に接続
			out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意
			receiver = new Receiver(socket); //受信用オブジェクトの準備
			receiver.start();//受信用オブジェクト(スレッド)起動
			InetAddress addr = InetAddress.getLocalHost();
			out.println(addr.getHostAddress());
		} catch (UnknownHostException e) {
			System.err.println("ホストのIPアドレスが判定できません: " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("サーバ接続時にエラーが発生しました: " + e);
			System.exit(-1);
		}
	}

	// サーバに操作情報を送信
	public void sendMessage(String msg){
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データを送る
		System.out.println("サーバにメッセージ " + msg + " を送信しました"); //テスト標準出力
	}

	// データ受信用スレッド(内部クラス)
	class Receiver extends Thread {
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ

		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				sisr = new InputStreamReader(socket.getInputStream()); //受信したバイトデータを文字ストリームに
				br = new BufferedReader(sisr);//文字ストリームをバッファリングする
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		// 内部クラス Receiverのメソッド
		public void run(){
			try{
				while(true) { // 接続が切れるまでデータを受信し続ける
					String inputLine = br.readLine(); // 受信データを一行分読み込む
					if (inputLine != null){ // データを受信したら
						receiveMessage(inputLine); // データ受信用メソッドを呼び出す
					}
				}
			} catch (IOException e){
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
	}

	public void receiveMessage(String msg){	// メッセージの受信
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); // テスト用標準出力

		String[] strList; // 分割後のメッセージ保存
		String[] strList2;
		String token = ","; // 分割記号
		String cmd; // コマンド保存
		int tmp1,tmp2; // 座標一時保存
		int nowplace; // 移動前の座標
		int nextplace; // 移動後の座標
		int attackerplace; // 攻撃者の座標
		int targetplace; // 攻撃される座標


		strList = msg.split(token);
		cmd = strList[0];

		if(cmd.equals("move")) { // moveコマンドの場合
			tmp1 = Integer.parseInt(strList[1]);
			tmp2 = Integer.parseInt(strList[2]);

			// 自分視点に座標変換
			tmp1 = Math.abs(tmp1 - 39);
			tmp2 = Math.abs(tmp2 - 39);

			nowplace = tmp1;
			nextplace = tmp2;
			strList2 = grids[nowplace].split(token);
			shogi.Move(nowplace, nextplace);

			logLabel.setText("相手の" + strList2[1] + "が移動しました");

			updateDisp();
		}else if(cmd.equals("attack")) { // attackコマンドの場合
			tmp1 = Integer.parseInt(strList[1]);
			tmp2 = Integer.parseInt(strList[2]);

			// 自分視点に座標変換
			tmp1 = Math.abs(tmp1 - 39);
			tmp2 = Math.abs(tmp2 - 39);

			attackerplace = tmp1;
			targetplace = tmp2;
			shogi.Attack(attackerplace, targetplace);

			strList2 = grids[attackerplace].split(token);
			logLabel.setText("相手の" + strList2[1] + "が攻撃しました");

			updateDisp();
			result();
		}else if(cmd.equals("endturn")) { // endturnコマンドの場合
			acceptOperation();
		}else if(cmd.equals("accountCorrect")) { //アカウント作成成功
			homePanelInit();
		}else if(cmd.equals("accountError")) { //アカウント作成失敗
			errorLabelAtAccount.setText("同一IDとパスワードを持つアカウントが存在");
			accountPanelInit();
		}else if(cmd.equals("nameCorrect")) { //ログイン成功
			homePanelInit();
		}else if(cmd.equals("logError")) { // 既にログイン済み
			errorLabelAtLogin.setText("そのアカウントは既にログイン済みです");
		}else if(cmd.equals("nameError")) { // 入力間違い
			errorLabelAtLogin.setText("IDまたはパスワードが間違っています");
		}else if(cmd.equals("disconnected")){
			logLabel.setText("You Win");
			sendMessage("result,You Win");
			updateDisp();
			try {
				Thread.sleep(3000);
			}catch(Exception e) {
			}
			homePanelInit();
		}else if(cmd.equals("first")){
			matchingPanelInit();
		}else if(cmd.equals("second")) {
			myTurn = "second";
			updateDisp();
		}else if(cmd.equals("set")) {
			for(int i = 0; i < 6; i++) {
				eSet[i] = Integer.parseInt(strList[i + 1]);
			}
		}else if(cmd.equals("wait")){
			readyPanelInit();
		}else if(cmd.equals("ready")) {
			shogi = new Shogi(mySet, eSet, piece);
			grids = shogi.getGrids();
			shogi.trueAttackFlag();
			updateDisp();
		}else if(cmd.equals("myRank")) {
			myRank = strList[1];
			myName = strList[2];
			myScore = strList[3];
		}else if(cmd.equals("top1")) {
			top1Name = strList[1];
			top1Score = strList[2];
		}else if(cmd.equals("top2")) {
			top2Name = strList[1];
			top2Score = strList[2];
		}else if(cmd.equals("top3")) {
			top3Name = strList[1];
			top3Score = strList[2];
		}else {
			sendMessage("matched," + cmd);
			updateDisp();
		}

	}

	public void updateDisp(){ // 画面を更新する
		c.removeAll();
		c.setLayout(null);

		//盤面の生成
		buttonArray = new JButton[row * col];
		for(int i = 0; i < row * col; i++) {
			if(grids[i].equals("my,King")){ buttonArray[i] = new JButton(kingIcon);}
			if(grids[i].equals("my,General")){ buttonArray[i] = new JButton(generalIcon);}
			if(grids[i].equals("my,Pawn1")){ buttonArray[i] = new JButton(pawnIcon1);}
			if(grids[i].equals("my,Pawn2")){ buttonArray[i] = new JButton(pawnIcon2);}
			if(grids[i].equals("my,Archer")){ buttonArray[i] = new JButton(archerIcon);}
			if(grids[i].equals("my,Knight")){ buttonArray[i] = new JButton(knightIcon);}
			if(grids[i].equals("e,King")){ buttonArray[i] = new JButton(eKingIcon);}
			if(grids[i].equals("e,General")){ buttonArray[i] = new JButton(eGeneralIcon);}
			if(grids[i].equals("e,Pawn1")){ buttonArray[i] = new JButton(ePawnIcon1);}
			if(grids[i].equals("e,Pawn2")){ buttonArray[i] = new JButton(ePawnIcon2);}
			if(grids[i].equals("e,Archer")){ buttonArray[i] = new JButton(eArcherIcon);}
			if(grids[i].equals("e,Knight")){ buttonArray[i] = new JButton(eKnightIcon);}
			if(grids[i].equals("a,King")){ buttonArray[i] = new JButton(aKingIcon);}
			if(grids[i].equals("a,General")){ buttonArray[i] = new JButton(aGeneralIcon);}
			if(grids[i].equals("a,Pawn1")){ buttonArray[i] = new JButton(aPawnIcon1);}
			if(grids[i].equals("a,Pawn2")){ buttonArray[i] = new JButton(aPawnIcon2);}
			if(grids[i].equals("a,Archer")){ buttonArray[i] = new JButton(aArcherIcon);}
			if(grids[i].equals("a,Knight")){ buttonArray[i] = new JButton(aKnightIcon);}
			if(grids[i].equals("t,King")){ buttonArray[i] = new JButton(tKingIcon);}
			if(grids[i].equals("t,General")){ buttonArray[i] = new JButton(tGeneralIcon);}
			if(grids[i].equals("t,Pawn1")){ buttonArray[i] = new JButton(tPawnIcon1);}
			if(grids[i].equals("t,Pawn2")){ buttonArray[i] = new JButton(tPawnIcon2);}
			if(grids[i].equals("t,Archer")){ buttonArray[i] = new JButton(tArcherIcon);}
			if(grids[i].equals("t,Knight")){ buttonArray[i] = new JButton(tKnightIcon);}
			if(grids[i].equals("board")){ buttonArray[i] = new JButton(boardIcon);}
			if(grids[i].equals("move,Board")){ buttonArray[i] = new JButton(moveIcon);}
			if(grids[i].equals("attack,Board")){ buttonArray[i] = new JButton(attackIcon);}

			c.add(buttonArray[i]); // ボタンの配列をペインに貼り付け

			// ボタンを配置する
			int x = (i % col) * 75 + 325;
			int y = (int)(i / col) * 75 + 100;
			buttonArray[i].setBounds(x, y, 75, 75); // ボタンの大きさと位置を設定する
			buttonArray[i].setActionCommand(Integer.toString(i)); // ボタンを識別するための名前(番号)を付加する

			String[] strList; // 分割後のメッセージ保存
			String token = ","; // 分割記号
			String cmd; // コマンド保存

			strList = grids[i].split(token);
			cmd = strList[0];

			// マウス操作を認識できるようにする
			if(setFlag) {
				if(cmd.equals("my") || cmd.equals("move") || cmd.equals("a")) {
					buttonArray[i].addMouseListener(this);
				}
			}else {
				if(myTurn.equals(shogi.getTurn())) { // 自分の番のとき
					if(phase.equals("move") && cmd.equals("my")) { // 移動フェーズ && 自分の駒
						buttonArray[i].addMouseListener(this);
					}else if(phase.equals("move") && cmd.equals("move")) { // 移動フェーズ && 移動できるマス
						buttonArray[i].addMouseListener(this);
					}else if(phase.equals("attack") && cmd.equals("a")) { // 攻撃フェーズ && 攻撃させる駒
						buttonArray[i].addMouseListener(this);
					}else if(phase.equals("attack") && cmd.equals("t")) { // 攻撃フェーズ && 攻撃対象の駒
						buttonArray[i].addMouseListener(this);
					}
				}
			}
		}

		if(setFlag) {
			//ボタンの作成
			finishSetButton = new JButton(setFinishIcon);

			//ボタン操作を認識できるようにする
			finishSetButton.addActionListener(this);
			finishSetButton.setActionCommand(finishSet);
			finishSetButton.setBounds(375, 750, 300, 100);
			finishSetButton.setContentAreaFilled(false);
			finishSetButton.setBorderPainted(false);

			c.add(finishSetButton);

			if(myTurn.equals("first")) {
				c.add(firstLabel);
			}else {
				c.add(secondLabel);
			}
		}else {
			if(myTurn.equals(shogi.getTurn())) {
				turnLabel.setText("あなたの番です");
			}else {
				turnLabel.setText("相手の番です");
			}

			c.add(turnLabel);
			c.add(my_nameLabel);
			c.add(opp_nameLabel);
			c.add(logLabel);

			ruleButton2 = new JButton(questionIcon);
			ruleButton2.setBounds(945, 850, 75, 75);
			ruleButton2.addActionListener(this);
			ruleButton2.setActionCommand(rule);
			c.add(ruleButton2);

			for(int i = 0; i < 12; i++) {
				nowHP[i] = piece.getHP(i);

				hpLine[i] = "";

				for(int j = 0; j < nowHP[i] / 5; j++) {
					hpLine[i] = hpLine[i] + "|";
				}

				hpBar[i] = new JLabel(hpLine[i]);
				hpBar[i].setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 5));
				hpBar[i].setForeground(Color.green);
				hpBar[i].setOpaque(true);
				hpBar[i].setBackground(Color.black);

				hptext[i] = new JLabel(nowHP[i] + "/" + maxHP[i]);
				hptext[i].setFont(new Font( "ＭＳ ゴシック" , Font.BOLD, 30));
			}

			for(int i = 0; i < 6; i++) {
				hpBar[i].setBounds(100, i * 100 + 110, maxHP[i] * 3 / 5, 7);

				hptext[i].setBounds(100, i * 100 + 135, 300, 30);

				pieceImage[i] = new JLabel(HPIcon[i]);
				pieceImage[i].setBounds(25, i * 100 + 95, 75, 75);
			}

			for(int i = 6; i < 12; i++) {
				hpBar[i].setBounds(800, (i - 6) * 100 + 110, maxHP[i] * 3 / 5, 7);

				hptext[i].setBounds(800, (i - 6) * 100 + 135, 300, 30);

				pieceImage[i] = new JLabel(HPIcon[i - 6]);
				pieceImage[i].setBounds(725, (i - 6) * 100 + 95, 75, 75);
			}

			for(int i = 0; i < 12; i++) {
				c.add(hpBar[i]);
				c.add(hptext[i]);
				c.add(pieceImage[i]);
			}
		}

		c.setBackground(color); // 背景色
		c.repaint();
	}

	public void acceptOperation(){	// プレイヤの操作を受付
		shogi.changeTurn();
		shogi.trueAttackFlag();
		phase = "move";
		updateDisp();
	}

	public void result() {
		if(shogi.CheckFinish()) {
			logLabel.setText(shogi.checkWinner());
			sendMessage("result," + shogi.checkWinner());
			updateDisp();
			try {
				Thread.sleep(3000);
			}catch(Exception e) {
			}
			homePanelInit();
		}
	}

  	// マウスクリック時の処理
	public void mouseClicked(MouseEvent e) {
		JButton theButton = (JButton)e.getComponent(); // クリックしたオブジェクトを得る
		String command = theButton.getActionCommand(); // ボタンの名前を取り出す
		int place = Integer.parseInt(command); // ボタンの名前(int)

		System.out.println("マウスがクリックされました。押されたボタンは " + grids[place] + " です。"); // テスト用に標準出力

		String[] strList; // 分割後のメッセージ保存
		String[] strList2;
		String token = ","; // 分割記号
		String cmd; // コマンド保存

		strList = grids[place].split(token);
		cmd = strList[0];

		if(setFlag) {
			if(cmd.equals("my")) {
				myplace = place;
				set.moveAble(grids[place]);
			}else if(cmd.equals("a") || cmd.equals("move")) {
				set.move(myplace, place);
			}

			updateDisp();
		}else {
			if(phase.equals("move") && cmd.equals("my")) { // 移動フェーズ && 自分の駒
				shogi.MoveRange(place);
				myplace = place;
			}else if(phase.equals("move") && cmd.equals("move")) { // 移動フェーズ && 移動できるマス
				strList2 = grids[myplace].split(token);
				shogi.Move(myplace, place);
				sendMessage("move," + myplace + "," + place);
				logLabel.setText("あなたの" + strList2[1] + "が移動しました");
				phase = "attack";
				countAttacker = shogi.countAttacker();
				countFlag = true;
			}else if(phase.equals("attack") && cmd.equals("a")) { // 攻撃フェーズ && 攻撃させる駒
				shogi.AttackRange(place);
				myplace = place;
			}else if(phase.equals("attack") && cmd.equals("t")) { // 攻撃フェーズ && 攻撃対象の駒
				shogi.Attack(myplace, place);
				strList2 = grids[myplace].split(token);
				logLabel.setText("あなたの" + strList2[1] + "が攻撃しました");
				shogi.falseAttackFlag(grids[myplace]);
				sendMessage("attack," + myplace + "," + place);
				countAttacker = shogi.countAttacker();
			}

			if(countAttacker == 0 && countFlag) {
				sendMessage("endturn,0,0");
				shogi.changeTurn();
				countFlag = false;
			}

			updateDisp();
			result();
		}
	}
	public void mouseEntered(MouseEvent e) {} // マウスがオブジェクトに入ったときの処理
	public void mouseExited(MouseEvent e) {} // マウスがオブジェクトから出たときの処理
	public void mousePressed(MouseEvent e) {} // マウスでオブジェクトを押したときの処理
	public void mouseReleased(MouseEvent e) {} // マウスで押していたオブジェクトを離したときの処理

	//画面初期化メソッド
	private void startPanelInit() {
		c.removeAll();
		c.setLayout(null);

		//ボタンの作成
		homeLabel = new JLabel(titleIcon);

		selectAccountButton = new JButton(NewAccountIcon);
		selectAccountButton.setContentAreaFilled(false);
		selectAccountButton.setBorderPainted(false);
		selectLoginButton = new JButton(loginIcon);
		selectLoginButton.setContentAreaFilled(false);
		selectLoginButton.setBorderPainted(false);

		//ボタン操作を認識できるようにする
		selectAccountButton.addActionListener(this);
		selectAccountButton.setActionCommand(selectAccount);
		selectLoginButton.addActionListener(this);
		selectLoginButton.setActionCommand(selectLogin);

		//ボタンを配置
		c.add(homeLabel);
		c.add(selectAccountButton);
		c.add(selectLoginButton);
		homeLabel.setBounds(150, 50, 800, 800);
		selectAccountButton.setBounds(70, 650, 400, 153);
		selectLoginButton.setBounds(570, 650, 400, 153);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void loginPanelInit() {
		//要素のレイアウト用の変数
		int x = 200;
		int y = 250;
		int yDistance = 100;
		int labelWidth = 100;
		int boxWidth = 500;
		int errorWidth = 600;
		int height = 100;

		c.removeAll();
		c.setLayout(null);

		//ユーザ名入力インターフェース用ラベルの作成
		nameLabelAtLogin = new JLabel("ユーザ名");
		nameLabelAtLogin.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		nameLabelAtLogin.setFont(new Font("MSゴシック", Font.BOLD, 15));//書式設定：文字サイズを拡大
		//loginPanel.add(nameLabelAtLogin);
		c.add(nameLabelAtLogin);

		//ユーザ名入力インターフェース用テキストフィールドの作成
		c.add(nameBoxAtLogin);

		//パスワード入力インターフェース用ラベルの作成
		passwordLabelAtLogin = new JLabel("パスワード");
		passwordLabelAtLogin.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordLabelAtLogin.setFont(new Font("MSゴシック", Font.BOLD, 15));//書式設定：文字サイズを拡大
		//loginPanel.add(passwordLabelAtLogin);
		c.add(passwordLabelAtLogin);

		//パスワード入力インターフェース用テキストフィールドの作成
		c.add(passwordBoxAtLogin);

		//ボタンの作成
		backButtonAtLogin = new JButton(returnIcon);
		loginButtonAtLogin = new JButton(loginIcon);
		loginButtonAtLogin.setContentAreaFilled(false);
		loginButtonAtLogin.setBorderPainted(false);

		//ボタン操作を認識できるようにする
		backButtonAtLogin.addActionListener(this);
		backButtonAtLogin.setActionCommand(backAtLogin);
		loginButtonAtLogin.addActionListener(this);
		loginButtonAtLogin.setActionCommand(loginAtLogin);

		//ボタンを配置
		c.add(backButtonAtLogin);
		c.add(loginButtonAtLogin);

		//エラー表示用ラベルの作成
		c.add(errorLabelAtLogin);


		//レイアウトを決定
		nameLabelAtLogin.setBounds(x, y, labelWidth, height);
		nameBoxAtLogin.setBounds(x + labelWidth, y, boxWidth, height);
		passwordLabelAtLogin.setBounds(x, y + height + yDistance, labelWidth, height);
		passwordBoxAtLogin.setBounds(x + labelWidth, y + height + yDistance, boxWidth, height);
		errorLabelAtLogin.setBounds(x, 600, errorWidth, height);
		backButtonAtLogin.setBounds(50, 50, 150, 150);
		loginButtonAtLogin.setBounds(300, 700, 400, 153);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void accountPanelInit() {
		//要素のレイアウト用の変数
		int x = 200;
		int y = 150;
		int yDistance = 50;
		int labelWidth = 100;
		int boxWidth = 500;
		int errorWidth = 600;
		int height = 100;

		c.removeAll();
		c.setLayout(null);

		//ユーザ名入力インターフェース用ラベルの作成
		nameLabelAtAccount = new JLabel("ユーザ名");
		nameLabelAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		nameLabelAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 15));//書式設定：文字サイズを拡大
		//accountPanel.add(nameLabelAtAccount);
		c.add(nameLabelAtAccount);

		//ユーザ名入力インターフェース用テキストフィールドの作成
		c.add(nameBoxAtAccount);

		//パスワード入力インターフェース用ラベルの作成
		passwordLabelAtAccount = new JLabel("パスワード");
		passwordLabelAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordLabelAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 15));//書式設定：文字サイズを拡大
		//accountPanel.add(passwordLabelAtAccount);
		c.add(passwordLabelAtAccount);

		//パスワード入力インターフェース用テキストフィールドの作成
		c.add(passwordBoxAtAccount);

		//パスワード確認インターフェース用ラベルの作成
		passwordCheckLabelAtAccount = new JLabel("<html><body>パスワード<br />（確認）<body><html>");
		passwordCheckLabelAtAccount.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		passwordCheckLabelAtAccount.setFont(new Font("MSゴシック", Font.BOLD, 15));//書式設定：文字サイズを拡大
		//accountPanel.add(passwordCheckLabelAtAccount);
		c.add(passwordCheckLabelAtAccount);

		//パスワード確認インターフェース用テキストフィールドの作成
		c.add(passwordCheckBoxAtAccount);

		//ボタンの作成
		backButtonAtAccount = new JButton(returnIcon);
		makeButtonAtAccount = new JButton(NewAccountIcon);
		makeButtonAtAccount.setContentAreaFilled(false);
		makeButtonAtAccount.setBorderPainted(false);

		//ボタン操作を認識できるようにする
		backButtonAtAccount.addActionListener(this);
		backButtonAtAccount.setActionCommand(backAtAccount);
		makeButtonAtAccount.addActionListener(this);
		makeButtonAtAccount.setActionCommand(makeAtAccount);

		//ボタンを配置

		c.add(backButtonAtAccount);
		c.add(makeButtonAtAccount);

		//エラー表示用ラベルの作成
		c.add(errorLabelAtAccount);

		//レイアウトを決定
		nameLabelAtAccount.setBounds(x, y, labelWidth, height);
		nameBoxAtAccount.setBounds(x + labelWidth, y, boxWidth, height);
		passwordLabelAtAccount.setBounds(x, y + height + yDistance, labelWidth, height);
		passwordBoxAtAccount.setBounds(x + labelWidth, y + height + yDistance, boxWidth, height);
		passwordCheckLabelAtAccount.setBounds(x, y + (height + yDistance) * 2, labelWidth, height);
		passwordCheckBoxAtAccount.setBounds(x + labelWidth, y + (height + yDistance) * 2, boxWidth, height);
		errorLabelAtAccount.setBounds(x, y + (height + yDistance) * 3, errorWidth, height);
		backButtonAtAccount.setBounds(50, 50, 150, 150);
		makeButtonAtAccount.setBounds(300, y + (height + yDistance) * 4, 400, 153);


		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void homePanelInit() {

		//要素のレイアウト用の変数
		int x = 300;
		int y = 200;
		int yDistance = 50;
		int width = 400;
		int height = 153;

		//パネルの作成
		c.removeAll();
		c.setLayout(null);

		//ボタンの作成
		startButton = new JButton(startIcon);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		ruleButton = new JButton(ruleIcon);
		ruleButton.setContentAreaFilled(false);
		ruleButton.setBorderPainted(false);
		rankButton = new JButton(rankIcon);
		rankButton.setContentAreaFilled(false);
		rankButton.setBorderPainted(false);

		//ボタン操作を認識できるようにする
		startButton.addActionListener(this);
		startButton.setActionCommand(startAtHome);
		ruleButton.addActionListener(this);
		ruleButton.setActionCommand(rule);
		rankButton.addActionListener(this);
		rankButton.setActionCommand(rankAtHome);

		//ボタンを配置
		c.add(startButton);
		c.add(ruleButton);
		c.add(rankButton);

		//レイアウトを決定
		startButton.setBounds(x, y, width, height);
		ruleButton.setBounds(x, y + height + yDistance, width, height);
		rankButton.setBounds(x, y + (height + yDistance) * 2, width, height);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void rankPanelInit() {
		//要素のレイアウト用の変数
		int x = 275;
		int y = 250;
		int yDistance = 30;
		int buttonWidth = 500;
		int height = 40;


		c.removeAll();
		c.setLayout(null);

		//対局記録表示用インターフェースの作成
		myrankBox = new JTextField("あなたの順位");
		myrankBox.setEditable(false);
		myrankBox.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		myrankBox.setFont(new Font("MSゴシック", Font.BOLD, 20));//書式設定：文字サイズを拡大
		myrankLabel = new JLabel( myRank + "位  " + myName + "  " + myScore + "pt");
		myrankLabel.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		myrankLabel.setFont(new Font("MSゴシック", Font.BOLD, 30));//書式設定：文字サイズを拡大
		toprankBox = new JTextField("トップ３");
		toprankBox.setEditable(false);
		toprankBox.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		toprankBox.setFont(new Font("MSゴシック", Font.BOLD, 20));//書式設定：文字サイズを拡大
		onerankLabel = new JLabel("1位  " + top1Name + "  " + top1Score + "pt");
		onerankLabel.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		onerankLabel.setFont(new Font("MSゴシック", Font.BOLD, 30));//書式設定：文字サイズを拡大
		tworankLabel = new JLabel("2位  " + top2Name + "  " + top2Score + "pt");
		tworankLabel.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		tworankLabel.setFont(new Font("MSゴシック", Font.BOLD, 30));//書式設定：文字サイズを拡大
		threerankLabel = new JLabel("3位  " + top3Name + "  " + top3Score + "pt");
		threerankLabel.setHorizontalAlignment(JLabel.CENTER);//書式設定：中央揃え
		threerankLabel.setFont(new Font("MSゴシック", Font.BOLD, 30));//書式設定：文字サイズを拡大

		//ボタンの作成
		backButtonAtRank = new JButton(returnIcon);

		//ボタン操作を認識できるようにする
		backButtonAtRank.addActionListener(this);
		backButtonAtRank.setActionCommand(backAtRank);

		//ボタンを配置
		c.add(myrankBox);
		c.add(myrankLabel);
		c.add(toprankBox);
		c.add(onerankLabel);
		c.add(tworankLabel);
		c.add(threerankLabel);
		c.add(backButtonAtRank);

		//レイアウトを決定
		myrankBox.setBounds(x, y + (height + yDistance) , buttonWidth, height);
		myrankLabel.setBounds(x, y + (height + yDistance) * 2, buttonWidth, height);
		toprankBox.setBounds(x, y + (height + yDistance) * 3, buttonWidth, height);
		onerankLabel.setBounds(x, y + (height + yDistance) * 4 , buttonWidth, height);
		tworankLabel.setBounds(x, y + (height + yDistance) * 5, buttonWidth, height);
		threerankLabel.setBounds(x, y + (height + yDistance) * 6, buttonWidth, height);
		backButtonAtRank.setBounds(50, 50, 150, 150);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void matchingPanelInit() {
		c.removeAll();
		c.setLayout(null);

		c.add(matchingLabel);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	private void readyPanelInit() {
		c.removeAll();
		c.setLayout(null);

		c.add(readyLabel);

		c.setBackground(color); // 背景色
		c.repaint();
	}

	//ボタンクリック時の動作の設定
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();

		if(cmd.equals(selectAccount)) {
			//アカウント作成画面の初期化

			nameBoxAtAccount.setText("");
			passwordBoxAtAccount.setText("");
			passwordCheckBoxAtAccount.setText("");
			errorLabelAtAccount.setText("");

			c.removeAll();

			accountPanelInit();//アカウント作成画面へ遷移
		}
		if(cmd.equals(selectLogin)) {
			//ログイン画面の初期化

			nameBoxAtLogin.setText("");
			passwordBoxAtLogin.setText("");
			errorLabelAtLogin.setText("");
			c.removeAll();

			loginPanelInit();//ログイン画面へ遷移
		}
		if(cmd.equals(backAtLogin)) {
			System.out.println("modoru kakunin");
			c.removeAll();
			startPanelInit();//スタート画面へ遷移
		}
		if(cmd.equals(backAtAccount)) {
			System.out.println("modoru kakunin");
			c.removeAll();
			startPanelInit();//スタート画面へ遷移
		}
		if(cmd.equals(makeAtAccount)) {
			String nBAA = nameBoxAtAccount.getText();
			String pBAA = passwordBoxAtAccount.getText();
			String pCBAA = passwordCheckBoxAtAccount.getText();

			if(nBAA.equals("")) {
				errorLabelAtAccount.setText("名前を入力してください");
				accountPanelInit();
			}else if(pBAA.equals("")) {
				errorLabelAtAccount.setText("パスワードを入力してください");
				accountPanelInit();
			}else if(pBAA.equals(pCBAA)) {
				sendMessage("makeAccount,"+nBAA+","+pBAA);
			}else {
				errorLabelAtAccount.setText("パスワードが一致しません");
				accountPanelInit();
			}
		}
		if(cmd.equals(loginAtLogin)) {
			String nBAL = nameBoxAtLogin.getText();
			String pBAL = passwordBoxAtLogin.getText();

			if(nBAL.equals("")) {
				errorLabelAtLogin.setText("名前を入力してください");
				loginPanelInit();
			}else if(pBAL.equals("")) {
				errorLabelAtLogin.setText("パスワードを入力してください");
				loginPanelInit();
			}else {
				sendMessage("name," + nBAL + "," + pBAL);
			}
		}
		if(cmd.equals(rankAtHome)) {
			sendMessage("ranking");
			c.removeAll();
			c.setLayout(null);
			rankPanelInit();
		}
		if(cmd.equals(rule)) {
			Rule rule = new Rule(ModalityType.MODELESS);
			rule.setLocation(this.getLocation().x + 1050, this.getLocation().y);
			rule.setVisible(true);
		}
		if(cmd.equals(backAtRank)) {
			c.removeAll();
			c.setLayout(null);
			homePanelInit();
		}
		if(cmd.equals(startAtHome)) {
			sendMessage("matching");
		}
		if(cmd.equals(finishSet)) {
			mySet = set.getMySet();
			setFlag = false;
			sendMessage("set," + mySet[0] + "," + mySet[1] + "," + mySet[2] + "," + mySet[3] + "," + mySet[4] + "," + mySet[5]);
		}
	}


	// mainメソッド
	public static void main(String[] args) {
		Client client = new Client();
		client.setVisible(true);
	}
}
