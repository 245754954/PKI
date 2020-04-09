package nudt.web.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@ToString
public class SysMenu {
//            `id` int(65) NOT NULL auto_increment COMMENT '主键id',
//            `code` varchar(255) NOT NULL COMMENT '菜单编号',
//            `pcode` varchar(255) NOT NULL COMMENT '菜单父编号',
//            `pcodes` varchar(255) default NULL COMMENT '当前菜单的所有父菜单编号',
//            `name` varchar(255) NOT NULL COMMENT '菜单名称',
//            `icon` varchar(255) default NULL COMMENT '菜单图标',
//            `url` varchar(255) default NULL COMMENT 'url地址',
//            `num` int(65) default NULL COMMENT '菜单排序号',
//            `levels` int(65) default NULL COMMENT '菜单层级',
//            `ismenu` int(11) default NULL COMMENT '是否是菜单（1：是  0：不是）',
//            `tips` varchar(255) default NULL COMMENT '备注',
//            `status` int(65) default NULL COMMENT '菜单状态 :  1:启用   0:不启用',
//            `isopen` int(11) default NULL COMMENT '是否打开:    1:打开   0:不打开',
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column
    private String pcode;

    @Column
    private String pcodes;

    @Column
    private String name;

    @Column
    private String icon;

    @Column
    private String url;

    @Column
    private Integer num;

    @Column
    private Integer levels;

    @Column
    private Integer ismenu;

    @Column
    private String tips;

    @Column
    private Integer status;

    @Column
    private Integer isopen;



    @Transient
    private List<SysMenu> children = new ArrayList<SysMenu>();








}
