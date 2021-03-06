package com.jobplus.service;


/**
 * 更改 某表 某数
 * @author Jerry
 *
 */
public interface IUpdTableColumnService {
	
	/**
	 * TABLENAMES =  new String[]{"tbl_docs","tbl_books","tbl_topics","tbl_courses","tbl_article","tbl_sites",
				"tbl_docs_comment","tbl_books_share","tbl_topics_comment","tbl_courses_share","tbl_article_share","tbl_sites_share",
				"tbl_company_news"};
	 * DOCSCOLUMNS = new String[]{"collectSum","downSum","readSum","likeSum","recommendSum" };
	 * BOOKSCOLUMNS = new String[]{"collectSum","recommendSum" };
	 * TOPICSCOLUMNS = new String[]{"followsSum","readSum","replySum","likeSum","futilitySum","collectSum","commentSum"};	
	 * COURSESCOLUMNS = new String[]{"collectSum","recommendSum","likeSum","readSum" };
	 * ARTICLESCOLUMNS = new String[]{"collectSum","recommendSum","likeSum","readSum" };
	 * SITESCOLUMNS = new String[]{"collectSum","recommendSum","likeSum","readSum" };
		
	 * DOCSCOMMENTCOLUMNS = new String[]{ "likesum"};
	 * BOOKSHARECOLUMNS = new String[]{ "likesum"};
	 * TOPICSCOMMENTCOLUMNS = new String[]{ "replySum","likeSum","collectSum"};
	 * COURSESSHARECOLUMNS = new String[]{ "likesum"};
	 * ARTICLESHARECOLUMNS = new String[]{ "likesum"};
	 * SITESSHARECOLUMNS = new String[]{ "likesum"};
	 * COMPANYNEWSCOLUMNS = new String[]{"commentsum", "likesum"};
	 * @param tableName 要更改的表名
	 * @param tableColumn 要更改的表字段
	 * @param addOrDec 增加or减少
	 * @param AdOrDeNum 增减数量
	 * @param objId id
	 * @param updTime 表中的updatetime  不传递不修改
	 * @return
	 */
	int updNums(Integer tableName, Integer tableColumn, Integer addOrDec, Integer AdOrDeNum, Integer objId);
	
	/**
	 * 后台管理删除某一个
	 * TABLENAMES =  new String[]{"tbl_docs","tbl_books","tbl_topics","tbl_courses","tbl_article","tbl_sites", 5
				"tbl_docs_comment","tbl_books_share","tbl_topics_comment","tbl_courses_share","tbl_article_share","tbl_sites_share"};
	 * @param id
	 * @param tableName
	 * @return
	 */
	int delOneById(String tableName,Integer id);
	
	/**
	 * 是否已经分享了链接 
	 * @param tableName
	 * @param userid
	 * @return
	 */
	int hasSharedUrl(String tableName,Integer userid,String url);
}
