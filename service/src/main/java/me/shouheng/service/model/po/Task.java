package me.shouheng.service.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.AbstractPO;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author shouh, 2019/3/31-11:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
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

}
