package com.dmall.plat.product.vo;

import com.dmall.plat.product.dto.ProductDTO;
import com.dmall.plat.product.dto.PropsGroupDTO;
import com.dmall.product.entity.Product;
import com.dmall.product.entity.ProductExt;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: yuhang
 * @date: 2018/9/27
 */
@Data
public class ProductVo implements Serializable {

    private static final long serialVersionUID = 3562591674701442716L;

    private Product product;

    private ProductExt productExt;

    private List<PropsGroupDTO> propsVoList;

    private List<Map<String,String>> imgUrls;



}
