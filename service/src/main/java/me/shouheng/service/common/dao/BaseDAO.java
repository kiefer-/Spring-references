package me.shouheng.service.common.dao;

import me.shouheng.common.dao.DAO;
import me.shouheng.common.exception.DAOException;
import me.shouheng.service.common.exception.OptLockException;
import me.shouheng.common.model.AbstractPO;
import me.shouheng.common.model.query.SearchObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shouh, 2019/3/31-15:40
 */
public abstract class BaseDAO<T extends AbstractPO> implements DAO<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);

    static {
        // 设置编码格式，防止中文乱码
        Resources.setCharset(Charset.forName("UTF-8"));
    }

    private static final String POSTFIX_SPLIT = ".";

    private static final String POSTFIX_INSERT = "insert";

    private static final String POSTFIX_UPDATE = "update";

    private static final String POSTFIX_SEARCH = "searchBySo";

    private static final String POSTFIX_SEARCH_VOS = "searchVosBySo";

    private static final String POSTFIX_SEARCH_COUNT = "searchCountBySo";

    private static final String POSTFIX_DELETE_PRIMARY_KEY = "deleteByPrimaryKey";

    private static final String POSTFIX_SELECT = "selectByPrimaryKey";

    private static final String POSTFIX_SELECT_VO = "selectVoByPrimaryKey";

    private static final String POSTFIX_UPDATE_PO_SELECTIVE = "updatePOSelective";

    private static final int DEFAULT_PAGE_SIZE = 1000;

    private static final int DEFAULT_CURRENT_PAGE = 1;

    protected static final String OPT_LOCK_EXCEPTION_ERROR = "This record modified by another thread before commit,please try again";

    protected Class<T> entityClass;

    public BaseDAO() {
        entityClass = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected SqlSession getSqlSession() {
        return SqlSessionHolder.getSession();
    }

    protected String getStatementPrefix() {
        return entityClass.getSimpleName() + POSTFIX_SPLIT;
    }

    @Override
    public Long createPO(T entity) throws DAOException {
        try {
            Long start = System.currentTimeMillis();
            getSqlSession().insert(getStatementPrefix() + POSTFIX_INSERT, entity);
            logger.debug("insert cost is :" + (System.currentTimeMillis() - start));
            return entity.getId();
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " createPO", e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Long> createPOBatch(List<T> entityList) throws DAOException {
        List<Long> rltList = new LinkedList<>();
        try {
            for (T entity : entityList) {
                getSqlSession().insert(getStatementPrefix() + POSTFIX_INSERT, entity);
            }
            getSqlSession().flushStatements();
            for (T entity : entityList) {
                rltList.add(entity.getId());
            }
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " createPOBatch", e);
            throw new DAOException(e);
        }
        return rltList;
    }

    @Override
    public T createPOReturnObj(T entity) throws DAOException {
        try {
            Long start = System.currentTimeMillis();
            getSqlSession().insert(getStatementPrefix() + POSTFIX_INSERT, entity);
            logger.debug("insert cost is :" + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            T ret = getPO(entity.getId());
            logger.debug("return created record cost is :" + (System.currentTimeMillis() - start));
            return ret;
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " createPOReturnObj", e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getPO(Long id) throws DAOException {
        try {
            return (T) getSqlSession().selectOne(getStatementPrefix() + POSTFIX_SELECT, id);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " getPO", e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E getVO(Long primaryKey) throws DAOException {
        try {
            return (E) getSqlSession().selectOne(getStatementPrefix() + POSTFIX_SELECT_VO, primaryKey);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " getVO", e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> searchPOs(SearchObject so) throws DAOException {
        return getPaginatedList(POSTFIX_SEARCH, so);
    }

    @Override
    public <E> List<E> searchVOs(SearchObject so) throws DAOException {
        return getPaginatedList(POSTFIX_SEARCH_VOS, so);
    }

    @Override
    public long searchPOsCount(SearchObject so) throws DAOException {
        try {
            return (Long) getSqlSession().selectOne(getStatementPrefix() + POSTFIX_SEARCH_COUNT, so);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " searchPOsCount", e);
            throw new DAOException(e);
        }
    }

    @Override
    public int updatePO(T entity) {
        int records = getSqlSession().update(getStatementPrefix() + POSTFIX_UPDATE, entity);
        if (records == 0 || records == -1) {
            throw new OptLockException(OPT_LOCK_EXCEPTION_ERROR);
        }
        return records;
    }

    @Override
    public void updatePOBatch(List<T> entityList) throws OptLockException {
        try {
            for (T entity : entityList) {
                getSqlSession().update(getStatementPrefix() + POSTFIX_UPDATE, entity);
            }
            getSqlSession().flushStatements();
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " updatePOBatch", e);
            throw new DAOException(e);
        }
    }

    @Override
    public T updatePOReturnObj(T entity) {
        int records = getSqlSession().update(getStatementPrefix() + POSTFIX_UPDATE, entity);
        if (records == 0 || records == -1) {
            throw new OptLockException(OPT_LOCK_EXCEPTION_ERROR);
        }
        return getPO(entity.getId());
    }

    @Override
    public int updatePOSelective(T entity) throws DAOException {
        int records = getSqlSession().update(getStatementPrefix() + POSTFIX_UPDATE_PO_SELECTIVE, entity);
        if (records == 0 || records == -1) {
            throw new OptLockException(OPT_LOCK_EXCEPTION_ERROR);
        }
        return records;
    }

    @Override
    public T updatePOSelectiveReturnObj(T entity) {
        int records = getSqlSession().update(getStatementPrefix() + POSTFIX_UPDATE_PO_SELECTIVE, entity);
        if (records == 0 || records == -1) {
            throw new OptLockException(OPT_LOCK_EXCEPTION_ERROR);
        }
        return getPO(entity.getId());
    }

    @Override
    public void deletePOById(Long id) throws DAOException {
        try {
            getSqlSession().delete(getStatementPrefix() + POSTFIX_DELETE_PRIMARY_KEY, id);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " deletePOById", e);
            throw new DAOException(e);
        }
    }

    @Override
    public void deletePOBatchById(List<Long> idList) throws DAOException {
        try {
            for (Long id : idList) {
                getSqlSession().delete(getStatementPrefix() + POSTFIX_DELETE_PRIMARY_KEY, id);
            }
            getSqlSession().flushStatements();
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " deletePOBatchById", e);
            throw new DAOException(e);
        }
    }

    public void manualCommit() {
        try {
            this.getSqlSession().getConnection().commit();
        } catch (SQLException e) {
            logger.error("manual commint failure", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <E> List<E> getQueryListByStatement(String statementId, Object params) throws DAOException {
        try {
            return (List<E>) getSqlSession().selectList(getStatementPrefix() + statementId, params);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " queryForList by object", e);
            throw new DAOException(e);
        }
    }

    protected <E> E getSingleModelByStatement(String statementId, Object params) throws DAOException {
        try {

            List<E> ret = getQueryListByStatement(statementId, params);
            if (ret.size() > 0) {
                return ret.get(0);
            }
            return null;
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " queryForObject by object", e);
            throw new DAOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <E> List<E> getPaginatedList(String statementId, SearchObject so) throws DAOException {
        try {
            initPageSize(so);
            RowBounds rowBounds = new RowBounds((so.getCurrentPage() - 1) * so.getPageSize(), so.getPageSize());
            return (List<E>) getSqlSession().selectList(getStatementPrefix() + statementId, so, rowBounds);
        } catch (Exception e) {
            logger.error(getStatementPrefix() + " queryForVoList by so", e);
            throw new DAOException(e);
        }
    }

    private void initPageSize(SearchObject so) {
        int pageSize = so.getPageSize();
        int currentPage = so.getCurrentPage();
        so.setCurrentPage(currentPage < 1 ? DEFAULT_CURRENT_PAGE : currentPage);
        so.setPageSize(pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize);
    }

}
