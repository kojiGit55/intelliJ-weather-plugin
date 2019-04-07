import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class WeatherAction extends AnAction {
    public WeatherAction() {
        super("Weather");
    }

    public void actionPerformed(AnActionEvent event) {

        Project project = event.getProject();

        OkHttpClient client = new OkHttpClient();
        String apiKey = "";
        String cityName = "";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                Messages.showMessageDialog(project, "error!!", "外の天気", null);
            }

            ResponseBody body = response.body();
            if (body != null) {
                String bodyString = body.string();
                JSONObject jsonObj = new JSONObject(bodyString);
                JSONArray w = jsonObj.getJSONArray("weather");
                String description = w.getJSONObject(0).getString("description");
//                String icon = w.getJSONObject(0).getString("icon");
//                ImageIcon imageIcon = new ImageIcon("http://openweathermap.org/img/w/" + icon + ".png");

                Messages.showMessageDialog(project, description, "外の天気", null);
            }
        } catch (IOException e) {
            Messages.showMessageDialog(project, e.getMessage(), "外の天気", null);
        }
    }
}