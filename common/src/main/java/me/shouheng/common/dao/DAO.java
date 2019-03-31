package me.shouheng.common.dao;

import me.shouheng.common.exception.DAOException;
import me.shouheng.common.model.AbstractPO;
import me.shouheng.common.model.query.SearchObject;

import java.util.List;

/**
 * @author shouh, 2019/3/31-14:33
 */
public interface DAO<T extends AbstractPO> {

    Long createPO(T entity) throws DAOException;

    T createPOReturnObj(T entity) throws DAOException;

    List<Long> createPOBatch(List<T> entityList) throws DAOException;


    T getPO(Long primaryKey) throws DAOException;

    <E> E getVO(Long primaryKey) throws DAOException;

    List<T> searchPOs(SearchObject so) throws DAOException;

    <E> List<E> searchVOs(SearchObject so) throws DAOException;

    long searchPOsCount(SearchObject so) throws DAOException;


    int updatePO(T entity) throws DAOException;

    T updatePOReturnObj(T entity) throws DAOException;

    void updatePOBatch(List<T> entityList) throws DAOException;

    int updatePOSelective(T entity) throws DAOException;

    T updatePOSelectiveReturnObj(T entity) throws DAOException;


    void deletePOById(Long id) throws DAOException;

    void deletePOBatchById(List<Long> idList) throws DAOException;
}
