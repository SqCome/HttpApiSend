package com.lsq.mapper;

import com.lsq.entity.*;
import com.lsq.utils.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;


import java.util.List;

public class AgvMapperTest {
    @Test
    public void test(){
//        获得session对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

//      方法一：getMapper
        MapMapper mapper = sqlSession.getMapper(MapMapper.class);
        MapClass map = mapper.getMap();
        System.out.println(map);
//        关闭sqlSession
        sqlSession.close();
    }


    @Test
    public void testRegisterAgv(){
        String agvListJson = Agv2Json.getAgvJson();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.doPost("http://172.31.238.237:30416/DriveUnitSimulation/RegisteredAgv",agvListJson);
//        httpUtils.doPost("http://172.31.238.237:30416/DriveUnitSimulation/RegisteredAgv","[\"CARRIER_192168001012\"]");
    }

    @Test
    public void testUnregisterAgv(){
        String agvListJson = Agv2Json.getAgvJson();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.doPost("http://172.31.238.230:30416/DriveUnitSimulation/UnregisteredAgv",agvListJson);
//        httpUtils.doPost("http://172.31.238.230:30161/DriveUnitSimulation/UnregisteredAgv","[\"CARRIER_192168001019\"]");
    }

    @Test
    public void testQueryBucket(){
        List<Bucket> buckets = BucketIn.getBucket(1, 1);
        for (Bucket bucket : buckets) {
            System.out.println(bucket);
        }

        List<Bucket> buckets1 = BucketIn.getBucket(2, 1);
        for (Bucket bucket : buckets1) {
            System.out.println(bucket);
        }

    }

    @Test
    public void testBucketIn(){
        BucketIn.buckerIn(1,"http://172.31.238.16:30577/api/wcs/standardized/bucket/in");
    }

    @Test
    public void testBucketMove(){
//         BucketMoveJob.sendBucketMoveJob(2,1,"http://172.31.236.88:8071/api/wcs/standardized/robot/job/submit");
         BucketMoveJob.sendBucketMoveJob(1,1,"http://172.31.238.229:30955/api/wcs/standardized/robot/job/submit");
    }

    @Test
    public void testOperaate(){
        SendOperate.sendOperate(1,"http://172.31.238.229:30955/api/wcs/standardized/operation/notice");
    }
}
