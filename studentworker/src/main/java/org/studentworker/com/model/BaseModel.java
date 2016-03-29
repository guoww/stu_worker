package org.studentworker.com.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.studentworker.com.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public class BaseModel extends Model<Model>{
	
	public Model BindModel(Controller cr, Model model) {
		Table table = TableMapping.me().getTable(model.getClass());
		Set<Entry<String, Class<?>>> colTypeEntry = table.getColumnTypeMapEntrySet();
		Iterator<Entry<String, Class<?>>> it = colTypeEntry.iterator();
		while(it.hasNext()){
			Entry<String, Class<?>> next = it.next();
			String key = next.getKey();
			Class<?> javaType = next.getValue();
			String value = cr.getPara(key);
			if(value!=null&&!"".equals(value)){
				setAttrByType(key,javaType,cr,model);
			}
		}
		return model;
	}

	private void setAttrByType(String key , Class<?> javaType,Controller ai ,Model model) {
		// TODO Auto-generated method stub
		if(Integer.class.equals(javaType.getClass())){
			model.set(key, ai.getParaToInt(key));
		}else if(Long.class.equals(javaType.getClass())){
			model.set(key, ai.getParaToLong(key));
		}else if(Date.class.equals(javaType.getClass())){
			model.set(key, ai.getParaToDate(key));
		}else{
			model.set(key, ai.getPara(key));
		}
	}
	
	public boolean save(){
		Table table = TableMapping.me().getTable(this.getClass());
		String[] primaryKey = table.getPrimaryKey();
		String id = this.getStr(primaryKey[0]);
		if(id==null||"".equals(id)){
			this.remove(primaryKey[0]);
			return super.save();
		}else{
			this.set(primaryKey[0], id);
			return super.update();
		}
	}
	
	public Table getTable(Class modelClass){
		return TableMapping.me().getTable(modelClass);
	}
	
	public List<Model> findListByAttrs(Map<String,Object> attrs,Class<? extends BaseModel> modelClass) throws Exception{
		BaseModel model = modelClass.newInstance();
		String sql = buildFindByAttrsSql(attrs,modelClass);
		return findListByAttrs(attrs,sql,modelClass);
	}
	
	public List<Model> findListByAttrs(Map<String,Object> attrs,String sql,Class<? extends BaseModel> modelClass) throws Exception{
		BaseModel model = modelClass.newInstance();
		return model.find(sql);
	}
	
	public Model findFirstByAttrs(Map<String,Object> attrs,Class<? extends BaseModel> modelClass) throws Exception{
		BaseModel model = modelClass.newInstance();
		String sql = buildFindByAttrsSql(attrs,modelClass);
		List<Model> list = findListByAttrs(attrs,sql,modelClass);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public String buildFindByAttrsSql(Map<String,Object> map , Class<? extends BaseModel> modelClass){
		Table table = this.getTable(modelClass);
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from "+table.getName()+" where 1=1 \n");
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if(StringUtil.isNotEmptyORNull(key)&&value!=null){
				sb.append(" and "+key+"="+value+" \n");
			}
		}
		return sb.toString();
	}
}
