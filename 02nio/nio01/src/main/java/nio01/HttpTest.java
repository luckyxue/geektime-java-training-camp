package nio01;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpTest {
    public void doGetTest() {
        // Http 客户端
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = builder.build();
        // URL
        HttpGet get = new HttpGet("http://localhost:8008/test");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            System.out.println("\n\nStatus: " + response.getStatusLine());
            System.out.println("Content-Type: " + entity.getContentType());
            System.out.println("Content-Length: " + entity.getContentLength());
            System.out.println("Content: " + EntityUtils.toString(entity));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HttpTest test = new HttpTest();
        while (true) {
            test.doGetTest();
            Thread.sleep(1000);
        }
    }
}