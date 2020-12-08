package br.com.alura.owasp.retrofit;

import java.io.IOException;
import org.springframework.stereotype.Component;
import retrofit2.Call;

@Component
public class GoogleWebClient {
	
	private static final String SECRET = "6LeUEsAZAAAAAMO6LJpXWO6EryIFuKR0OFfotqiB";

	public boolean verify(String recaptcha) throws IOException {
		Call<GoogleResponse> token = new RetrofitInitializer().getGoogleService().sendToken(SECRET, recaptcha);
		return token.execute().body().isSuccess();
	}
}
