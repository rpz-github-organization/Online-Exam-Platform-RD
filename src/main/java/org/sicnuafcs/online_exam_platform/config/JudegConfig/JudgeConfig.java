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
        private CompileConfig compile ;
        private RunConfig run;
        public ClangConfig() {
            this.compile = new CompileConfig().builder()
                    .src_name("main.c")
                    .exe_name("main")
                    .max_cpu_time(3000)
                    .max_real_time(5000)
                    .max_memory(128 * 1024 * 1024)
                    .compile_command("/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}")
                    .build();

            this.run = new RunConfig().builder()
                    .command("{exe_path}")
                    .seccomp_rule("c_cpp")
                    .env(DefaultEnv)
                    .build();
        }

    }


    @Getter
    public static class CPPLangConfig implements LangConfig {
        private CompileConfig compile;
        private RunConfig run;
        private String name;

        public CPPLangConfig() {
            this.compile = new CompileConfig().builder()
                    .src_name("main.cpp")
                    .exe_name("main")
                    .max_cpu_time(3000)
                    .max_real_time(5000)
                    .max_memory(128 * 1024 * 1024)
                    .compile_command("/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}")
                    .build();

            this.run = new RunConfig().builder()
                    .command("{exe_path}")
                    .seccomp_rule("c_cpp")
                    .env(DefaultEnv)
                    .build();
            this.name = "cpp";
        }
    }


    @Getter
    public static class JavaLangConfig implements LangConfig {
        private CompileConfig compile;
        private RunConfig run;
        private String name;

        public JavaLangConfig() {
            this.compile = new CompileConfig().builder()
                    .src_name("Main.java")
                    .exe_name("Main")
                    .max_cpu_time(3000)
                    .max_real_time(5000)
                    .max_memory(-1)
                    .compile_command("/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8")
                    .build();

            this.run = new RunConfig().builder()
                    .command("/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main")
                    .seccomp_rule("")
                    .env(DefaultEnv)
                    .memory_limit_check_only(1)
                    .build();
            this.name = "java";
        }
    }


    @Getter
    public static class PY2LangConfig implements LangConfig {
        private CompileConfig compileConfig;
        private RunConfig runConfig;

        public PY2LangConfig() {
            this.compileConfig = new CompileConfig().builder()
                    .src_name("solution.py")
                    .exe_name("solution.pyc")
                    .max_cpu_time(3000)
                    .max_real_time(5000)
                    .max_memory(128 * 1024 * 1024)
                    .compile_command("/usr/bin/python -m py_compile {src_path}")
                    .build();

            this.runConfig = new RunConfig().builder()
                    .command("/usr/bin/python {exe_path}")
                    .seccomp_rule("general")
                    .env(DefaultEnv)
                    .build();
        }
    }

    final static String[] PY3Env = {"LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8", "PYTHONIOENCODING=UTF-8"};

    @Getter
    public static class PY3LangConfig implements LangConfig{
        private CompileConfig compile;
        private RunConfig run;

        public PY3LangConfig() {
            this.compile = new CompileConfig().builder()
                    .src_name("solution.py")
                    .exe_name("__pycache__/solution.cpython-35.pyc")
                    .max_cpu_time(3000)
                    .max_real_time(5000)
                    .max_memory(128 * 1024 * 1024)
                    .compile_command("/usr/bin/python3 -m py_compile {src_path}")
                    .build();

            this.run= new RunConfig().builder()
                    .command("/usr/bin/python3 {exe_path}")
                    .seccomp_rule("general")
                    .env(PY3Env)
                    .build();
        }
    }

}
