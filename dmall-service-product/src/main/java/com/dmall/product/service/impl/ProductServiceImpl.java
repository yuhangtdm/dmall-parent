package com.dmall.product.service.impl;

import com.dmall.product.entity.Product;
import com.dmall.product.mapper.ProductMapper;
import com.dmall.product.service.ProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
