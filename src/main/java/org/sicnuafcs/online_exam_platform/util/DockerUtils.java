package org.sicnuafcs.online_exam_platform.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * <p>Description:  Some utils for operate docker</p>
 *
 * @author Hobert
 * @version 1.0
 * @create 2020/2/3 17:33
 */

public class DockerUtils {

    private static DockerClient dockerClient;

    static {

        //获取端口配置文件 try-with-resources
        Properties properties = new Properties();
        try(InputStream inputStream = DockerUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //证书路径
        String DockerCertPath = DockerUtils.class.getClassLoader().getResource("dockertls").getPath();
        if (System.getProperties().getProperty("os.name").contains("Windows")) {
            DockerCertPath = DockerCertPath.substring(1);
        }

        // 添加配置
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(properties.getProperty("DOCKER_HOST"))
                .withDockerTlsVerify(true)
                // 证书的本地位置
                .withDockerCertPath(DockerCertPath)
                .build();



        // docker命令执行工厂
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(60000)
                .withConnectTimeout(60000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);

        dockerClient = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();
    }

    public static void main(String[] args) {
        System.out.println(System.getProperties().getProperty("os.name"));
    }

    /**
     * 查询连接信息
     *
     * @return
     */
    public static Info queryClientInfo() {
        return dockerClient.infoCmd().exec();
    }

    /**
     * 查看镜像
     *
     * @return List<Image>
     */
    public static List<Image> queryImagesList() {
        return dockerClient.listImagesCmd().exec();
    }

    /**
     * 停止容器
     *
     * @param container    容器ID
     * @return Object
     */
    public static Object stopContainer(String container) {
        return dockerClient.stopContainerCmd(container).exec();
    }

    /**
     * 删除镜像
     *
     * @param imagesID     镜像ID
     * @return Object
     */
    public static Object removeImages(String imagesID) {
        return dockerClient.removeImageCmd(imagesID).exec();
    }

    /**
     * 删除容器
     *
     * @param container    容器ID
     * @return Object
     */
    public static Object removeContainer(String container) {
        return dockerClient.removeContainerCmd(container).exec();
    }

    /**
     * 创建容器
     *
     * @param imagesID     镜像ID
     * @return Object
     */
    public static Object createContainer(String imagesID) {
        return dockerClient.createContainerCmd(imagesID).exec();
    }

//    /**
//     * 创建一个镜像
//     *
//     * @return Object
//     * @throws FileNotFoundException 找不到文件
//     */
//    public static Object createImages(String path) throws FileNotFoundException {
//        //仓库地址
//        String repository = "";
//        //镜像文件流
//        InputStream imageStream = new FileInputStream(path);
//        return dockerClient.createImageCmd(repository, imageStream).exec();
//    }

    /**
     * 容器列表(运行中的)
     *
     * @return Object
     */
    public static List<Container> listContainersCmd() {
        return dockerClient.listContainersCmd().exec();
    }


    public static List<String> getContainerNameList(List<Container> containerList) {
        List<String> containerNameList = new ArrayList<>();
        for (Container container : containerList) {
            String containerName = container.getNames()[0].replace("/", "");
            containerNameList.add(containerName);
        }
        return containerNameList;
    }

    /**
     * 启动容器
     *
     * @param containerID  容器ID
     */
    public static Object startContainerCmd(String containerID) {
        return dockerClient.startContainerCmd(containerID).exec();
    }

    /**
     * 重启容器
     *
     * @param containerID  容器id
     * @return Object
     */
    public static Object restartContainerCmd(String containerID) {
        return dockerClient.restartContainerCmd(containerID).exec();
    }

    /**
     * 从本地上传资源到容器
     *
     * @param containerID  容器id
     * @param resource     本地资源路径
     * @param remote       服务器资源路径
     * @return Object
     */
    public static Object copyArchiveToContainerCmd(String containerID, String resource, String remote) {
        return dockerClient.copyArchiveToContainerCmd(containerID).withHostResource(resource).withRemotePath(remote).exec();
    }

    /**
     *
     * @param containerID  容器id
     * @param local        本地路径
     * @param remote       远程容器路径
     * @return boolean 下载完成返回成功
     */
    public static boolean copyArchiveFromContainerCmd(String containerID, String local, String remote) {
        /*
        * 地址示例
        * String path = "F:\\tmp\\wealth.rar"
        * remote="/tmp/wealth.rar"
        */

        try ( InputStream input = dockerClient
                .copyArchiveFromContainerCmd(containerID, remote)
                .exec();
              FileOutputStream downloadFile = new FileOutputStream(local);
        ) {
            int index;
            byte[] bytes = new byte[1024];

            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据容器名获取容器ID
     *
     * @param containerName 容器名
     * @return String
     */
    public static String getContainerIdByName(String containerName) {
        try {
            String containerId = "";
            Object object = DockerUtils.listContainersCmd();
            JSONArray jsonArray = new JSONArray(object);
            for (int i = 0; i < jsonArray.length(); i++) {
                String name = jsonArray.getJSONObject(i).getString("names");
                name = name.replace("[\"/", "").replace("\"]", "");
                if (!StringUtils.isEmpty(name) && name.equals(containerName)) {
                    containerId = jsonArray.getJSONObject(i).getString("id");
                }
            }
            return containerId;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
