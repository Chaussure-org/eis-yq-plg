package com.prolog.eis.util;


import org.apache.poi.ss.formula.functions.T;

import java.util.*;
import java.util.function.Function;

public class CollectionUtils {

    /**
     * 分组过滤
     * @param list
     * @param keyFunc
     * @param filterFunc
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> Map<T,List<S>> mapList(List<S> list,Function<S,T> keyFunc, Function<S,Boolean> filterFunc){
        Map<T,List<S>> map = new HashMap<>();

        list.forEach(item ->{
            List<S> templist = null;
            T key = keyFunc.apply(item);

            if(map.get(key)==null){
                templist = new ArrayList<>();
                map.put(key,templist);
            }else{
                templist=map.get(key);
            }

            if(filterFunc.apply(item)){
                templist.add(item);
            }
        });

        return map;
    }

    /**
     * 分组求和
     * @param list
     * @param keyFunc
     * @param valueFunc
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> Map<T,Long> mapSum(List<S> list,Function<S,T> keyFunc, Function<S,Long> valueFunc){
        Map<T,Long> map = new HashMap<>();
        list.forEach(item ->{
            T key = keyFunc.apply(item);
            Long v = valueFunc.apply(item);

            if(map.get(key)==null){
                map.put(key,v);
            }else{
                map.put(key,map.get(key)+v);
            }
        });
        return map;
    }

    /**
     * 分组求数量
     * @param list
     * @param keyFunc
     * @param filterFunc
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> Map<T,Long> mapCount(List<S> list,Function<S,T> keyFunc, Function<S,Boolean> filterFunc){
        Map<T,Long> map = new HashMap<>();
        for(int i=0;i< list.size();i++){
            T key = keyFunc.apply(list.get(i));
            boolean v = filterFunc.apply(list.get(i));

            if(map.get(key)==null){
                if(v){
                    map.put(key,1L);
                }else{
                    map.put(key,0L);
                }
            }else{
                map.put(key,map.get(key)+1);
            }
        }

        return map;
    }


    /**
     * 排序
     * @param list
     * @param func
     * @param asc
     * @param <T>
     * @return
     */
    public static <T> List<T> sort(List<T> list,Function<T,Integer> func,boolean asc){
        Collections.sort(list, (o1, o2)->{
            if(asc)
                return func.apply(o1) - func.apply(o2);
            else
                return  func.apply(o2) - func.apply(o1);
        });
        return list;
    }


    public static <T> List<T> findListHeader(List<T> list,Function<T,Integer> func){
       List<T> result = new ArrayList<>();
       T temp = null;
       for(int i=0;i<list.size();i++){
           if(i==0){
               temp = list.get(i);
               result.add(list.get(i));
           }else{
               if(func.apply(list.get(i)) == func.apply(temp)){
                   result.add(list.get(i));
               }else{
                   break;
               }
           }
        }
        return result;
    }


    /**
     * 自定义条件判断是否存在
     * @param collection
     * @param func
     * @param <T>
     * @return
     */
    public static <T> boolean contains(Collection<T> collection,Function<T,Boolean> func){
        for(T t : collection){
            if(func.apply(t)){
                return true;
            }
        }
        return false;
    }


    /**
     * 寻找最小集合
     * @param map
     * @param func
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K,T> K findSmallest(Map<K,T> map,Function<T,Integer> func){
        Iterator<K> itr = map.keySet().iterator();
        K tk=null;
        Integer v=null;
        while (itr.hasNext()){
            K k = itr.next();
            T t = map.get(k);
            if(tk==null){
                tk = k;
                v =  func.apply(t);
            }else if(v > func.apply(t)){
                tk = k;
                v = func.apply(t);
            }
        }
        return tk;
    }

    /**
     * 寻找最大集合
     * @param map
     * @param func
     * @param <K>
     * @param <T>
     * @return
     */
    public static <K,T> K findLargest(Map<K,T> map,Function<T,Integer> func){
        Iterator<K> itr = map.keySet().iterator();
        K tk=null;
        Integer v=null;
        while (itr.hasNext()){
            K k = itr.next();
            T t = map.get(k);
            if(tk==null){
                tk = k;
                v =  func.apply(t);
            }else if(v < func.apply(t)){
                tk = k;
                v = func.apply(t);
            }
        }
        return tk;
    }

    public  static <K> List<K> distinct(List<K> list,Function<K,Object> func){
        Iterator<K> itr = list.iterator();
        List<K> res = new ArrayList<>();
        while (itr.hasNext()){
            K item = itr.next();
            Object obj = func.apply(item);
            if(obj==null){
                if(!CollectionUtils.contains(res,x->func.apply(x)==null)){
                    res.add(item);
                }
            }else if(obj instanceof String){
                if(!CollectionUtils.contains(res,x->func.apply(x).equals(obj))){
                    res.add(item);
                }
            }else{
                if(!CollectionUtils.contains(res,x->func.apply(x)==obj)){
                    res.add(item);
                }
            }

        }
        return res;
    }
}
