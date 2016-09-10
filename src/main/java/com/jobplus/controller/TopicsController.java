package com.jobplus.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSON;
import com.jobplus.pojo.Page;
import com.jobplus.pojo.Topics;
import com.jobplus.pojo.TopicsComment;
import com.jobplus.pojo.User;
import com.jobplus.pojo.response.BaseResponse;
import com.jobplus.pojo.response.TopicsCommentResponse;
import com.jobplus.service.ITagsService;
import com.jobplus.service.ITopicsCommentService;
import com.jobplus.service.ITopicsService;
import com.jobplus.service.ITypeConfigService;
import com.jobplus.service.IUserService;
import com.jobplus.utils.ConstantManager;
import com.jobplus.utils.SolrJUtils;

@Controller
@RequestMapping("/topics")
public class TopicsController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(TopicsController.class);

	@Resource
	private ITopicsService topicsService;	
	@Resource
	private ITagsService tagsService ;	
	@Resource
	private SolrJUtils solrJUtils;
	@Resource
	private ITypeConfigService typeConfigService;
	@Resource
	private ITopicsCommentService topicsCommentService;
	@Resource
	private IUserService userService;

	
	/**
	 * 图片上传
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/imageUp", method = RequestMethod.POST)
	public ModelAndView imageUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		logger.info("*imageUp**图片上传******");
		mv.setViewName("forward:/uedit/jsp/imageUp.jsp");
		return mv;
	}
	
	/**
	 * 话题、提问 发布 （新增话题）
	 * @param request
	 * @param response
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addTop(HttpServletRequest request, HttpServletResponse response, Topics record) {
		ModelAndView mv = new ModelAndView();
		int userID = Integer.parseInt((String) request.getSession().getAttribute("userid"));
		// 设置创建人
		record.setCreateperson(userID);
		// 1.插入话题
		record = topicsService.insert(record);
		if(record!=null){
			//个人操作数之类的信息放入session
			userService.getMyHeadTopAndOper(request);
		}
		// 返回前端数据设置
		mv.addObject("record", record);
		// 返回视图名设置
		mv.setViewName("redirect:success");
		logger.info("**addTop*话题、提问 发布 （新增话题）***record==" + JSON.toJSONString(record));
		return mv;
	}
	
	/**
	 * 防止重复提交   转发到静态页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sharein/success/successUploadTopic");
		return mv;
	}

	/**
	 * 话题详情   包括话题的回答    粉丝列表   相关话题列表   sortType排序方式      1是时间排序      2是评论数排序    默认按照时间排序
	 * 
	 * @param request
	 * @param response
	 * @param topicId
	 * @param sortType
	 * @return
	 */
	@RequestMapping(value = "/getTopicsDetail")
	@ResponseBody
	public ModelAndView getTopicsDetail(HttpServletRequest request, HttpServletResponse response,@RequestParam(required=true) String topicId,
			@RequestParam(required=false) String sortType) {
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isBlank(sortType)){//默认为1  按照时间排序
			sortType = ConstantManager.DEFAULT_SORT_TYPE_VALUE;
		}
		TopicsComment record = new TopicsComment();
		record.setSortType(Integer.parseInt(sortType));
		record.setTopicsid(Integer.parseInt(topicId));
		Topics topicsDetail = topicsService.getTopicsDetail(record);
		if(null == topicsDetail){
			mv.setViewName("404");
			return mv;
		}
		logger.info("getTopicsDetail话题详情   包括话题的回答    粉丝列表   相关话题列表   sortType排序方式      1是时间排序      2是评论数排序    默认按照时间排序   TopicsComment record=== "+JSON.toJSONString(record)+"*****");
//		logger.info("****getTopicsDetail*******topicsDetail**"+JSON.toJSONString(topicsDetail));
		
		mv.addObject("topicsDetail", topicsDetail);
		mv.addObject("sortType", sortType);
		mv.setViewName("mydocs/docs/topicDetail");
		return mv;
	}
	
	
	/**
	 * 按排序要求获取话题下面的回答 列表
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getSortTopicsCommentsByTopicId")
	@ResponseBody
	public String getTopicsCommentsByTopicIdSort(HttpServletRequest request, HttpServletResponse response,TopicsComment record) {

		BaseResponse basesResponse = new BaseResponse();
		try {
			if (record.getSortType() == null) {// 默认为1 按照时间排序
				record.setSortType(Integer.parseInt(ConstantManager.DEFAULT_SORT_TYPE_VALUE));
			}
			Page<TopicsComment> topicsCommentList = topicsCommentService.getSortTopicsCommentsByTopicId(record);
			logger.info("***/getSortTopicsCommentsByTopicId****按排序要求获取话题下面的回答 列表**   TopicsComment record=== "
					+ JSON.toJSONString(record) + "*****");
			logger.info("***/getSortTopicsCommentsByTopicId****按排序要求获取话题下面的回答 列表**topicsCommentList**"
					+ JSON.toJSONString(topicsCommentList));
			basesResponse.setObj(topicsCommentList);
			basesResponse.setReturnMsg(ConstantManager.SUCCESS_MESSAGE);
			basesResponse.setReturnStatus(ConstantManager.SUCCESS_STATUS);
			return JSON.toJSONString(basesResponse);

		} catch (Exception e) {
			basesResponse.setReturnMsg(e.getMessage());
			basesResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
			logger.info("****getSortTopicsCommentsByTopicId 失败999*******"+e.getMessage());
			return JSON.toJSONString(basesResponse);
		}
	}
	

	/**
	 *  获取话题的评论 或者 话题下回答的评论
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getPartTopicsComment")
	@ResponseBody
	public String getPartTopicsComments(HttpServletRequest request, HttpServletResponse response,TopicsComment topicsComment) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			Page<TopicsComment> topicsCommentPage = topicsCommentService.getPartTopicsComments(topicsComment);
			logger.info("**getPartTopicsComment 获取话题的评论 或者 话题下回答的评论 **topicsCommentList=="
					+ JSON.toJSONString(topicsCommentPage));
			baseResponse.setObj(topicsCommentPage);
			baseResponse.setReturnMsg(ConstantManager.SUCCESS_MESSAGE);
			baseResponse.setReturnStatus(ConstantManager.SUCCESS_STATUS);
			return JSON.toJSONString(baseResponse);

		} catch (Exception e) {
			baseResponse.setReturnMsg(e.getMessage());
			baseResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
			logger.info("**********getPartTopicsComment*请求失败 999 ********************"+e.getMessage());
			return JSON.toJSONString(baseResponse);
		}
	}
	
	/**
	 * 话题 新增回答         待测试 
	 * @param request
	 * @param response
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/insertTopicsComment")
	@ResponseBody
	public String insertTopicsComment(HttpServletRequest request, HttpServletResponse response,
			TopicsComment comment) {
		TopicsCommentResponse topicsCommentResponse = new TopicsCommentResponse();
		try {
			User user = (User)request.getSession().getAttribute("user");
			if (user!=null) {
				comment = topicsCommentService.insert(comment,request.getContextPath(),user);
				if(comment !=null){
					//更新用户操作数统计   存入session
					userService.getMyHeadTopAndOper(request);
					
					topicsCommentResponse.setTopicsComment(comment);
					topicsCommentResponse.setReturnMsg(ConstantManager.SUCCESS_MESSAGE);
					topicsCommentResponse.setReturnStatus(ConstantManager.SUCCESS_STATUS);
				}else{
					topicsCommentResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
				}
				
				logger.info("**insertTopicsComment **话题 新增回答**topicsComment=="+JSON.toJSONString(comment));
				return JSON.toJSONString(topicsCommentResponse);
			} else {
				topicsCommentResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
				logger.info("**insertTopicsComment **话题 新增回答  失败  999 登录失败****");
				return JSON.toJSONString(topicsCommentResponse);
			}
		} catch (Exception e) {
			topicsCommentResponse.setReturnMsg(e.getMessage());
			topicsCommentResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
			logger.info("**insertTopicsComment **话题 新增回答  失败  999****"+e.getMessage());
			return JSON.toJSONString(topicsCommentResponse);
		}

	}
	/**
	 * 在话题详情页删除 话题评论、话题回复、回答的评论
	 * @param request
	 * @param response
	 * @param topicsComment
	 * @return
	 */
	@RequestMapping(value = "/delCommentOnTopDetail")
	@ResponseBody
	public String delCommentOnTopDetail(HttpServletRequest request, HttpServletResponse response,
			TopicsComment topicsComment) {
		
		TopicsCommentResponse topicsCommentResponse = new TopicsCommentResponse();
		try {
			String userid = (String) request.getSession().getAttribute("userid");			
			if (!StringUtils.isBlank(userid)) {
//				int ret = 0; 
				topicsCommentService.delCommentOnTopDetail(topicsComment);
//				topicsCommentResponse.setTopicsComment(topicsComment);
				logger.info("**delCommentOnTopDetail*在话题详情页删除 话题评论、话题回复、回答的评论***topicsComment=="+JSON.toJSONString(topicsComment));
				topicsCommentResponse.setReturnMsg(ConstantManager.SUCCESS_MESSAGE);
				topicsCommentResponse.setReturnStatus(ConstantManager.SUCCESS_STATUS);
				return JSON.toJSONString(topicsCommentResponse);
			} else {
				topicsCommentResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
				logger.info("**delCommentOnTopDetail*在话题详情页删除 话题评论、话题回复、回答的评论 失败 999 登录失败***");
				return JSON.toJSONString(topicsCommentResponse);
			}
		} catch (Exception e) {
			topicsCommentResponse.setReturnMsg(e.getMessage());
			topicsCommentResponse.setReturnStatus(ConstantManager.ERROR_STATUS);
			logger.info("**delCommentOnTopDetail*在话题详情页删除 话题评论、话题回复、回答的评论 失败 999***"+e.getMessage());
			return JSON.toJSONString(topicsCommentResponse);
		}
		
	}
	
	
	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping(value = "/findTitle/{condition}", method = RequestMethod.POST)
	public String findAllTop(HttpServletRequest request,@PathVariable String condition) throws Exception {
			
			return solrJUtils.findTops(condition);
		
	}

	/**
	 * @param theme  1热门话题 2最新话题 3等待回答 4精彩问答
	 * @param record 参数
	 * @return
	 */
	@RequestMapping(value = "/fore/search/{theme}")
	public ModelAndView search(@RequestHeader("Accept") String encoding, @PathVariable int theme, Topics record) {
		String topicstype = record.getTopicstype();
		Page<Topics> topicsPage = topicsService.searchTopics(theme, record);
		if (encoding.indexOf("application/json") != -1) {
			//分页post请求
			Map<String, Page<Topics>> map = new HashMap<String, Page<Topics>>();
			map.put("topicsPage", topicsPage);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		} else {
			ModelAndView mv = new ModelAndView();
			mv.addObject("theme", theme);
			mv.addObject("topicstype", topicstype == null ? "" : topicstype);
			mv.addObject("topicsPage", topicsPage);
			mv.addObject("typeConfigs", typeConfigService.getAllParentTypeConfigs());
			mv.setViewName("topicDivision");
			return mv;
		}
	}
}
