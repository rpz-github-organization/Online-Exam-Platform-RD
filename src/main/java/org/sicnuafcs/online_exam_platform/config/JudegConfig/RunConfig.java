package org.sicnuafcs.online_exam_platform.config.JudegConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/6 20:38
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunConfig {
    String Command;
    String SeccompRule;
    String[] Env;
    int MemoryLimitCheckOnly;
}
