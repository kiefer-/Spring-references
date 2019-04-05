package me.shouheng.common.dao;

import me.shouheng.common.exception.DAOException;
import me.shouheng.common.model.AbstractPO;
import me.shouheng.common.model.query.SearchObject;

import java.util.List;

/**
 * @author shouh, 2019/3/31-14:33
 */
public interface DAO<T extends AbstractPO> {

    void insert(T entity) throws DAOException;

    int update(T entity) throws DAOException;

    int updatePOSelective(T entity) throws DAOException;

    List<T> searchBySo(SearchObject so) throws DAOException;

    <E> List<E> searchVosBySo(SearchObject so) throws DAOException;

    long searchCountBySo(SearchObject so) throws DAOException;

    void deleteByPrimaryKey(Long id) throws DAOException;

    T selectByPrimaryKey(Long id) throws DAOException;

    T selectVoByPrimaryKey(Long id) throws DAOException;

}
