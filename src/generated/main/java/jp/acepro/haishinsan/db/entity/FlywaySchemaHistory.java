/*
 * Copyright 2019 SparkWorks Co.,Ltd.
 * All rights reserved.
 */

package jp.acepro.haishinsan.db.entity;

import java.time.LocalDateTime;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * 
 */
@Entity
@Table(name = "flyway_schema_history")
public class FlywaySchemaHistory extends BaseEntity {

    /**  */
    @Id
    @Column(name = "installed_rank")
    Integer installedRank;

    /**  */
    @Column(name = "version")
    String version;

    /**  */
    @Column(name = "description")
    String description;

    /**  */
    @Column(name = "type")
    String type;

    /**  */
    @Column(name = "script")
    String script;

    /**  */
    @Column(name = "checksum")
    Integer checksum;

    /**  */
    @Column(name = "installed_by")
    String installedBy;

    /**  */
    @Column(name = "installed_on")
    LocalDateTime installedOn;

    /**  */
    @Column(name = "execution_time")
    Integer executionTime;

    /**  */
    @Column(name = "success")
    Boolean success;

    /** 
     * Returns the installedRank.
     * 
     * @return the installedRank
     */
    public Integer getInstalledRank() {
        return installedRank;
    }

    /** 
     * Sets the installedRank.
     * 
     * @param installedRank the installedRank
     */
    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    /** 
     * Returns the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /** 
     * Sets the version.
     * 
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /** 
     * Returns the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /** 
     * Sets the description.
     * 
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * Returns the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /** 
     * Sets the type.
     * 
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /** 
     * Returns the script.
     * 
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /** 
     * Sets the script.
     * 
     * @param script the script
     */
    public void setScript(String script) {
        this.script = script;
    }

    /** 
     * Returns the checksum.
     * 
     * @return the checksum
     */
    public Integer getChecksum() {
        return checksum;
    }

    /** 
     * Sets the checksum.
     * 
     * @param checksum the checksum
     */
    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    /** 
     * Returns the installedBy.
     * 
     * @return the installedBy
     */
    public String getInstalledBy() {
        return installedBy;
    }

    /** 
     * Sets the installedBy.
     * 
     * @param installedBy the installedBy
     */
    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    /** 
     * Returns the installedOn.
     * 
     * @return the installedOn
     */
    public LocalDateTime getInstalledOn() {
        return installedOn;
    }

    /** 
     * Sets the installedOn.
     * 
     * @param installedOn the installedOn
     */
    public void setInstalledOn(LocalDateTime installedOn) {
        this.installedOn = installedOn;
    }

    /** 
     * Returns the executionTime.
     * 
     * @return the executionTime
     */
    public Integer getExecutionTime() {
        return executionTime;
    }

    /** 
     * Sets the executionTime.
     * 
     * @param executionTime the executionTime
     */
    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    /** 
     * Returns the success.
     * 
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /** 
     * Sets the success.
     * 
     * @param success the success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}