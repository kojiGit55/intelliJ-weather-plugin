import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(
        name = "ConfigService",
        storages = {
            @Storage(StoragePathMacros.WORKSPACE_FILE),
        }
)

public class ConfigService implements PersistentStateComponent<ConfigService> {
    public String location;

    public ConfigService getState() {
        return this;
    }

    public void loadState(ConfigService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
