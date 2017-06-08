import java.io.*;
import java.net.*;

class NetClient1 {
	public static void main(String args[]) {
		// 引数チェック
		if (args.length < 2) {
			System.out.println("usage: java NetClient1 address port");
			return;
		}

		try {
			// キーボードから一行読み込み
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("一行入力してください: ");
			String inStr = br.readLine();

			// ソケットを作り、指定したサーバに接続する
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			Socket s = new Socket(server, port);

			// 入出力ストリームを得る
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			DataInputStream in = new DataInputStream(is);
			DataOutputStream out = new DataOutputStream(os);

			// サーバに文字列を送信する
			out.writeUTF(inStr);

			// サーバから文字列を受信して表示する
			String sStr = in.readUTF();
			System.out.println(sStr);

			// 入出力ストリームをクローズする(コネクションの切断)
			in.close();
			out.close();
		} catch(Exception e) {
			System.out.println("Exception: " + e);  // 例外処理
		}
	}
}
