package com.rikenmaharjan.y2yc.utils;

/**
 * Created by bikenmaharjan on 6/2/18.
 */

public class WarningModel {


    private String warningDescription;
    private String warningType;
    private String warningDate;

    private String suspensionStart;
    private String suspensionEnd;

    // num
    private int majorWarning;
    private int minorWarning;
    private int suspensionWarning;


    public WarningModel() {

    }

    public WarningModel(String warningDescription, String warningType, String warningDate, String suspensionStart, String suspensionEnd, int majorWarning, int minorWarning, int suspensionWarning) {
        this.warningDescription = warningDescription;
        this.warningType = warningType;
        this.warningDate = warningDate;
        this.suspensionStart = suspensionStart;
        this.suspensionEnd = suspensionEnd;
        this.majorWarning = majorWarning;
        this.minorWarning = minorWarning;
        this.suspensionWarning = suspensionWarning;
    }

    //getter


    public String getWarningDescription() {
        return warningDescription;
    }

    public String getWarningType() {
        return warningType;
    }

    public String getWarningDate() {
        return warningDate;
    }

    public String getSuspensionStart() {
        return suspensionStart;
    }

    public String getSuspensionEnd() {
        return suspensionEnd;
    }

    public int getMajorWarning() {
        return majorWarning;
    }

    public int getMinorWarning() {
        return minorWarning;
    }

    public int getSuspensionWarning() {
        return suspensionWarning;
    }

    //setter

    public void setWarningDescription(String warningDescription) {
        this.warningDescription = warningDescription;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public void setWarningDate(String warningDate) {
        this.warningDate = warningDate;
    }

    public void setSuspensionStart(String suspensionStart) {
        this.suspensionStart = suspensionStart;
    }

    public void setSuspensionEnd(String suspensionEnd) {
        this.suspensionEnd = suspensionEnd;
    }

    public void setMajorWarning(int majorWarning) {
        this.majorWarning = majorWarning;
    }

    public void setMinorWarning(int minorWarning) {
        this.minorWarning = minorWarning;
    }

    public void setSuspensionWarning(int suspensionWarning) {
        this.suspensionWarning = suspensionWarning;
    }
}
