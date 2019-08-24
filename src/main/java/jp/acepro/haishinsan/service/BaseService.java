package jp.acepro.haishinsan.service;

import java.io.IOException;

import org.springframework.http.HttpMethod;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public abstract class BaseService {

	/**
	 * WebAPIを呼ぶ共通メソッド
	 * 
	 * @param resource             webapiメソッドのURL("/goods","/addresses",など)
	 * 
	 * @param httpMethod           Httpのメソッド(GET,POST,PUT,DELETEなど)
	 * 
	 * @param requestObjec         リクエストの対象設定(POSTとPUTの場合必須)
	 * 
	 * @param twitterAuthorization リクエストの対象設定(POSTとPUTの場合必須、Twitterのみ)
	 * 
	 * @param responseClass        リターンクラスのタイプ
	 * @throws Exception
	 * 
	 */
	protected <T> T call(String resource, HttpMethod httpMethod, Object requestObjec, String twitterAuthorization,
			Class<T> responseClass) throws Exception {

		Request request = null;
		Request.Builder builder = new Request.Builder().url(resource);
		if (twitterAuthorization != null) {
			builder.addHeader("Authorization", twitterAuthorization);
		}

		RequestBody body = null;
		Gson gson = new Gson();
		if (requestObjec != null) {
			body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestObjec));
			// System.out.println("body : " + gson.toJson(requestObjec));
		} else {
			body = RequestBody.create(MediaType.parse("application/json"), gson.toJson(null));
		}

		switch (httpMethod) {
		case GET:
			builder.get();
			break;

		case POST:
			builder.post(body);
			break;

		case PUT:
			builder.put(body);
			break;

		case DELETE:
			builder.delete();
			break;

		default:
			throw new Exception();
		}

		request = builder.build();
		OkHttpClient client = new OkHttpClient();

		try {

			log.debug("-------------WEBAPIのREQUEST-----------");
			log.debug(request.toString());
			log.debug("----------------------------------------");

			Response response = client.newCall(request).execute();

			String bodyString = response.body().string();
			log.debug("-------------WEBAPIのRESPONSE-----------");
			log.debug(bodyString);
			log.debug("----------------------------------------");

			return gson.fromJson(bodyString, responseClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

}
