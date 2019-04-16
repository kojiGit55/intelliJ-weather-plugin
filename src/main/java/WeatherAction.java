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
        String cityName = Messages.showInputDialog(project, "Where is your location?\n\nExample: Tokyo", "Input your location", null);

        OkHttpClient client = new OkHttpClient();
        String apiKey = "";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    Messages.showMessageDialog(project, "city not found", "Current Weather", null);
                } else {
                    Messages.showMessageDialog(project, "error!!", "Current Weather", null);
                }
                return;
            }

            ResponseBody body = response.body();
            if (body != null) {
                String bodyString = body.string();
                JSONObject jsonObj = new JSONObject(bodyString);
                JSONArray w = jsonObj.getJSONArray("weather");
                String description = w.getJSONObject(0).getString("description");

                Messages.showMessageDialog(project, description, "Current Weather", null);
            }
        } catch (IOException e) {
            Messages.showMessageDialog(project, e.getMessage(), "Current Weather", null);
        }
    }
}