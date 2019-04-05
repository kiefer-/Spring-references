package me.shouheng.common.dao.dialect;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author shouh, 2019/3/31-18:17
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }
)})
public class SQLMapDialectInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(SQLMapDialectInterceptor.class);

    private static int MAPPED_STATEMENT_INDEX = 0;

    private static int PARAMETER_INDEX = 1;

    private static int ROWBOUNDS_INDEX = 2;

    static int RESULT_HANDLER_INDEX = 3;

    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    void processIntercept(final Object[] queryArgs) {
        // queryArgs = query(MappedStatement ms, Object parameter, RowBounds
        // rowBounds, ResultHandler resultHandler)
//        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
//        Object parameter = queryArgs[PARAMETER_INDEX];
//        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
//        int offset = rowBounds.getOffset();
//        int limit = rowBounds.getLimit();
//        BoundSql boundSql = ms.getBoundSql(parameter);
//        String sql = boundSql.getSql().trim();
//        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
//            if (dialect.supportsLimitOffset()) {
//                sql = dialect.getLimitString(sql, offset, limit);
//                offset = RowBounds.NO_ROW_OFFSET;
//            } else {
//                sql = dialect.getLimitString(sql, 0, limit);
//            }
//            limit = RowBounds.NO_ROW_LIMIT;
//
//            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
//            MetaObject mo = MetaObject.forObject(boundSql);
//            mo.setValue("sql", sql);
//            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(boundSql));
//            // BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
//            // boundSql.getParameterMappings(), boundSql.getParameterObject());
//            // MappedStatement newMs = copyFromMappedStatement(ms, new
//            // BoundSqlSqlSource(newBoundSql));
//            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
//        }
//        logger.debug("Thread: " + Thread.currentThread().getName() + " sql:" + sql);
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(ms.getKeyGenerator().toString());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        //add the flushCache
        builder.flushCacheRequired(true);
        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public static class BoundSqlSqlSource implements SqlSource {

        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
