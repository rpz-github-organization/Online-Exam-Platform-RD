package org.sicnuafcs.online_exam_platform.config;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.model.Container;
import lombok.extern.slf4j.Slf4j;
import org.sicnuafcs.online_exam_platform.config.exception.CustomException;
import org.sicnuafcs.online_exam_platform.config.exception.CustomExceptionType;
import org.sicnuafcs.online_exam_platform.util.DockerUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * <p>Description:  xx</p>
 *
 * @author Hobert-Li
 * @version 1.0
 * @create 2020/2/17 18:17
 */

@Configuration
@EnableScheduling
public class JudgeServerBeatHeartConfig {

    @Scheduled(cron = "0/5 * * * * ?")
    public void beatHeart() {

        List<Container> containerList = DockerUtils.listContainersCmd();
        if (containerList == null || containerList.size() == 0) {
            throw new CustomException(CustomExceptionType.OTHER_ERROR, "JudgeServer出现异常，请联系管理员处理!");
        }

        boolean alive = false;
        for (Container container : containerList) {
            if ("registry.cn-hangzhou.aliyuncs.com/onlinejudge/judge_server".equals(container.getImage())) {
                alive = true;
            }
        }

        if (!alive) {
            throw new CustomException(CustomExceptionType.OTHER_ERROR, "JudgeServer出现异常，请联系管理员处理!");
        }
    }

//    private JSONObject request() throws RestClientException {
//        String url = "https://121.36.18.182:10085/ping";
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject jsonObject = new JSONObject();
//
//        LinkedMultiValueMap body=new LinkedMultiValueMap();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-Judge-Server-Token", "b82fd881d1303ba9794e19b7f4a5e2b79231d065f744e72172ad9ee792909126");
//        headers.set("Content-Type", "application/json");
//        HttpEntity httpEntity = new HttpEntity(body, headers);
//
//
//        ResponseEntity<String> strbody = null;
//        try {
//            strbody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
//        } catch (RestClientException e) {
//            throw new CustomException(CustomExceptionType.OTHER_ERROR, "JudgeServer出现异常，请联系管理员处理!");
//        }
//
//        JSONObject respjson = JSONObject.parseObject(strbody.getBody());
//        System.out.println(jsonObject.toJSONString());
//        return respjson;
//    }
}
