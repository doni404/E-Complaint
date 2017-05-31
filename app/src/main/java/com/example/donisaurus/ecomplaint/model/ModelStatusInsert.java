package com.example.donisaurus.ecomplaint.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Donisaurus on 1/5/2017.
 */

public class ModelStatusInsert {

    @SerializedName("fieldCount")
    @Expose
    private Integer fieldCount;
    @SerializedName("affectedRows")
    @Expose
    private Integer affectedRows;
    @SerializedName("insertId")
    @Expose
    private Integer insertId;
    @SerializedName("serverStatus")
    @Expose
    private Integer serverStatus;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("protocol41")
    @Expose
    private Boolean protocol41;
    @SerializedName("changedRows")
    @Expose
    private Integer changedRows;

    /**
     *
     * @return
     *     The fieldCount
     */
    public Integer getFieldCount() {
        return fieldCount;
    }

    /**
     *
     * @param fieldCount
     *     The fieldCount
     */
    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }

    /**
     *
     * @return
     *     The affectedRows
     */
    public Integer getAffectedRows() {
        return affectedRows;
    }

    /**
     *
     * @param affectedRows
     *     The affectedRows
     */
    public void setAffectedRows(Integer affectedRows) {
        this.affectedRows = affectedRows;
    }

    /**
     *
     * @return
     *     The insertId
     */
    public Integer getInsertId() {
        return insertId;
    }

    /**
     *
     * @param insertId
     *     The insertId
     */
    public void setInsertId(Integer insertId) {
        this.insertId = insertId;
    }

    /**
     *
     * @return
     *     The serverStatus
     */
    public Integer getServerStatus() {
        return serverStatus;
    }

    /**
     *
     * @param serverStatus
     *     The serverStatus
     */
    public void setServerStatus(Integer serverStatus) {
        this.serverStatus = serverStatus;
    }

    /**
     *
     * @return
     *     The warningCount
     */
    public Integer getWarningCount() {
        return warningCount;
    }

    /**
     *
     * @param warningCount
     *     The warningCount
     */
    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     *     The protocol41
     */
    public Boolean getProtocol41() {
        return protocol41;
    }

    /**
     *
     * @param protocol41
     *     The protocol41
     */
    public void setProtocol41(Boolean protocol41) {
        this.protocol41 = protocol41;
    }

    /**
     *
     * @return
     *     The changedRows
     */
    public Integer getChangedRows() {
        return changedRows;
    }

    /**
     *
     * @param changedRows
     *     The changedRows
     */
    public void setChangedRows(Integer changedRows) {
        this.changedRows = changedRows;
    }

}

