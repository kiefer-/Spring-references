package me.shouheng.service.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.shouheng.common.model.AbstractVO;

import java.util.Date;

/**
 * @author shouh, 2019/3/31-11:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
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

}
