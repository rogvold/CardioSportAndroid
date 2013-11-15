package com.cardiomood.sport.android.client;

import android.content.Context;
import com.cardiomood.sport.android.tools.config.ConfigurationManager;
import com.cardiomood.sport.android.tools.config.PreferenceHelper;
import org.codegist.crest.CRestBuilder;

/**
 * Project: CardioSport
 * User: danon
 * Date: 15.06.13
 * Time: 14:02
 */
public class CardioSportService {

    public static ICardioSportService rebuildService(Context ctx) {
        ConfigurationManager config = ConfigurationManager.getInstance();
        PreferenceHelper pref = new PreferenceHelper(ctx);
        config.setString(ConfigurationManager.SERVICE_PROTOCOL, pref.getString(ConfigurationManager.SERVICE_PROTOCOL, ConfigurationManager.DEFAULT_SERVICE_PROTOCOL));
        config.setString(ConfigurationManager.SERVICE_HOST, pref.getString(ConfigurationManager.SERVICE_HOST, ConfigurationManager.DEFAULT_SERVICE_HOST));
        config.setString(ConfigurationManager.SERVICE_PORT, pref.getString(ConfigurationManager.SERVICE_PORT, ConfigurationManager.DEFAULT_SERVICE_PORT));
        config.setString(ConfigurationManager.SERVICE_PATH, pref.getString(ConfigurationManager.SERVICE_PATH, ConfigurationManager.DEFAULT_SERVICE_PATH));
        return Holder.instance = Holder.buildService();
    }

    private static class Holder {
        private static ICardioSportService instance = buildService();

        private static ICardioSportService buildService() {
            ConfigurationManager conf = ConfigurationManager.getInstance();
            CRestBuilder builder = new CRestBuilder();
            builder.placeholder(ConfigurationManager.SERVICE_PROTOCOL, conf.getString(ConfigurationManager.SERVICE_PROTOCOL, ConfigurationManager.DEFAULT_SERVICE_PROTOCOL));
            builder.placeholder(ConfigurationManager.SERVICE_HOST, conf.getString(ConfigurationManager.SERVICE_HOST, ConfigurationManager.DEFAULT_SERVICE_HOST));
            builder.placeholder(ConfigurationManager.SERVICE_PORT, conf.getString(ConfigurationManager.SERVICE_PORT, ConfigurationManager.DEFAULT_SERVICE_PORT));
            builder.placeholder(ConfigurationManager.SERVICE_PATH, conf.getString(ConfigurationManager.SERVICE_PATH, ConfigurationManager.DEFAULT_SERVICE_PATH));
            return builder.build().build(ICardioSportService.class);
        }
    }

    public static ICardioSportService getInstance() {
        return Holder.instance;
    }



}
