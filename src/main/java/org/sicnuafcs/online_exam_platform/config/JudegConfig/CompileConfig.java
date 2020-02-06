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
    String SrcName;

    String ExeName;

    long MaxCpuTime;

    long MaxRealTime;

    long MaxMemory;

    String CompileCommand;

}
