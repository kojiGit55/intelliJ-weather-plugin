import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
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
        ConfigService pc = ServiceManager.getService(ConfigService.class);
        if (pc.location == null) {
            String location = Messages.showInputDialog(project, "Where is your location?\nexample: Tokyo", "Input your location", Messages.getQuestionIcon());
            pc.location = location;
        }
        String cityName = pc.location;

        OkHttpClient client = new OkHttpClient();
        String apiKey = "";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    Messages.showMessageDialog(project, "city not found", "current weather", null);
                } else {
                    Messages.showMessageDialog(project, "error!!", "current weather", null);
                }
            }

            ResponseBody body = response.body();
            if (body != null) {
                String bodyString = body.string();
                JSONObject jsonObj = new JSONObject(bodyString);
                JSONArray w = jsonObj.getJSONArray("weather");
                String description = w.getJSONObject(0).getString("description");
//                String icon = w.getJSONObject(0).getString("icon");
//                ImageIcon imageIcon = new ImageIcon("http://openweathermap.org/img/w/" + icon + ".png");

                Messages.showMessageDialog(project, description, "current weather", null);
            }
        } catch (IOException e) {
            Messages.showMessageDialog(project, e.getMessage(), "current weather", null);
        }
    }
}