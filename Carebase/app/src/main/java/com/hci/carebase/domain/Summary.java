package com.hci.carebase.domain;

import java.io.Serializable;
import java.util.Date;

public class Summary implements Serializable {
    private String summaryTitle;
    private String description;
    private Date dateIssued;

    // Required for DataSnapshot.getValue(//.class)
    public Summary() {
    }

    public Summary(String summary, String description, Date dateIssued) {
        this.summaryTitle = summary;
        this.description = description;
        this.dateIssued = dateIssued;
    }

    public String getSummaryTitle() {
        return summaryTitle;
    }

    public void setSummaryTitle(String summaryTitle) {
        this.summaryTitle = summaryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "summaryTitle='" + summaryTitle + '\'' +
                ", description='" + description + '\'' +
                ", dateIssued=" + dateIssued +
                '}';
    }
}
