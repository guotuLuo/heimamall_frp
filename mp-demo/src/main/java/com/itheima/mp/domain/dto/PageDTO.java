package com.itheima.mp.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class PageDTO<V> {
//    private Long total;
//    private Long pages;
//    private List<V> list;
//
//    /**
//     * 返回空分页结果
//     * @param p MybatisPlus的分页结果
//     * @param <V> 目标VO类型
//     * @param <P> 原始PO类型
//     * @return VO的分页对象
//     */
//    public static <V, P> PageDTO<V> empty(Page<P> p){
//        return new PageDTO<>(p.getTotal(), p.getPages(), Collections.emptyList());
//    }
//
//    /**
//     * 将MybatisPlus分页结果转为 VO分页结果
//     * @param p MybatisPlus的分页结果
//     * @param voClass 目标VO类型的字节码
//     * @param <V> 目标VO类型
//     * @param <P> 原始PO类型
//     * @return VO的分页对象
//     */
//    public static <V, P> PageDTO<V> of(Page<P> p, Class<V> voClass) {
//        // 1.非空校验
//        List<P> records = p.getRecords();
//        if (records == null || records.size() <= 0) {
//            // 无数据，返回空结果
//            return empty(p);
//        }
//        // 2.数据转换
//        List<V> vos = BeanUtil.copyToList(records, voClass);
//        // 3.封装返回
//        return new PageDTO<>(p.getTotal(), p.getPages(), vos);
//    }
//
//    /**
//     * 将MybatisPlus分页结果转为 VO分页结果，允许用户自定义PO到VO的转换方式
//     * @param p MybatisPlus的分页结果
//     * @param convertor PO到VO的转换函数
//     * @param <V> 目标VO类型
//     * @param <P> 原始PO类型
//     * @return VO的分页对象
//     */
//    public static <V, P> PageDTO<V> of(Page<P> p, Function<P, V> convertor) {
//        // 1.非空校验
//        List<P> records = p.getRecords();
//        if (records == null || records.size() <= 0) {
//            // 无数据，返回空结果
//            return empty(p);
//        }
//        // 2.数据转换
//        List<V> vos = records.stream().map(convertor).collect(Collectors.toList());
//        // 3.封装返回
//        return new PageDTO<>(p.getTotal(), p.getPages(), vos);
//    }
//}

@Data
@ApiModel
public class PageDTO <T>{
    private Long total;
    private Long pages;
    private List list;


    // 泛型传入User 实体，返回UserVO 实体
    public static <VO, PO> PageDTO<VO> of(Page<PO> p, Class<VO> clazz){
        // 构建PageDTO
        PageDTO<VO> pageDTO = new PageDTO<>();
        pageDTO.setPages(p.getPages());
        pageDTO.setTotal(p.getTotal());

        // 判断分页查询状态
        if(p.getRecords() == null){
            pageDTO.setList(Collections.emptyList());
            return pageDTO;
        }
        // 由于泛型是一个指代符，并不能直接获取泛型的class, 所以需要手动传入VOd的字节码
        // 静态方法在类构建之间就可以调用，所以静态方法的泛型需要在静态方法上创建，而不能直接使用类的泛型来作为静态方法的泛型
        // pageDTO.setList(BeanUtil.copyToList(p.getRecords(), VO.class));
        pageDTO.setList(BeanUtil.copyToList(p.getRecords(), clazz));
        return pageDTO;
    }

    // 泛型传入User 实体，返回UserVO 实体
    public static <VO, PO> PageDTO<VO> of(Page<PO> p, Function<PO, VO> convertor){
        // 构建PageDTO
        PageDTO<VO> pageDTO = new PageDTO<>();
        pageDTO.setPages(p.getPages());
        pageDTO.setTotal(p.getTotal());

        // 判断分页查询状态
        if(p.getRecords() == null){
            pageDTO.setList(Collections.emptyList());
            return pageDTO;
        }

        // 从PO到VO的实例转换过程交给convertor来做
        List<PO> records = p.getRecords();
        pageDTO.setList(records.stream().map(convertor).collect(Collectors.toList()));
        return pageDTO;
    }

}
