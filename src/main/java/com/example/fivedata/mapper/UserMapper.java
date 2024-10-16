package com.example.fivedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.fivedata.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Zing
 * @Date: 2024/10/15/17:43
 * @Description:
 */
@Mapper

@Repository
public interface UserMapper extends BaseMapper<User> {

}
