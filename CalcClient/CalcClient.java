import java.io.*;
import java.net.*;

class CalcClient {
	public static void main(String args[]) {
		// 引数チェック
		if (args.length < 2) {
			System.out.println("usage: java CalcClient address port");
			return;
		}

		try {
			// キーボード読み込み
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("円または長方形の面積を求めます");
			System.out.print("circle または rectangle と入力してください: ");
			String figure = br.readLine();

			// ソケットを作り,指定したサーバに接続する
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			Socket s = new Socket(server, port);
			
			// 入出力ストリームを得る
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			DataInputStream in = new DataInputStream(is);
			DataOutputStream out = new DataOutputStream(os);

			if (figure.equals("circle")) {
				out.writeUTF(figure);
				
				System.out.print("半径を入力してください: ");
				double radius = Double.parseDouble(br.readLine());

				// 送信
				out.writeDouble(radius);
				// 受信
				String reply = in.readUTF();
				System.out.println(reply);
				double circleArea = in.readDouble();
				System.out.println(circleArea);
			}
			else if (figure.equals("rectangle")) {
				out.writeUTF(figure);
				
				System.out.print("底辺を入力してください: ");
				double base = Double.parseDouble(br.readLine());
				System.out.print("高さを入力してください: ");
				double height = Double.parseDouble(br.readLine());

				// 送信
				out.writeDouble(base);
				out.writeDouble(height);
				// 受信
				String reply = in.readUTF();
				System.out.println(reply);
				double rectArea = in.readDouble();
				System.out.println(rectArea);
			}
			else {
				System.out.println("error. circle または rectangle と入力してください");
			}

			// IOストリームをクローズ
			in.close();
			out.close();
			
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
