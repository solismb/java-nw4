// 1月7日(木) 情報ネットワーク

// [概要]
// NetClient1 と通信するためのサーバ。
// クライアントから一行文字列を受け取り、
// その内容を含めた文字列をクライアントに送信する。
//
// [注意事項]
// 自分でサーバを動かす際のポート番号の目安として、
// 10000 + 自分の出席番号を使うこと。
// もし先に何らかのプログラムが同ポートを使用中の場合、
// 「Exception: java.net.BindException: アドレスは既に使用中です」
// というエラーが出る。その時は 20000 + 自分の出席番号など別の番号を使うこと。
//
// [開発手順]
// 1. vim NetServer_Simple.java
// 2. 本プログラムを入力して保存終了する
// 3. javac NetServer_Simple.java
// 4. java NetServer_Simple localhost 10000+自分の出席番号
//
// [実行時の書式]
// java NetServer_Simple 10000+自分の出席番号
//
import java.io.*;
import java.net.*;

class NetServer_kadai3 {
	
	static double calcCircleArea(double radius) {
		return (radius * radius * Math.PI);
	}

	static double calcRectangleArea(double base, double height) {
		return (base * height);
	}
	
	public static void main(String args[]) {
		ServerSocket ss;

		//-------- 1) 実行時の引数チェック
		if (args.length != 1){
			System.out.println("usage: java NetServer_Simple port");
			return;
		}		
		int port = Integer.parseInt(args[0]); // 引数で指定したポート番号を得る
		
		//-------- 2) サーバソケットを作る
		try{
			ss = new ServerSocket(port);
		}
		catch(Exception e){
			System.out.println("Exception: " + e); // ソケット生成に失敗した場合
			return;
		}
		
		while(true){
			try{
				//-------- 3) クライアントから接続要求が来るまで待機する。
				//            接続が来たら変数 cs にクライアントと通信するソケットを入れる
				System.out.println("クライアントからのコネクション待ち...");
				Socket cs = ss.accept();
				
				//-------- 4) クライアントのアドレスを画面に表示する
				String cAddress = cs.getInetAddress().getHostAddress();
				int cPort = cs.getPort();
				System.out.println("クライアントから接続有");
				System.out.println("\t クライアントのアドレス = " + cAddress);
				System.out.println("\t クライアントのポート番号= " + cPort);
				
				//-------- 5) 指定した時間(単位は msec)待ってもパケットが来なければ
				//            タイムアウト例外を発生させて強制切断するようにする
				cs.setSoTimeout(20000);
				
				//-------- 6) クライアントとパケット送受信するためのストリームを作る
				InputStream is = cs.getInputStream();
				OutputStream os = cs.getOutputStream();
				DataInputStream in = new DataInputStream(is);
				DataOutputStream out = new DataOutputStream(os);
				
				try{
					//-------- 7) クライアントから文字列を受信し、画面に出力する
					String figure = in.readUTF();
					if (figure.equals("circle")) {
						double cRadius = in.readDouble();
						System.out.println("from client: " + cRadius);
						
						out.writeUTF("(サーバより)半径 " + cRadius + " の円の面積をお答えします");
						out.writeDouble(calcCircleArea(cRadius));
					}
					else if (figure.equals("rectangle")) {
						double cBase = in.readDouble();
						double cHeight = in.readDouble();
						System.out.println("from client: " + cBase + ", " + cHeight);

						out.writeUTF("(サーバより)底辺 " + cBase + ", 高さ " + cHeight + " の長方形の面積をお答えします");
						out.writeDouble(calcRectangleArea(cBase, cHeight));
					}
				}
				catch(SocketTimeoutException e){
					System.out.println("\tException: " + e);
				}
				
				//-------- 9) コネクションを切断する
				in.close();
				out.close();
				System.out.println("切断");
			}
			catch(Exception e){
				System.out.println("Exception: " + e);
			}
		}
	}
}
