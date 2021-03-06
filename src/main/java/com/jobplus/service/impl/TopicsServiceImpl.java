package com.jobplus.service.impl;

import com.jobplus.dao.AccountMapper;
import com.jobplus.dao.TagsMapper;
import com.jobplus.dao.TopicsCommentMapper;
import com.jobplus.dao.TopicsMapper;
import com.jobplus.pojo.*;
import com.jobplus.service.*;
import com.jobplus.utils.ConstantManager;
import com.jobplus.utils.DBUtilsTemplate;
import com.jobplus.utils.DateUtils;
import com.jobplus.utils.SolrJUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("topicsService")
public class TopicsServiceImpl implements ITopicsService {
    @Resource
    private TagsMapper tagsDao;
    @Resource
    private AccountMapper accountDao;
    @Resource
    private TopicsMapper topicsDao;
    @Resource
    private TopicsCommentMapper topicsCommentDao;
    @Resource
    private ISequenceService seqService;
    @Resource
    private IAccountService accountService;
    @Resource
    private IAccountDetailService accountDetailService;
    @Resource
    private IOperationSumService operationSumService;
    @Resource
    private IAttentionService attentionService;
    @Resource
    private ITopicsCommentService topicsCommentService;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private IUserService userService;
    @Resource
    private SolrJUtils solrJUtils;
    @Resource
    private DBUtilsTemplate dBUtilsTemplate;
    @Resource
    private ITypeConfigService typeConfigService;
    @Resource
    private ITagsService tagsService;
    @Resource
	private ISmsService smsService;

    @Override
    public int deleteByPrimaryKey(Integer id) {

        return topicsDao.deleteByPrimaryKey(id);
    }

    /**
     * 插入话题   对应插入账户财富值值和明细
     */
    @Transactional
    @Override
    public Topics insert(Topics record) {
        int ret = 0;
        int topicID = seqService.getSeqByTableName("tbl_topics");
        record.setId(topicID);
        record.setIspublic(null == record.getIspublic() ? 1 : record.getIspublic());
        // 1.插入话题
        ret = topicsDao.insert(record);
        if (ret > 0) {
            // 2.分享话题成功 增加财富值 // 3.记录财富值动作
            int userid = record.getCreateperson();
            // 2.分享话题成功 增加财富值 // 3.记录财富值动作2.分享话题成功 增加财富值 // 3.记录财富值动作
            accountService.modAccountAndDetail(userid, 0, new Account().getSCORES()[0], 1, 0,
                    new Account().getSCORES()[0], 1);

            // 对应用户分享的话题数 增加*******
            ret = operationSumService.updOperationSum(3, 0, 1, null);
            // 用于标签使用数 +1
            String tagsArray = record.getTopicsclass();
            // 所选标签总数 +1
            tagsService.addOrDecreaseTagUsenumer(tagsArray);
            
            if(ret > 0 && record.getRewardValue()>0){
            	//话题发布 如果有悬赏值 财富值扣减
            	accountService.modAccountAndDetail(userid, 0, -record.getRewardValue(), 1, 1,
            			record.getRewardValue(), 11);
            	
            }
            return record;
        } else {
            return null;
        }
    }

    @Override
    public int insertSelective(Topics record) {

        return topicsDao.insertSelective(record);
    }

    @Override
    public Topics selectByPrimaryKey(Integer id) {
        Topics record = topicsDao.selectByPrimaryKey(id);
        return record;
    }

    @Transactional
    @Override
    public int updateByPrimaryKeySelective(Topics record) {
        int ret = 0;
        ret = topicsDao.updateByPrimaryKeySelective(record);
        return ret;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Topics record) {


        return topicsDao.updateByPrimaryKeyWithBLOBs(record);
    }

    /**
     * 更新话题的关注数     value 正负值表示加减
     *
     * @param value
     * @param id
     * @return
     */
    @Override
    public int updateFollowsSum(Integer value, Integer id) {
        return topicsDao.updateFollowsSum(value, id);
    }

    /**
     * 我发布的话题
     *
     * @param userID
     * @return
     */
    @Override
    public Page<Topics> getMyTopicsUploaded(Topics record) {
        Page<Topics> page = new Page<Topics>();
        record.setPageNo(record.getPageNo() == null ? 1 : record.getPageNo());
        record.setPageSize(page.getPageSize());
        record.setLimitSt(record.getPageNo() * page.getPageSize() - page.getPageSize());
        int count = topicsDao.getMyTopicsUploadedCount(record);
        if (count < 1)
            return page;
        List<Topics> list = topicsDao.getMyTopicsUploaded(record);
        if (list.size() > 0) {
            page.initialize((long) count, record.getPageNo());
            page.setList(list);
            for (Topics topics : list) {// 设置时间用于页面显示
                topics.setShowcreatetime(DateUtils.formatDate(topics.getCreatetime(), "yyyy-MM-dd"));
            }
        }
        return page;
    }

    /**
     * 批量逻辑删除topics
     *
     * @param condition
     * @return
     */
    @Transactional
    @Override
    public int deleteTopics(String[] condition, String userid) {
        int ret = 0;
        ret = topicsDao.deleteTopics(condition);
        if (ret > 0) {
            // 对应用户分享的话题数 减少 st*******
            ret = operationSumService.updOperationSum(3, 1, condition.length, null);
        }
        return ret;
    }

    /**
     * 获取某条话题的所有回答   关联用户表的用户名和用户头像     （用于话题详情页的展示） sortType排序方式
     *
     * @param record
     * @return
     */
    @SuppressWarnings({"unchecked", "static-access"})
    @Override
    public Topics getTopicsDetail(TopicsComment record) {
        Topics topic = new Topics();
        if (record.getTopicsid() != null) {
            //获取话题详情主体
            topic = topicsDao.selectRecord(record.getTopicsid());
            if (null == topic) {
                return null;
            }
            Page<TopicsComment> commentList = new Page<>();
            List<User> fansList = new ArrayList<>();
            List<SolrDocumentList> relatedTopicsList = new ArrayList<>();
            //查询评论列表
            commentList = topicsCommentService.getSortTopicsCommentsByTopicId(record);

//			String fansIds = (String)this.findFandIds(record.getTopicsid().toString()).get(0).get("fansIds");
            //查询粉丝
            fansList = userService.getFansListInformation("tbl_topics", record.getTopicsid());
            //根据标题查询相关话题列表
            relatedTopicsList = solrJUtils.findTopsFromList(topic.getTitle(), String.valueOf(topic.getId()),topic.getTopicstype(),topic.getTopicsclass());
            topic.setCommentList(commentList);
            topic.setFansList(fansList);
            topic.setRelatedList(relatedTopicsList);
//			topic.setFansIds(fansIds);
            return topic;
        } else {
            return null;
        }
    }

//	@Override
//	public List<Map<String,Object>> findFandIds(String topicsId) {
//
//		return dBUtilsTemplate.find("select group_concat(userid) as fansIds  from tbl_attention where objectid=? and colltype='tbl_topics'", new Object[] { topicsId });
//
//	}

    /**
     * 话题专题列表
     *
     * @param theme
     * @param record
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Page<Topics> searchTopics(int theme, Topics record) {
        @SuppressWarnings({ "unused", "static-access" })
		List<Object> objectList = solrJUtils.searchTopics(record.getCondition(), record.getTopicstype(), null, String.valueOf(record.getPageNo() == null ? 1 : record.getPageNo()), String.valueOf(record.getPageSize() == null ? record.getDefaultPageSize() : record.getPageSize()), String.valueOf(theme));
      
        List<Topics> list = null;
        String topicKey = ConstantManager.REDIS_KEY_TOPICS_SEARCH + theme + "-" + record.getTopicstype();
        if (!redisService.exists(topicKey)) {
            if (StringUtils.isNotBlank(record.getTopicstype())) {
                TypeConfig typeConfig = typeConfigService.getTypeConfigById(Integer.parseInt(record.getTopicstype()));
                if (typeConfig != null) {
                    record.setTopicstype(typeConfig.getTypeid() + ":" + typeConfig.getTypename());
                }
            }
            if (theme == 1) {//热门话题
                int count = topicsDao.getHotTopicsCount(record);
                if (count < 1)
                    list = null;
                else
                    list = topicsDao.getHotTopics(record);
            } else if (theme == 2) {//最新话题
                int count = topicsDao.getLatestTopicsCount(record);
                if (count < 1)
                    list = null;
                else
                    list = topicsDao.getLatestTopics(record);
            } else if (theme == 3) {//等待回答
                int count = topicsDao.getWaitReplyTopicsCount(record);
                if (count < 1)
                    list = null;
                else
                    list = topicsDao.getWaitReplyTopics(record);
            } else if (theme == 4) {//精彩问答
                int count = topicsDao.getHotReplyTopicsCount(record);
                if (count < 1)
                    list = null;
                else
                    list = topicsDao.getHotReplyTopics(record);
            }
            if (list != null && list.size() > 0)
                redisService.setList(topicKey, list, ConstantManager.REDIS_KEY_TOPICS_SEARCH_EXPIRE);
        }

        Page<Topics> page = new Page();
        record.setPageNo(record.getPageNo() == null ? 1 : record.getPageNo());
        record.setPageSize(page.getPageSize());
        record.setLimitSt(record.getPageNo() * page.getPageSize() - page.getPageSize());
        if (redisService.exists(topicKey)) {
            list = (List<Topics>) redisService.getList(topicKey, record.getLimitSt(), record.getLimitSt() + record.getPageSize() - 1);
            long count = redisService.getListSize(topicKey);
            if (list != null && list.size() > 0) {
                page.initialize(count, record.getPageNo());
                page.setList(list);
                for (Topics topics : list) {// 设置时间用于页面显示
                    topics.setShowcreatetime(DateUtils.formatDate(topics.getCreatetime(), "yyyy-MM-dd"));
                }
            }
        }
        return page;
    }
    /**
	 * 采纳 答案 
	 * @param record
	 * @return
	 */
    @Transactional
	@Override
	public int acptAnswer(Topics topic, TopicsComment comment,Sms sms) {
    	int ret = 0;
    	//更改为已采纳   回答采纳状态  1:已采纳   0:不悬赏  -1：取消悬赏 2:等待悬赏
    	topic.setAcceptStatus(1);
		ret = topicsDao.updateByPrimaryKeySelective(topic);
		if(ret > 0){
			// 修改评论状态位最佳答案
			comment.setIsAccept(1);
			topicsCommentDao.updateByPrimaryKeySelective(comment);
			if(!StringUtils.isBlank(sms.getSmscontent())){
				// 私信答谢
				smsService.insertPrvivatSms(sms);
			}			
			
			//财富值增加
			//话题评论者增加财富值
			accountService.modAccountAndDetail(sms.getReceivedid(), 0,  topic.getRewardValue(), 
					1, 0, topic.getRewardValue(),12);
			//财富值减少
//			//话题发布者减少财富值
//			accountService.modAccountAndDetail(topic.getCreateperson(), 0,  -topic.getRewardValue(), 
//					1, 0, topic.getRewardValue(),11);
			
		}	
		return ret;
	}
    /**
	 * 取消悬赏
	 */
    @Transactional
	@Override
	public int cancelRewd(Topics topic) {
		int ret = 0;
    	//更改为取消悬赏 	回答采纳状态  1:已采纳   0:不悬赏  -1：取消悬赏 2:等待悬赏
    	topic.setAcceptStatus(-1);
		ret = topicsDao.updateByPrimaryKeySelective(topic);
		if(ret > 0){
			//悬赏值返回到个人账户
			accountService.modAccountAndDetail(topic.getCreateperson(), 0,  topic.getRewardValue(), 
					1, 0, topic.getRewardValue(),11);
		}
		return ret;
	}
/**
 * 提高悬赏
 */
    @Transactional
	@Override
	public int upRewd(Topics record,Integer dValue) {
		int ret = 0;
//      回答采纳状态  1:已采纳   0:不悬赏  -1：取消悬赏 2:等待悬赏
				record.setAcceptStatus(2);
		ret = topicsDao.updateByPrimaryKeySelective(record);
		if(ret > 0 && dValue>0){
        	//有悬赏值变化  财富值扣减
        	accountService.modAccountAndDetail(record.getCreateperson(), 0, -dValue, 1, 1,
        			dValue, 14);
        	
        }
		return ret;
	}


}
