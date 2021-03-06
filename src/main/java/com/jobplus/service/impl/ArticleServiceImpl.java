package com.jobplus.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.jobplus.dao.ArticleMapper;
import com.jobplus.pojo.Account;
import com.jobplus.pojo.Article;
import com.jobplus.pojo.ArticleShare;
import com.jobplus.pojo.MyCollect;
import com.jobplus.pojo.Page;
import com.jobplus.pojo.Sms;
import com.jobplus.pojo.SupportList;
import com.jobplus.pojo.User;
import com.jobplus.service.IAccountService;
import com.jobplus.service.IArticleService;
import com.jobplus.service.IArticleShareService;
import com.jobplus.service.IMyCollectService;
import com.jobplus.service.IOperationSumService;
import com.jobplus.service.ISequenceService;
import com.jobplus.service.ISmsService;
import com.jobplus.service.ISupportListService;
import com.jobplus.service.IUpdTableColumnService;
import com.jobplus.service.IUserService;
import com.jobplus.utils.DateUtils;
import com.jobplus.utils.SolrJUtils;

@Service("articleService")
public class ArticleServiceImpl implements IArticleService{
	@Resource
	private ArticleMapper articleDao;
	@Resource
	private IArticleShareService articleShareService;	
	@Resource
	private ISequenceService seqService;
	@Resource
	private IUserService userService;
	@Resource
	private SolrJUtils solrJUtils;
	@Resource
	private IMyCollectService myCollectService;
	@Resource
	private IOperationSumService operationSumService;
	@Resource
	private IUpdTableColumnService updTableColumnService;
	@Resource
	private IAccountService accountService;
	@Resource
	private ISupportListService supportListService;
	@Resource
	private ISmsService smsService;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		
		return articleDao.deleteByPrimaryKey(id);
	}

	// 同时 插入一条文章分享记录
	@Transactional
	@Override
	public int insertArticleAndArticlehare(Article record) {
		ArticleShare share = new ArticleShare();
		int ret = 0;
		int id = seqService.getSeqByTableName("tbl_article");
		int shareId = seqService.getSeqByTableName("tbl_article_share");
		record.setId(id);
		share.setId(shareId);
		share.setUserid(record.getUserid());
		share.setArticleid(id);
		share.setRecommend(record.getRecommend());

		ret = articleDao.insert(record);
		if (ret > 0) {
			// operationSum表 文章分享数增加
			ret = operationSumService.updOperationSum(8, 0, 1,null);
			// 同时 插入一条书籍分享记录
			ret = articleShareService.insert(share);
			// 初始默认值+1    对应文章的评论数 + 1
			updTableColumnService.updNums(4, 1, 0, 1, id);
			//增加财富值
			accountService.modAccountAndDetail(record.getUserid(), 0, new Account().getSCORES()[0], 
					1, 0, new Account().getSCORES()[0],4);
		}
		return ret;
	}

	// 我分享的文章列表
	@Override
	public Page<Article> getSharedArticleList(Article record) {
		Page<Article> page = new Page<Article>();
		record.setPageNo(record.getPageNo() == null ? 1 : record.getPageNo());
		record.setLimitSt(record.getPageNo() * page.getPageSize() - page.getPageSize());
		record.setPageSize(page.getPageSize());
		int count = articleDao.getSharedArticleListCount(record);
		if(count < 1)
			return page;
		List<Article> list = articleDao.getSharedArticleList(record);
		if (list.size() > 0) {
			for (Article course : list) {
				//用于前端页面显示
				course.setUserShareTime(DateUtils.formatDate(course.getCreatetime(), "yyyy-MM-dd"));
			}
			page.initialize((long)count, record.getPageNo());
			page.setList(list);
		}
		return page;

	}
	// 我收藏的文章
	@Override
	public Page<Article> getCollectedArticleList(Article record) {
		Integer userid = Integer.parseInt((String) SecurityUtils.getSubject().getSession().getAttribute("userid"));
		MyCollect collect = new MyCollect();
		collect.setUserid(userid);
		// 动作类型枚举 0下载 1收藏 ACTIONTYPE ={0,1}
		collect.setActionType(collect.getACTIONTYPES()[1]);
		// 类型 暂时用表名存储 tbl_docs tbl_topics tbl_books
		// COLLECTTYPES = { "tbl_docs", "tbl_topics", "tbl_books",
		// "tbl_Article", "tbl_Articles", "tbl_sites" };
		collect.setCollecttype(collect.getCOLLECTTYPES()[4]);
		record.setMyCollect(collect);

		Page<Article> page = new Page<Article>();
		record.setPageNo(record.getPageNo() == null ? 1 : record.getPageNo());
		record.setLimitSt(record.getPageNo() * page.getPageSize() - page.getPageSize());
		record.setPageSize(page.getPageSize());
		int count = articleDao.getCollectedArticleListCount(record);
		if(count < 1)
			return page;
		List<Article> list = articleDao.getCollectedArticleList(record);
		if (list.size() > 0) {
			for (Article course : list) {
				course.setUserShareTime(DateUtils.formatDate(course.getMyCollect().getColltime(), "yyyy-MM-dd"));
			}
			page.initialize((long)count, record.getPageNo());
			page.setList(list);
		}
		return page;
	}

	// 批量删除个人中心-我分享的文章
	@Transactional
	@Override
	public int delSharedArticle(String condition[]) {
		int ret = 0;
		ret = articleDao.delSharedArticle(condition);
		if (ret > 0) {
			operationSumService.updOperationSum(8, 1, condition.length,null);
		}
		return ret;
	}
		
	// 获取文章详情 浏览数++
	@Override
	public Article getArticleDetail(Article record) {
		// 1.文章主体
		record = articleDao.selectByRecord(record);
		if(null == record){
			return null;
		}
		// 浏览次数+1
		articleDao.updateReadSum(record.getId());

		// 2.收藏此文章人的列表
		// 设置 收藏还是下载 MyCollect（ 动作类型枚举 0下载 1收藏）ACTIONTYPE ={0,1}
		// 设置表名 MyCollect COLLECTTYPES = { "tbl_docs", "tbl_topics", "tbl_books", "tbl_courses", "tbl_Articles", "tbl_sites" };
		List<User> userList = userService.getCollectUsers(new MyCollect().getCOLLECTTYPES()[4],
				new MyCollect().getACTIONTYPES()[1], record.getId());

		// 3.相关文章列表
		@SuppressWarnings({ "unchecked", "static-access" })
		List<Article> theSameArticle = solrJUtils.findArticleFromList(record.getTitle(), record.getId().toString(),record.getArticletype(),record.getArticleclass());
		// 4.评论列表
		ArticleShare share = new ArticleShare();
		share.setArticleid(record.getId());

		Page<ArticleShare> shareList = articleShareService.getList(share);
		
		//5.打赏的人
		List<User> rewardUsers = userService.getRewardUsers(record.getId());

//		record.setCurrentUser(currentUser);
		record.setCollectUsers(userList);
		record.setRelatedList(theSameArticle);
		record.setCommentList(shareList);
		record.setRewardUsers(rewardUsers);
		return record;
	}

	// 文章的收藏与取消
	@Transactional
	@Override
	public MyCollect collectArticle(MyCollect record) {
		int ret = 0;
		if (record.getJudgeTodo() == 0) {
			// 开始收藏文章
			int id = seqService.getSeqByTableName("tbl_collect");
			record.setId(id);
			// 动作类型枚举 0下载 1收藏
			record.setActionType(record.getACTIONTYPES()[1]);
			// 类型 暂时用表名存储COLLECTTYPES = { "tbl_docs", "tbl_topics", "tbl_books",
			// "tbl_courses", "tbl_Articles", "tbl_sites" };
			record.setCollecttype(record.getCOLLECTTYPES()[4]);
			// 插入记录
			ret = myCollectService.insert(record);

			// 对应用户文章收藏数 增加
			operationSumService.updOperationSum(9, 0, 1,null);

			// 文章的收藏数增加
			updTableColumnService.updNums(4, 0, 0, 1, record.getObjectid());

		} else if (record.getJudgeTodo() == 1) {
			// 取消收藏文章
			// 删除记录

			if (record.getCollecttype() != null && record.getObjectid() != null && record.getActionType() != null
					&& record.getUserid() != null) {
				ret = myCollectService.delMycollects(record);
			}

			// 对应用户文章收藏数 减少
			operationSumService.updOperationSum(9, 1, 1,null);

			// 文章的收藏数减少
			updTableColumnService.updNums(4, 0, 1, 1, record.getObjectid());
		}
		if(ret <1){
			record = null;
		}
		return record;
	}
	@Transactional
	@Override
	public int insert(Article record) {
		int ret = 0;
		int id = seqService.getSeqByTableName("tbl_article");
		record.setId(id);
		ret = articleDao.insert(record);
		if(ret > 0){
			//operationSum表  文章分享数增加
			ret = operationSumService.updOperationSum(8, 0, 1,null);
			
			//增加财富值
			accountService.modAccountAndDetail(record.getUserid(), 0, new Account().getSCORES()[0], 
					1, 0, new Account().getSCORES()[0],4);
		}
		return ret;
	}
	@Transactional
	@Override
	public int insertSelective(Article record) {
		
		return articleDao.insertSelective(record);
	}

	@Override
	public Article selectByPrimaryKey(Integer id) {
		
		return articleDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Article record) {
		
		return articleDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Article record) {
		
		return articleDao.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Article record) {
		
		return articleDao.updateByPrimaryKey(record);
	}

	/**
	 * 打赏 
	 */
	@Transactional
	@Override
	public int reward(HttpServletRequest request,Article article, SupportList supt) {
		int ret = 0;
		//1.插入打赏记录表
		int suptId = seqService.getSeqByTableName("tbl_article_supportlist");
		supt.setId(suptId);
		ret = supportListService.insert(supt);
		if(ret>0){
			//文章打赏次数增加
			articleDao.updateSupportCount(article.getId());
			
			//2.打赏者与被打赏者 财富值增减
			// 扣减财富值
			ret = accountService.modAccountAndDetail(supt.getUserid(), 0, -supt.getSupportvalue(), 1, 1,
					supt.getSupportvalue(), 9);
			//财富值扣减失败（财富值不足等）
			if(ret==0)
				return 0;
			// 增加财富值
			accountService.modAccountAndDetail(article.getUserid(), 0, article.getSupportValue(), 1, 0,
					article.getSupportValue(), 10);
			
			// 发送系统通知 
			//消息通知 对方 财富值增加
		      //添加消息通知
		      smsService.addNotice((User)request.getSession().getAttribute("user"), request.getContextPath(), new Sms().getTABLENAMES()[4],article.getId(),
		    		  article.getUserid(),100,article.getId(),article.getTitle(),"系统为您增加了"+article.getSupportValue()+"财富值");
		    //打赏留言不为空  则发送私信   data:{receivedid:receivedid,smstype:1,smscontent:smscontent},
		      if(!StringUtils.isBlank(article.getSmsContent())){
		    	  String userid = (String) request.getSession().getAttribute("userid");
		    	  Sms sms = new Sms();
		    	  sms.setSenderid(Integer.parseInt(userid));
		    	  sms.setReceivedid(article.getUserid());
		    	  sms.setSmstype(1);//私信默认为1
		    	  sms.setSmscontent(article.getSmsContent());
		    	  smsService.insertPrvivatSms(sms);	    	  
		      }
			
		}
			
		return ret;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
