package com.jial.basic.dao;

import java.util.List;
import java.util.Map;

import com.jial.basic.model.Pager;

/**
 * 公共的Dao处理对象，这个对象包含了hibernate的所有基本操作和对sql的操作
 * @author jial
 *
 * @param <T>
 */
public interface IBaseDao<T> {

	/**
	 * 添加对象
	 * @param t
	 * @return
	 */
	public T add(T t);
	/**
	 * 更新对象
	 * @param t
	 */
	public void update(T t);
	/**
	 * 根据id删除对象
	 * @param id
	 */
	public void delete(int id);
	/**
	 * 根据id加载对象
	 * @param id
	 * @return
	 */
	public T load(int id);
	/**
	 * 不分页对象列表
	 * @param hql 查询对象的hql
	 * @param args 查询参数
	 * @return 一组不分页对象列表
	 */
	public List<T> list(String hql,Object[] args);
	public List<T> list(String hql,Object arg);
	public List<T> list(String hql);
	/**
	 * 不分页基于别名和查询参数的混合列表对象
	 * @param hql
	 * @param alias 别名对象
	 * @return
	 */
	public List<T> listByAlias(String hql,Map<String,Object> alias);
	public List<T> list(String hql,Object[] args,Map<String,Object> alias);
	
	/**
	 * 分页对象列表
	 * @param hql 查询对象的hql
	 * @param args 查询参数
	 * @return 一组分页对象列表
	 */
	public Pager<T> find(String hql,Object[] args);
	public Pager<T> find(String hql,Object arg);
	public Pager<T> find(String hql);
	/**
	 * 分页基于别名和查询参数的混合列表对象
	 * @param hql
	 * @param alias 别名对象
	 * @return
	 */
	public Pager<T> findByAlias(String hql,Map<String,Object> alias);
	public Pager<T> find(String hql,Object[] args,Map<String,Object> alias);
	/**
	 * 根据hql查询一组对象
	 * @param hql
	 * @param args
	 * @return
	 */
	public Object queryObject(String hql,Object[] args);
	public Object queryObject(String hql,Object arg);
	public Object queryObject(String hql);
	public Object queryObject(String hql,Object[] args,Map<String,Object> alias);
	public Object queryObjectByAlias(String hql,Map<String,Object> alias);
	/**
	 * 根据hql更新对象
	 * @param hql
	 * @param args
	 */
	public void updateByHql(String hql,Object[] args);
	public void updateByHql(String hql,Object arg);
	public void updateByHql(String hql);
	/**
	 * 不分页根据sql查询对象，不包含关联对象
	 * @param sql 查询的sql语句
	 * @param args 查询参数
	 * @param clz 查询的实体对象
	 * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是，需要使用setReseultTransform查询
	 * @return 一组对象
	 */
	public List<Object> listBySql(String sql,Object[] args,Class<Object> clz,boolean hasEntity);
	public List<Object> listBySql(String sql,Object arg,Class<Object> clz,boolean hasEntity);
	public List<Object> listBySql(String sql,Class<Object> clz,boolean hasEntity);
	public List<Object> listBySql(String sql,Object[] args,Map<String,Object> alias,Class<Object> clz,boolean hasEntity);
	public List<Object> listByAliasSql(String sql,Map<String,Object> alias,Class<Object> clz,boolean hasEntity);
	
	/**
	 * 分页根据sql查询对象，不包含关联对象
	 * @param sql 查询的sql语句
	 * @param args 查询参数
	 * @param clz 查询的实体对象
	 * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是，需要使用setReseultTransform查询
	 * @return 一组对象
	 */
	public Pager<Object> findBySql(String sql,Object[] args,Class<Object> clz,boolean hasEntity);
	public Pager<Object> findBySql(String sql,Object arg,Class<Object> clz,boolean hasEntity);
	public Pager<Object> findBySql(String sql,Class<Object> clz,boolean hasEntity);
	public Pager<Object> findBySql(String sql,Object[] args,Map<String,Object> alias,Class<Object> clz,boolean hasEntity);
	public Pager<Object> findByAliasSql(String sql,Map<String,Object> alias,Class<Object> clz,boolean hasEntity);
}

