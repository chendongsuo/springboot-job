package com.cds.job.mapper;

import com.cds.job.domain.MyJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author cds
 * @date 2021/12/27 4:12 下午
 */
@Repository
@Mapper
public interface MyJobMapper {

    /**
     * insert
     * @param myJob
     * @return
     */
    int insert(MyJob myJob);

    /**
     * update
     * @param myJob
     * @return
     */
    int update(MyJob myJob);

    /**
     * selectById
     * @param id
     * @return
     */
    MyJob selectById(Long id);

    /**
     * delete
     * @param id
     */
    void deleteById(Long id);
}
