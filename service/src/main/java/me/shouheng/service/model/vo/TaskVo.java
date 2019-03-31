package me.shouheng.service.model.vo;

import me.shouheng.common.model.AbstractVO;

import java.util.Date;

/**
 * @author shouh, 2019/3/31-11:40
 */
public class TaskVo extends AbstractVO {

    private Long categoryCode;

    private Long classCode;

    private Long purposeCode;

    private String title;

    private String comment;

    private String tags;

    private Date startedTime;

    private Date endedTime;

    private Date completedTime;

    private Short progress;

    private Short assignmentOrder;

    public Long getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Long categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getClassCode() {
        return classCode;
    }

    public void setClassCode(Long classCode) {
        this.classCode = classCode;
    }

    public Long getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(Long purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public Date getEndedTime() {
        return endedTime;
    }

    public void setEndedTime(Date endedTime) {
        this.endedTime = endedTime;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public Short getProgress() {
        return progress;
    }

    public void setProgress(Short progress) {
        this.progress = progress;
    }

    public Short getAssignmentOrder() {
        return assignmentOrder;
    }

    public void setAssignmentOrder(Short assignmentOrder) {
        this.assignmentOrder = assignmentOrder;
    }
}
