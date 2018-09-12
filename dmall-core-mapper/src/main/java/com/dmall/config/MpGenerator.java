package com.dmall.config;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MpGenerator {

    /**
         * <p>
         * MySQL 生成演示
         * </p>
         */
        public static void main(String[] args) {
            AutoGenerator mpg = new AutoGenerator();
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            gc.setOutputDir("D:\\code");
            gc.setFileOverride(true);
            gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
            gc.setEnableCache(false);// XML 二级缓存
            gc.setBaseResultMap(true);// XML ResultMap
            gc.setBaseColumnList(true);// XML columList
            gc.setAuthor("yuhang");

            gc.setMapperName("%sMapper");
            // gc.setXmlName("%sDao");
            gc.setServiceName("%sService");
//        gc.setDtoName("%sDTO");
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDbType(DbType.MYSQL);
            dsc.setTypeConvert(new MySqlTypeConvert() {
                // 自定义数据库表字段类型转换【可选】
                @Override
                public DbColumnType processTypeConvert(String fieldType) {
                    // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                    return super.processTypeConvert(fieldType);
                }
            });
            dsc.setDriverName("com.mysql.jdbc.Driver");
            dsc.setUsername("root");
            dsc.setPassword("uAiqwVwjJ8-i");
            dsc.setUrl("jdbc:mysql://106.15.188.249:3306/dmall?characterEncoding=utf8");
            mpg.setDataSource(dsc);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setEntityLombokModel(true);
            List<TableFill> tableFillList =  new ArrayList<>();
            tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
            tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
            strategy.setTableFillList(tableFillList);
            // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
            strategy.setTablePrefix(new String[] { "p_"});// 此处可以修改为您的表前缀
            strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
            strategy.setInclude(new String[] { "p_product_type_brand"}); // 需要生成的表
            // strategy.setExclude(new String[]{"test"}); // 排除生成的表

            // 【实体】是否生成字段常量（默认 false）
            // public static final String ID = "test_id";
            // strategy.setEntityColumnConstant(true);
            // 【实体】是否为构建者模型（默认 false）
            // public User setName(String name) {this.name = name; return this;}
            strategy.setEntityBuilderModel(true);

            mpg.setStrategy(strategy);

            // 包配置
            PackageConfig pc = new PackageConfig();
            pc.setParent("com.dmall");
            pc.setModuleName("product");
            pc.setController("controller");
            pc.setMapper("mapper");
            pc.setEntity("entity");
            pc.setXml("sqlmaps");
//        pc.setDto("dto");
            mpg.setPackageInfo(pc);

            // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
            // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
            TemplateConfig tc = new TemplateConfig();
            /*tc.setDto("/templates/dto.java");
            tc.setAddDto("/templates/adddto.java");
            tc.setUpdDto("/templates/upddto.java");
            tc.setPageInDto("/templates/pagein.java");
            tc.setPageOutDto("/templates/pageout.java");*/
            // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
            mpg.setTemplate(tc);

            // 执行生成
            mpg.execute();
        }

    }
