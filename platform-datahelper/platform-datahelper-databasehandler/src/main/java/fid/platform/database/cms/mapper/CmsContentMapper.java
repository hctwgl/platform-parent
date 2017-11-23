package fid.platform.database.cms.mapper;


import fid.platform.core.common.pojo.cms.CmsContent;

public interface CmsContentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsContent record);

    int insertSelective(CmsContent record);

    CmsContent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsContent record);

    int updateByPrimaryKey(CmsContent record);
}