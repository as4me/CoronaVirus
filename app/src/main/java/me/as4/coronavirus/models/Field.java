
package me.as4.coronavirus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Field {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("sqlType")
    @Expose
    private String sqlType;
    @SerializedName("domain")
    @Expose
    private Object domain;
    @SerializedName("defaultValue")
    @Expose
    private Object defaultValue;
    @SerializedName("length")
    @Expose
    private Integer length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Object getDomain() {
        return domain;
    }

    public void setDomain(Object domain) {
        this.domain = domain;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
