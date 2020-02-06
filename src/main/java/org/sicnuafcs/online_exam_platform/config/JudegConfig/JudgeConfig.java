package org.sicnuafcs.online_exam_platform.config.JudegConfig;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/6 18:29
 */

@Configuration
public class JudgeConfig {

    final static String[] DefaultEnv = {"LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8"};

    public interface LangConfig {
        CompileConfig getCompileConfig();
        RunConfig getRunConfig();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class SPJConfig {
        String ExeName;
        String Command;
        String SeccompRule;
    }


    @Getter
    public static class ClangConfig implements LangConfig {
        private CompileConfig compileConfig ;
        private RunConfig runConfig;

        public ClangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .SrcName("main.c")
                    .ExeName("main")
                    .MaxCpuTime(3000)
                    .MaxRealTime(5000)
                    .MaxMemory(128 * 1024 * 1024)
                    .CompileCommand("/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .Command("{exe_path}")
                    .SeccompRule("c_cpp")
                    .Env(DefaultEnv)
                    .build();
        }
    }


    @Getter
    public static class CPPLangConfig implements LangConfig {
        private CompileConfig compileConfig;
        private RunConfig runConfig;

        public CPPLangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .SrcName("main.cpp")
                    .ExeName("main")
                    .MaxCpuTime(3000)
                    .MaxRealTime(5000)
                    .MaxMemory(128 * 1024 * 1024)
                    .CompileCommand("/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .Command("{exe_path}")
                    .SeccompRule("c_cpp")
                    .Env(DefaultEnv)
                    .build();
        }
    }


    @Getter
    public static class JavaLangConfig implements LangConfig {
        private CompileConfig compileConfig;
        private RunConfig runConfig;

        public JavaLangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .SrcName("Main.java")
                    .ExeName("Main")
                    .MaxCpuTime(3000)
                    .MaxRealTime(5000)
                    .MaxMemory(-1)
                    .CompileCommand("/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .Command("/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main")
                    .SeccompRule("")
                    .Env(DefaultEnv)
                    .MemoryLimitCheckOnly(1)
                    .build();
        }
    }


    @Getter
    public static class PY2LangConfig implements LangConfig {
        private CompileConfig compileConfig;
        private RunConfig runConfig;

        public PY2LangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .SrcName("solution.py")
                    .ExeName("solution.pyc")
                    .MaxCpuTime(3000)
                    .MaxRealTime(5000)
                    .MaxMemory(128 * 1024 * 1024)
                    .CompileCommand("/usr/bin/python -m py_compile {src_path}")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .Command("/usr/bin/python {exe_path}")
                    .SeccompRule("general")
                    .Env(DefaultEnv)
                    .build();
        }
    }

    final static String[] PY3Env = {"LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8", "PYTHONIOENCODING=UTF-8"};

    @Getter
    public static class PY3LangConfig implements LangConfig{
        private CompileConfig compileConfig;
        private RunConfig runConfig;

        public PY3LangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .SrcName("solution.py")
                    .ExeName("__pycache__/solution.cpython-35.pyc")
                    .MaxCpuTime(3000)
                    .MaxRealTime(5000)
                    .MaxMemory(128 * 1024 * 1024)
                    .CompileCommand("/usr/bin/python3 -m py_compile {src_path}")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .Command("/usr/bin/python3 {exe_path}")
                    .SeccompRule("general")
                    .Env(PY3Env)
                    .build();
        }

    }

}
