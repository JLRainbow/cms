package com.jial.basic.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.jial.basic.model.Pager;
import com.jial.basic.model.SystemContext;
@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T> {
	
	private SessionFactory sessionFactory;
	
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Inject
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession(){
		return sessionFactory.openSession();
	}
	/** 
	* 创建一个Class的对象来获取泛型的class 
	 */  
	private Class<T> clz;  
	      
	public Class<T> getClz(){  
	    if (clz==null) {  
	        clz=(Class<T>)(((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);  
	    }  
	    return clz;  
	} 
	
	@Override
	public T add(T t) {
		getSession().save(t);
		return t;
	}

	@Override
	public void update(T t) {
		getSession().update(t);
	}

	@Override
	public void delete(int id) {
		getSession().delete(this.load(id));
	}

	
	@Override
	public T load(int id) {
		return (T) getSession().load(getClz(), id);
	}

	@Override
	public List<T> list(String hql, Object[] args) {
		return this.list(hql, args, null);
	}

	@Override
	public List<T> list(String hql, Object arg) {
		return this.list(hql, new Object[]{arg});
	}

	@Override
	public List<T> list(String hql) {
		return this.list(hql,null);
	}

	@Override
	public List<T> listByAlias(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}

	private String initSort(String hql){
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		if(sort!=null&&!"".equals(sort.trim())){
			hql += " order by " + sort;
			if(!"desc".equals(order)) hql+=" asc";
			else hql+=" desc";
		}
		return hql;
	}
	
	private void setAliasParameter(Query query, Map<String, Object> alias){
		if(alias!=null){
			Set<String> keys = alias.keySet();
			for (String key : keys) {
				Object val = alias.get(key);
				if(val instanceof Collection){
					//查询条件是列表
					query.setParameter(key, (Collection)val);
				}else{
					query.setParameter(key, val);
				}
			}
		}
	}
	@SuppressWarnings("unused")
	private void setParameter(Query query,Object[] args){
		if(args!=null&&args.length>0){
			int index = 0;
			for (Object arg : args) {
				query.setParameter(index, arg);
			}
		}
	}
	
	@Override
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		Query query = getSession().createQuery(hql);
		setAliasParameter(query,alias);
		setParameter(query, args);
		
		return query.list();
	}

	@Override
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null);
	}

	@Override
	public Pager<T> find(String hql, Object arg) {
		return this.find(hql, new Object[]{arg});
	}

	@Override
	public Pager<T> find(String hql) {
		return this.find(hql, null);
	}

	@Override
	public Pager<T> findByAlias(String hql, Map<String, Object> alias) {
		return this.find(hql, null, alias);
	}
	
	@SuppressWarnings("rawtypes")
	private void setPagers(Query query,Pager pages){
		Integer pageSize = SystemContext.getPageSize();
		Integer pageOffset = SystemContext.getPageOffset();
		if(pageSize==null||pageSize<0) pageSize = 15;
		if(pageOffset==null||pageOffset<0) pageSize = 0;
		pages.setOffset(pageOffset);
		pages.setSize(pageSize);
		query.setFirstResult(pageOffset).setMaxResults(pageSize);
	}
	
	private String getCountHql(String hql,boolean isHql){
		String end = hql.substring(hql.indexOf("from"));
		String countHql = "select count(*) "+end;
		if(isHql){
			countHql.replaceAll("fetch", "");
		}
		return countHql;
	}

	@Override
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		String cq = getCountHql(hql,true);
		cq = initSort(cq);
		Query cquery = getSession().createQuery(cq);
		Query query = getSession().createQuery(hql);
		//设置别名参数
		setAliasParameter(query, alias);
		setAliasParameter(cquery, alias);
		//设置参数
		setParameter(query, args);
		setParameter(cquery, args);
		
		Pager<T> pages = new Pager<T>();
		setPagers(query,pages);
		List datas = query.list();
		pages.setDatas(datas);
		long total = (Long) cquery.uniqueResult();
		pages.setTotal(total);
		return pages;
	}

	@Override
	public Object queryObject(String hql, Object[] args) {
		return this.queryObject(hql, args, null);
	}

	@Override
	public Object queryObject(String hql, Object arg) {
		return this.queryObject(hql, new Object[]{arg});
	}

	@Override
	public Object queryObject(String hql) {
		return this.queryObject(hql, null);
	}
	
	@Override
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
		Query query = getSession().createQuery(hql);
		setAliasParameter(query,alias);
		setParameter(query, args);
		return query.uniqueResult();
	}

	@Override
	public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
		return this.queryObject(hql, null, alias);
	}

	@Override
	public void updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		query.executeUpdate();
	}

	@Override
	public void updateByHql(String hql, Object arg) {
		this.updateByHql(hql, new Object[]{arg});
	}

	@Override
	public void updateByHql(String hql) {
		this.updateByHql(hql, null);
	}

	@Override
	public List<Object> listBySql(String sql, Object[] args, Class<Object> clz, boolean hasEntity) {
		return this.listBySql(sql, args, null, clz, hasEntity);
	}

	@Override
	public List<Object> listBySql(String sql, Object arg, Class<Object> clz, boolean hasEntity) {
		return this.listBySql(sql, new Object[]{arg}, clz, hasEntity);
	}

	@Override
	public List<Object> listBySql(String sql, Class<Object> clz, boolean hasEntity) {
		return this.listBySql(sql, null, clz, hasEntity);
	}

	@Override
	public List<Object> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<Object> clz, boolean hasEntity) {
		sql = initSort(sql);
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		setAliasParameter(sqlQuery, alias);
		setParameter(sqlQuery, args);
		if(hasEntity){
			sqlQuery.addEntity(clz);
		}else{
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		}
		return sqlQuery.list();
	}

	@Override
	public List<Object> listByAliasSql(String sql, Map<String, Object> alias, Class<Object> clz, boolean hasEntity) {
		return this.listBySql(sql, null, alias, clz, hasEntity);
	}

	@Override
	public Pager<Object> findBySql(String sql, Object[] args, Class<Object> clz, boolean hasEntity) {
		return this.findBySql(sql, args, null, clz, hasEntity);
	}

	@Override
	public Pager<Object> findBySql(String sql, Object arg, Class<Object> clz, boolean hasEntity) {
		return this.findBySql(sql, new Object[]{arg}, clz, hasEntity);
	}

	@Override
	public Pager<Object> findBySql(String sql, Class<Object> clz, boolean hasEntity) {
		return this.findBySql(sql, null, clz, hasEntity);
	}

	@Override
	public Pager<Object> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<Object> clz, boolean hasEntity) {
		String countSql = getCountHql(sql,false);
		countSql = initSort(countSql);
		sql = initSort(sql);
		
		SQLQuery cquery = getSession().createSQLQuery(countSql);
		SQLQuery squery = getSession().createSQLQuery(sql);
		//设置别名参数
		setAliasParameter(squery, alias);
		setAliasParameter(cquery, alias);
		//设置参数
		setParameter(squery, args);
		setParameter(cquery, args);
		
		Pager<Object> pages = new Pager<Object>();
		setPagers(squery,pages);
		if(hasEntity){
			squery.addEntity(clz);
		}else{
			squery.setResultTransformer(Transformers.aliasToBean(clz));
		}
		
		List<Object> datas = squery.list();
		pages.setDatas(datas);
		long total = (Long) cquery.uniqueResult();
		pages.setTotal(total);
		return pages;
	}

	@Override
	public Pager<Object> findByAliasSql(String sql, Map<String, Object> alias, Class<Object> clz, boolean hasEntity) {
		return this.findBySql(sql, null, alias, clz, hasEntity);
	}

	

}
