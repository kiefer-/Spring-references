package me.shouheng.service.model.po;

import me.shouheng.common.model.AbstractPO;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author shouh, 2019/3/31-11:40
 */
@Table(name = "gt_task")
public class Task extends AbstractPO {

    @Column(name = "category_code")
    private Long categoryCode;

    @Column(name = "class_code")
    private Long classCode;

    @Column(name = "purpose_code")
    private Long purposeCode;

    @Column(name = "title")
    private String title;

    @Column(name = "comment")
    private String comment;

    @Column(name = "tags")
    private String tags;

    @Column(name = "started_time")
    private Date startedTime;

    @Column(name = "ended_time")
    private Date endedTime;

    @Column(name = "completed_time")
    private Date completedTime;

    @Column(name = "progress")
    private Short progress;

    @Column(name = "assignment_order")
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
