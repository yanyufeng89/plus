package com.jobplus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jobplus.pojo.Docs;

public interface DocsMapper {
	
    int deleteByPrimaryKey(Integer id);

    /**
     * mapping文件  数据需要初始化
     * @param record
     * @return
     */
    int insert(Docs record);

    int insertSelective(Docs record);

    Docs selectByPrimaryKey(@Param(value="id")Integer id);
    
    /**
     * 关联查询当前文档上传者的个人信息
     * @param id
     * @return
     */
    Docs selectRecord(Docs record);

    int updateByPrimaryKeySelective(Docs record);

  //暂时注释  此方法为全量更新
//    int updateByPrimaryKey(Docs record);
    
    /**
     * 插入多条记录
     * @param list
     * @return
     */
    int insertDocs(List<Docs> list);
    
    /**
     * 我上传的文档
     * @param record
     * @return
     */
    List<Docs> getMyDocsUploaded(Docs record);
    
    /**
     * 批量逻辑删除文档
     * @param condition
     * @return
     */
    int deleteDocs(@Param(value="condition")String[] condition);
    /**
	 * 阅读数 ++
	 * 
	 * @param id
	 * @return
	 */
	int updateReadSum(Integer id);
}