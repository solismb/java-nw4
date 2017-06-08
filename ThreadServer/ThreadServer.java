// コンパイル
// javac ThreadServer.java
// 実行
// java ThreadServer サーバのポート番号
//
// - クライアントがCtrl+c等で途中終了してもサーバが終了しないようにした
// - クライアントから指定時間パケットが届かなければ切断するようにした
// - マルチスレッドによる同時接続に対応した
//
import java.io.*;
import java.net.*;

class CommClass implements Runnable {
	Socket s;

	// コンストラクタ
	CommClass(Socket _s) {
		this.s = _s;
		System.out.println("\tスレッド生成");
	}

	// スレッドで処理する通信内容
	public void run() {
		try {
			String cAddress = s.getInetAddress().getHostAddress();
			System.out.println("\t" + cAddress + "から接続有");

			// 指定した時間待ってもパケットが来なければタイムアウト例外を起こす
			s.setSoTimeout(30000);

			// クライアントからデータを受信する
			InputStream is = s.getInputStream();
			DataInputStream in = new DataInputStream(is);
			String cStr;
			try {
				cStr = in.readUTF();
			}
			catch(SocketTimeoutException e) {
				System.out.println("\tException: " + e);
				in.close();  // ストリームをクローズする
				System.out.println("\t 切断");
				return;
			}
			System.out.println("\t クライアントからの文字列: 「" + cStr + "」");

			// クライアントにデータを送信する
			OutputStream os = s.getOutputStream();
			DataOutputStream out = new DataOutputStream(os);
			out.writeUTF("(サーバより) あなたからの文字列「" + cStr + "」を受け取りました");

			in.close();   // ストリームをクローズする
			out.close();  // ストリームをクローズする
			System.out.println("\t 切断");
		}
		catch(Exception e) {
			System.out.println("\tException: " + e);
		}
	}
}

class ThreadServer {
	public static void main(String args[]) {
		ServerSocket ss;

		// 1) 引数チェックする
		if (args.length < 1) {
			System.out.println("usage: java ThreadServer port");
			return;
		}

		int port = Integer.parseInt(args[0]);  // 指定したポート番号を得る

		// 2) サーバソケット ss を作る
		try {
			ss = new ServerSocket(port);
		}
		catch(Exception e) {
			System.out.println("Exception: " + e);
			return;
		}

		// 3) メインループ
		while (true) {
			try {
				System.out.println("クライアントからのコネクション待ち..");

				// クライアントから接続が来るまでここで待機する
				CommClass c = new CommClass(ss.accept());;  // 4) 接続があるまで待機
				Thread th = new Thread(c);                  // 5) スレッドを生成
				th.start();                                 // 6) スレッドの処理開始
			}
			catch(Exception e) {
				System.out.println("Exception: " + e);
			}
		}
	}
}
