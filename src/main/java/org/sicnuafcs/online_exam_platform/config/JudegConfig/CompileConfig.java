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
 * @create 2020/2/6 20:36
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompileConfig {
    String src_name;

    String exe_name;

    long max_cpu_time;

    long max_real_time;

    long max_memory;

    String compile_command;

}
