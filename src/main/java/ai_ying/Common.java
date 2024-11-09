package ai_ying;

import java.io.InputStream;

import javax.servlet.ServletContext;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Common {
	// 標示FirebaseApp是否初始化完成
	private static boolean initialized = false;

	public static void initializeFirebaseApp(ServletContext context) {
		if (initialized) {
			return;
		}
		// 請自行建立Firebase專案並下載私密金鑰檔案，否則執行錯誤
		// 私密金鑰檔案可以儲存在專案以外
		// File file = new File("/path/to/firsebase-java-privateKey.json");
		// 私密金鑰檔案也可以儲存在專案內(web專案儲存在webapp目錄內)
		try (InputStream in = context.getResourceAsStream("/firebaseServerKey.json")) {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(in))
					.build();
			FirebaseApp.initializeApp(options);
			initialized = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
