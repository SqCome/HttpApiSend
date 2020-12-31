package com.lsq.utils;

import com.alibaba.fastjson.JSON;
import com.lsq.entity.Bucket;
import com.lsq.entity.BucketInJob;
import com.lsq.entity.Point;
import com.lsq.mapper.BucketMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class BucketIn {
    /**
     *
     * @param type  用哪个数据库的内容,1表示用devops，2表示用虚拟机
     * @param bucket_state
     * @return
     */
    public static List<Bucket> getBucket(int type,int bucket_state){
        SqlSession sqlSession = null;
        List<Bucket> bucketList ;
        if (type ==1){
            sqlSession = MybatisUtils.getSqlSession2();
        }else if (type == 2){
            sqlSession = MybatisUtils.getSqlSession4();
        }
        BucketMapper mapper = sqlSession.getMapper(BucketMapper.class);
        if (bucket_state == 1){
            bucketList = mapper.getBucketInList();
        }else bucketList = mapper.getBucketOutList();

        sqlSession.close();
        return bucketList;
    }

    /**
     *
     * @param type
     */

    public static void buckerIn(int type,String url){
        List<Point> points = GetPoint.getPoints(type);
        List<Point> storage = GetPoint.getPointsByType(points, "STORAGE");
        List<Bucket> bucketList = BucketIn.getBucket(type,2);
        if (storage == null || bucketList ==null){
            System.out.println("不存在货架储存点或可用的货架");
        }else {
            for (int i = 0; i < storage.size() && i<bucketList.size(); i++) {
                System.out.println(1);
                BucketInJob bucketInJob = new BucketInJob();
                bucketInJob.setBucketCode(bucketList.get(i).getBucket_code());
                bucketInJob.setPointCode(storage.get(i).getPoint_code());
                String bukeetInJson = JSON.toJSONString(bucketInJob);
                HttpUtils.doPost(url,bukeetInJson);
            }
        }
    }
}
