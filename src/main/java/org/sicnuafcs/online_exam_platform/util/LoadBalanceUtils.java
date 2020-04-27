package org.sicnuafcs.online_exam_platform.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LoadBalanceUtils {
    private static Map<Integer, String> url_map;
    //统计各个服务历史判题数量
    private  static Map<Integer, Integer> count;
    private static ReentrantLock r = new ReentrantLock();
    static {
        //初始化操作
        url_map = new HashMap<>();
        count = new HashMap<>();
        url_map.put(0, "http://121.36.18.182:10085/judge");
        url_map.put(1, "http://121.36.18.182:10084/judge");
        url_map.put(2, "http://121.36.18.182:10083/judge");
        count.put(0, 0);
        count.put(1, 0);
        count.put(2, 0);
    }
    public static String GetUrl(Long id) {
        //权重数组
        Map<Integer,Integer> weights = new HashMap<>();
        weights.put(0, 0);
        weights.put(1, 0);
        weights.put(2, 0);
        Integer num = Integer.valueOf(id.toString()) % 3;
        if (weights.containsKey(num)) {
            weights.put(num, weights.get(num) + 10);
            weights.put(num, 10);
        }
        //获取历史推送最少的
        Integer min = getMinCount();
        if (weights.containsKey(min)) {
            weights.put(min, weights.get(min) + 15);
        } else {
            weights.put(min, 15);
        }
        Integer max = getMaxWeights(weights);
        //计数时加锁
        r.lock();
        count.put(max, count.get(max) + 1);
        r.unlock();
        return url_map.get(max);
    }
    private static Integer getMinCount() {
        System.out.println("count: " + count.toString());
        Integer ret = 0;
        Integer min = count.get(0);
        boolean isAllEqual = true;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (min != entry.getValue()) {
                isAllEqual = false;
            }
            if (entry.getValue() < min) {
                min = entry.getValue();
                ret = entry.getKey();
            }
        }
        if (isAllEqual) {
            Calendar cld = Calendar.getInstance();
            System.out.println("time: " + cld.get((Calendar.MILLISECOND)));
            return cld.get((Calendar.MILLISECOND)) % 3;
        }
        return ret;
    }
    private static Integer getMaxWeights(Map<Integer,Integer> weights) {
        Integer ret = 0;
        Integer max = weights.get(0);
        boolean isAllEqual = true;
        for (Map.Entry<Integer, Integer> entry : weights.entrySet()) {
            if (max != entry.getValue()) {
                isAllEqual = false;
            }
            if (entry.getValue() > max) {
                max = entry.getValue();
                ret = entry.getKey();
            }
        }
        if (isAllEqual) {
            return Calendar.MILLISECOND % 3;
        }
        return ret;
    }
}
