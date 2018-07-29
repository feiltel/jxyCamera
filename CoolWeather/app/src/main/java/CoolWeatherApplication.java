import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2018/7/26.
 */

public class CoolWeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
