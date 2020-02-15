package org.sicnuafcs.online_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToTestCase {
    private String input;
    private String output;
}
