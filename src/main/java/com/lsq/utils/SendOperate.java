package com.lsq.utils;

import com.alibaba.fastjson.JSON;
import com.lsq.entity.BucketRobotJob;
import com.lsq.entity.OperateJob;
import com.lsq.mapper.OperateMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class SendOperate {
    /**
     *
     * @param type  选择数据库环境
     * @param url  下发任务接口地址
     */
    public static void sendOperate(int type,String url){
        int flag  = 0;

        while(true) {
            SqlSession sqlSession = null;
            if (type == 1){
                sqlSession = MybatisUtils.getSqlSession3();
            }else if (type ==2){
                sqlSession = MybatisUtils.getSqlSession6();
            }
            OperateMapper operateMapper = sqlSession.getMapper(OperateMapper.class);
            List<BucketRobotJob> bucketRobotJobs = operateMapper.getOperateList();
            if (bucketRobotJobs.size() == 0){
                try {
                    Thread.sleep(1000*10);
                    flag += 1;
                    if (flag<=3){
                        continue;
                    }else break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            flag = 0;
            OperateJob operateJob = new OperateJob();
            for (BucketRobotJob bucketRobotJob : bucketRobotJobs) {
                operateJob.setRobotJobId(bucketRobotJob.getRobot_job_id());
                operateJob.setWarehouseId(bucketRobotJob.getWarehouse_id());
                String operateJson = JSON.toJSONString(operateJob);
                System.out.println(operateJson);
                HttpUtils.doPost(url, operateJson);
            }
            try {
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
