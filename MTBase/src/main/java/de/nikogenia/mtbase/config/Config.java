package de.nikogenia.mtbase.config;

import de.nikogenia.mtbase.utils.FileConfig;

public class Config extends FileConfig {

    private SQLConfig sql = new SQLConfig();
    private APIConfig api = new APIConfig();
    private boolean debug = false;
    private String name = "base";

    public SQLConfig getSql() {
        return sql;
    }

    public void setSql(SQLConfig sql) {
        this.sql = sql;
    }

    public APIConfig getApi() {
        return api;
    }

    public void setApi(APIConfig api) {
        this.api = api;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
