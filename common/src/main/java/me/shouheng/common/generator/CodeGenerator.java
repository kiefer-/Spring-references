package me.shouheng.common.generator;

import me.shouheng.common.util.TextUtils;

import javax.persistence.Table;
import java.io.*;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * 用于生成模板代码的工具类，如何使用：
 * 1. 拷贝 PO 类拷贝到与当前类相同的包下面；
 * 2. 执行 main 方法，然后按照提示输入指定类的路径和输出文件的路径。
 *
 * 比如，将类 Task 拷贝到 generator 包下面，在提示 "请输入 PO 类的全路径包含类名:"
 * 时输入 "me.shouheng.common.generator.Task"；
 * 然后在提示 "请输入输出文件路径:" 时，输入文件的输出路径，比如 "F:\gen"。
 * 程序自动执行文件生成逻辑之后，到 "F:\gen" 目录下面找到程序为自己生成的文件并将其拷贝到自己的项目中即可。
 *
 * @author shouh, 2019/3/31-10:38
 */
public final class CodeGenerator {

    private final static String DIR_SPLIT = System.getProperty("file.separator");
    private static final String LINE_SPLIT = System.getProperty("line.separator");

    private static final String VO_SUFFIX = "Vo";
    private static final String SO_SUFFIX = "So";
    private static final String PACK_VO_PREFIX = "Pack";
    private static final String PACK_VO_SUFFIX = "Vo";
    private static final String MAPPER_SUFFIX = "Mapper";
    private static final String TEST_CLASS_SUFFIX = "ServiceTest";
    private final static String DEFAULT_OUTPUT_DIR = "D:" + DIR_SPLIT + "DDLFiles";
    private static final String DAO_INTERFACE_SUFFIX = "DAO";
    private static final String DAO_IMPL_SUFFIX = "DAOImpl";
    private static final String SERVICE_INTERFACE_SUFFIX = "Service";
    private static final String SERVICE_IMPL_SUFFIX = "ServiceImpl";
    private static final String CREATE_METHOD_NAME_PREFIX = "create";
    private static final String DELETE_METHOD_NAME_PREFIX = "delete";
    private static final String GET_METHOD_NAME_PREFIX = "get";
    private static final String SEARCH_METHOD_NAME_PREFIX = "search";
    private static final String UPDATE_METHOD_NAME_PREFIX = "update";
    private static final String JAVA_FILE_SUFFIX = ".java";
    private static final String SQL_SUFFIX = ".sql";
    private static final String XML_SUFFIX = ".xml";
    private static final String TEST_ANNOTATION = "@Test";
    private static final String REMARK_UPDATED_STR = "remark was updated";

    private static String OUTPUT_DIR;
    private static String PO_CLASS_PATH;
    private static String PO_CLASS_SIMPLE_NAME;
    private static String VO_CLASS_SIMPLE_NAME;
    private static String PACK_VO_CLASS_SIMPLE_NAME;
    private static String SO_CLASS_SIMPLE_NAME;
    private static String DAO_INTERFACE_CLASS_NAME;
    private static String DAO_IMPL_CLASS_NAME;
    private static String SERVICE_INTERFACE_CLASS_NAME;
    private static String SERVICE_IMPL_CLASS_NAME;
    private static String BLANK_FOR_METHOD = "    ";
    private static String BLANK_FOR_METHOD_BODY = "        ";
    private static String BLANK_FOR_PARAM = " ";
    private static String COMMA = " , ";
    private static List<JPAHelper.ColumnModel> columnModels = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        inputAllParam();

        Class clazz = Class.forName(PO_CLASS_PATH);
        columnModels = JPAHelper.getColumnModels(clazz);

        initAllComponentNames(clazz);

        printAllComponent(clazz);
    }


    /**
     * 初始化 PO 信息和输入、输出目录
     *
     * @throws IOException IO 异常
     * @throws ClassNotFoundException 类异常
     */
    private static void inputAllParam() throws IOException, ClassNotFoundException {
        PO_CLASS_PATH = getInputPath("请输入 PO 类的全路径包含类名:").trim();

        Class clazz = Class.forName(PO_CLASS_PATH);

        OUTPUT_DIR = getInputPath("请输入输出文件路径:").trim();
        OUTPUT_DIR = TextUtils.isEmpty(OUTPUT_DIR) ? DEFAULT_OUTPUT_DIR : OUTPUT_DIR;
        OUTPUT_DIR += DIR_SPLIT + clazz.getSimpleName();

        File outPutDir = new File(OUTPUT_DIR);
        if (!outPutDir.exists()) {
            boolean createDirSuccess = outPutDir.mkdirs();
            System.out.println((createDirSuccess ? "创建目录成功: " : "创建目录失败: ") + outPutDir);
        }

        System.out.println();
    }

    /**
     * 初始化各个文件的名称（类名）
     *
     * @param clazz PO 类
     */
    private static void initAllComponentNames(Class clazz) {
        PO_CLASS_SIMPLE_NAME = clazz.getSimpleName();
        VO_CLASS_SIMPLE_NAME = PO_CLASS_SIMPLE_NAME + VO_SUFFIX;
        SO_CLASS_SIMPLE_NAME = PO_CLASS_SIMPLE_NAME + SO_SUFFIX;
        PACK_VO_CLASS_SIMPLE_NAME = PACK_VO_PREFIX + PO_CLASS_SIMPLE_NAME + PACK_VO_SUFFIX;
        DAO_INTERFACE_CLASS_NAME = PO_CLASS_SIMPLE_NAME + DAO_INTERFACE_SUFFIX;
        SERVICE_INTERFACE_CLASS_NAME = PO_CLASS_SIMPLE_NAME + SERVICE_INTERFACE_SUFFIX;
        DAO_IMPL_CLASS_NAME = PO_CLASS_SIMPLE_NAME + DAO_IMPL_SUFFIX;
        SERVICE_IMPL_CLASS_NAME = PO_CLASS_SIMPLE_NAME + SERVICE_IMPL_SUFFIX;
    }

    /**
     * 输出创建的文件并打印文件信息
     *
     * @param clazz PO 类
     */
    private static void printAllComponent(Class clazz) {
        String sqlContent = getSqlContent(clazz);
        outputContent2File(OUTPUT_DIR, PO_CLASS_SIMPLE_NAME + SQL_SUFFIX, sqlContent);

        String mapperContent = getMapper(clazz);
        outputContent2File(OUTPUT_DIR, PO_CLASS_SIMPLE_NAME + MAPPER_SUFFIX + XML_SUFFIX, mapperContent);

        String serviceInterfaceContent = getServiceInterfaceContent();
        outputContent2File(OUTPUT_DIR, SERVICE_INTERFACE_CLASS_NAME + JAVA_FILE_SUFFIX, serviceInterfaceContent);

        String serviceImplContent = getServiceImplContent();
        outputContent2File(OUTPUT_DIR, SERVICE_IMPL_CLASS_NAME + JAVA_FILE_SUFFIX, serviceImplContent);

        String packVoContent = getPackVoContent(clazz);
        outputContent2File(OUTPUT_DIR, PACK_VO_CLASS_SIMPLE_NAME + JAVA_FILE_SUFFIX, packVoContent);

        String daoInterfaceContent = getDAOInterfaceContent();
        outputContent2File(OUTPUT_DIR, DAO_INTERFACE_CLASS_NAME + JAVA_FILE_SUFFIX, daoInterfaceContent);

        String daoImplContent = getDAOImplContent();
        outputContent2File(OUTPUT_DIR, DAO_IMPL_CLASS_NAME + JAVA_FILE_SUFFIX, daoImplContent);

        String soContent = getSearchSoContent();
        outputContent2File(OUTPUT_DIR, SO_CLASS_SIMPLE_NAME + JAVA_FILE_SUFFIX, soContent);

        String testClassContent = getTestClassContent();
        outputContent2File(OUTPUT_DIR, PO_CLASS_SIMPLE_NAME + TEST_CLASS_SUFFIX + JAVA_FILE_SUFFIX, testClassContent);
    }


    /**
     * 获取用于创建 Mybatis Mapper 文件的字符串
     *
     * @param clazz PO 类
     * @return Mapper 文件内容
     */
    private static String getMapper(Class clazz) {
        String tableName = JPAHelper.getTableName(clazz);
        String typeName = clazz.getSimpleName();

        // typeAlias
        System.out.println("<typeAlias alias=\"" + typeName + "\" type=\"" + clazz.getName() + "\"/>");
        System.out.println();

        String indentation1 = "    ";
        String indentation2 = "        ";
        String indentation3 = "            ";

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LINE_SPLIT);
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" " + LINE_SPLIT);
        sb.append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + LINE_SPLIT);
        sb.append("<mapper namespace=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // insert
        sb.append(indentation1 + "<insert id=\"insert\" parameterType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "insert into " + tableName + "(\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (k == size-1) {
                sb.append(indentation2 + "<!-- " + k + "-->" + model.column + LINE_SPLIT);
            } else {
                sb.append(indentation2 + "<!-- " + k + "-->" + model.column + ",\n");
            }
        }
        sb.append(indentation2 + ")\n");
        sb.append(indentation2 + "values(\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (k == size-1) {
                sb.append(indentation2 + "<!-- " + k + "-->#{" + model.prop + ":" + model.types + "}\n");
            } else {
                sb.append(indentation2 + "<!-- " + k + "-->#{" + model.prop + ":" + model.types + "},\n");
            }
        }
        sb.append(indentation2 + ")\n");
        // 将主键信息更新到数据记录上面，这里的主键转换成 Long 类型的
        sb.append(indentation2 + "<selectKey resultType=\"java.lang.Long\" order=\"AFTER\" keyProperty=\"id\">\n");
        sb.append(indentation3 + "select last_insert_id() as id\n");
        sb.append(indentation2 + "</selectKey>\n");
        sb.append(indentation1 + "</insert>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // update
        sb.append(indentation1 + "<update id=\"update\" parameterType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "update " + tableName + " set\n");
        for (int k=0, size=columnModels.size(); k<size; k++) {
            JPAHelper.ColumnModel model = columnModels.get(k);
            if (!model.column.equals("lock_version") && !model.column.equals("id")) {
                sb.append(indentation3 + model.column + "=#{" + model.prop + ":" + model.types + "},\n");
            }
        }
        sb.append(indentation3 + "LOCK_VERSION = LOCK_VERSION+1\n");
        sb.append(indentation3 + "where ID=#{id} and LOCK_VERSION=#{lockVersion} \n");
        sb.append(indentation1 + "</update>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // updatePOSelective
        printUpdatePOSelective(sb, tableName, typeName);

        // selectByPrimaryKey
        sb.append(indentation1 + "<select id=\"selectByPrimaryKey\" parameterType=\"long\" resultType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select * from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // selectVoByPrimaryKey
        sb.append(indentation1 + "<select id=\"selectVoByPrimaryKey\" parameterType=\"long\" resultType=\"" + typeName + "Vo" + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select * from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchBySo
        sb.append(indentation1 + "<select id=\"searchBySo\" resultType=\"" + typeName + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select t.* from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchVosBySo
        sb.append(indentation1 + "<select id=\"searchVosBySo\" resultType=\"" + typeName + "Vo" + "\">" + LINE_SPLIT);
        sb.append(indentation2 + "select t.* from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // searchCountBySo
        sb.append(indentation1 + "<select id=\"searchCountBySo\" resultType=\"long\">" + LINE_SPLIT);
        sb.append(indentation2 + "select count(t.id) from " + tableName + " t\n");
        sb.append(indentation2 + "<include refid=\"SO_Where_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</select>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);

        // SO_Where_Clause
        sb.append(indentation1 + "<sql id=\"SO_Where_Clause\">" + LINE_SPLIT);
        sb.append(indentation2 + "<where>" + LINE_SPLIT);
        sb.append("" + LINE_SPLIT);
        sb.append(indentation2 + "</where>" + LINE_SPLIT);
        sb.append(indentation2 + "<include refid=\"Base.Order_By_Clause\" />" + LINE_SPLIT);
        sb.append(indentation1 + "</sql>" + LINE_SPLIT);

        // deleteByPrimaryKey
        sb.append(indentation1 + "<delete id=\"deleteByPrimaryKey\" parameterType=\"long\">" + LINE_SPLIT);
        sb.append(indentation2 + "delete from " + tableName + " where id=#{id}" + LINE_SPLIT);
        sb.append(indentation1 + "</delete>" + LINE_SPLIT);

        sb.append("</mapper>" + LINE_SPLIT);
        System.out.println(sb.toString());
        return sb.toString();
    }

    private static void printUpdatePOSelective(StringBuilder sb, String table, String typeName) {
        sb.append(MessageFormat.format("    <update id=\"updatePOSelective\" parameterType=\"{0}\">", typeName) + LINE_SPLIT);
        sb.append("        update " + table + LINE_SPLIT);
        sb.append("        <set>" + LINE_SPLIT);

        for (JPAHelper.ColumnModel model : columnModels) {
            if (!"lock_version".equals(model.column) && !"id".equals(model.column)) {
                sb.append("            " + MessageFormat.format("<if test=\"{0} != null \">", model.prop) + LINE_SPLIT);
                sb.append("                " + model.column + "=#{" + model.prop + ":" + model.types + "}" + COMMA + LINE_SPLIT);
                sb.append("            </if>" + LINE_SPLIT);
            }
        }

        sb.append("            LOCK_VERSION = LOCK_VERSION+1" + LINE_SPLIT);
        sb.append("        </set>" + LINE_SPLIT);
        sb.append("            where ID=#{id} and LOCK_VERSION=#{lockVersion} " + LINE_SPLIT);

        sb.append("    </update>").append(LINE_SPLIT);
        sb.append(LINE_SPLIT);
    }


    /**
     * 获取完整的用于创建表的 SQL
     *
     * @param clazz PO 类
     * @return 表创建 SQL，表 + 主键
     */
    private static String getSqlContent(Class clazz) {
        return table(clazz) + primaryKey(clazz);
    }

    /**
     * 获取用于创建数据库表的 SQL
     *
     * @param clazz PO 类
     * @return SQL
     */
    private static String table(Class clazz) {
        StringBuilder sql = new StringBuilder();

        String tableName = JPAHelper.getTableName(clazz);
        sql.append("--创建表 " + tableName + LINE_SPLIT);
        sql.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        JPAHelper.ColumnModel columnModel;
        for (int i=0, size=columnModels.size(); i<size; i++) {
            columnModel = columnModels.get(i);
            if (i == (size - 1)) {
                sql.append(LINE_SPLIT + columnModel.column + " " + columnModel.columnType +  ")");
            } else {
                sql.append(LINE_SPLIT + columnModel.column + " " + columnModel.columnType +  ",");
            }
        }
        sql.append(";");
        sql.append(LINE_SPLIT);
        System.out.println(sql.toString());
        return sql.toString();
    }

    /**
     * 获取指定 PO 类的主键创建 SQL
     *
     * @param clazz PO 类
     * @return 主键创建 SQL
     */
    private static String primaryKey(Class clazz) {
        StringBuilder key = new StringBuilder();
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            String tableName = table.name();
            key.append("--添加主键 " + key + LINE_SPLIT);
            key.append("ALTER TABLE " + tableName + " MODIFY id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT;");
            key.append(LINE_SPLIT);
        }
        System.out.println(key.toString());
        return key.toString();
    }


    private static String getTestClassContent() {
        StringBuilder sb = new StringBuilder();

        String testAnnotationStartStr = TEST_ANNOTATION;

        // start
        sb.append(String.format("public class %sServiceTest extends SpringBaseTest{", PO_CLASS_SIMPLE_NAME));
        sb.append(LINE_SPLIT);
        sb.append(LINE_SPLIT);

        sb.append(fillBlankForString("@Autowired"));
        sb.append(LINE_SPLIT);
        sb.append(fillBlankForString(String.format("private %s %s;", SERVICE_INTERFACE_CLASS_NAME,
                convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME))));
        sb.append(LINE_SPLIT);

        // create entry method
        sb.append(LINE_SPLIT);
        String createEntryMethodHeader = generateMethodHeader(MethodAccess.PUBLIC,
                VO_CLASS_SIMPLE_NAME, "create", null, null);
        String createEntryMethodBody = getCreateEntryMethodBodyForTest();
        sb.append(generateEntireMethod(createEntryMethodHeader, createEntryMethodBody));
        sb.append(LINE_SPLIT);

        // testCreate method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String createMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, "void", "testCreate", null, null);
        String createMethodBody = getCreateMethodBodyForTest();
        sb.append(generateEntireMethod(createMethodHeader, createMethodBody));
        sb.append(LINE_SPLIT);

        // testSearch method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testSearchMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, "void", "testSearch", null, null);
        String testSearchMethodBody = getTestSearchMethodBodyForTest();
        sb.append(generateEntireMethod(testSearchMethodHeader, testSearchMethodBody));
        sb.append(LINE_SPLIT);

        // testSearchCount method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testSearchCountMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, "void", "testSearchCount", null, null);
        String testSearchCountMethodBody = getTestSearchCountMethodBodyForTest();
        sb.append(generateEntireMethod(testSearchCountMethodHeader, testSearchCountMethodBody));
        sb.append(LINE_SPLIT);

        // testUpdate method
        sb.append(LINE_SPLIT);
        sb.append(BLANK_FOR_METHOD + testAnnotationStartStr + LINE_SPLIT);
        String testUpdateMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, "void", "testUpdate", null, null);
        String testUpdateMethodBody = getUpdateMethodBodyForTest();
        sb.append(generateEntireMethod(testUpdateMethodHeader, testUpdateMethodBody));
        sb.append(LINE_SPLIT);
        sb.append(LINE_SPLIT);

        // end
        sb.append("}");
        sb.append(LINE_SPLIT);
        return sb.toString();
    }

    private static String getUpdateMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", VO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("%s voTemp = %s.get%s(vo.getId()).getVo();",
                        VO_CLASS_SIMPLE_NAME, convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME), PO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("voTemp.setRemark(\"%s\");", REMARK_UPDATED_STR)) +
                getMethodBodyLineByFormatContent(String.format("%s updateRes = %s.update%s(voTemp).getVo();",
                        VO_CLASS_SIMPLE_NAME, convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME), PO_CLASS_SIMPLE_NAME)) +
                fillBlankForMethodBody(String.format(
                        "Assert.assertTrue(updateRes !=null && \"%s\".equals(updateRes.getRemark()));", REMARK_UPDATED_STR));
    }

    private static String getTestSearchCountMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", VO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("%s so = new %s();", SO_CLASS_SIMPLE_NAME,
                        SO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("long count = %s.search%sCount(so).getUdf1();",
                        convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME), PO_CLASS_SIMPLE_NAME)) +
                fillBlankForMethodBody("Assert.assertTrue(count > 0);");
    }

    private static String getTestSearchMethodBodyForTest() {
        return getMethodBodyLineByFormatContent(String.format("%s vo = this.create();", VO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("%s so = new %s();", SO_CLASS_SIMPLE_NAME,
                        SO_CLASS_SIMPLE_NAME)) +
                getMethodBodyLineByFormatContent(String.format("List<%s> voList = %s.search%s(so).getVoList();",
                        VO_CLASS_SIMPLE_NAME, convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME), PO_CLASS_SIMPLE_NAME)) +
                fillBlankForMethodBody("Assert.assertTrue(voList != null && voList.size() > 0);");
    }

    private static String getMethodBodyLineByFormatContent(String s) {
        String res = fillBlankForMethodBody(s);
        res += LINE_SPLIT;
        return res;
    }

    private static String getCreateMethodBodyForTest() {
        return fillBlankForMethodBody(String.format("%s vo = this.create();", VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody("Assert.assertTrue(vo!= null);");
    }

    private static String getCreateEntryMethodBodyForTest() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(fillBlankForMethodBody(String.format("%s vo = MockTestUtil.getJavaBean(%s.class);", VO_CLASS_SIMPLE_NAME,
                VO_CLASS_SIMPLE_NAME)));
        buffer.append(LINE_SPLIT);

        buffer.append(fillBlankForMethodBody(String.format("return %s.%s(vo).getVo();",
                convertFirstLetterToLower(SERVICE_INTERFACE_CLASS_NAME), CREATE_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME)));
        return buffer.toString();
    }


    private static String getDAOInterfaceContent() {
        return String.format("public interface %s extends DAO<%s>{", DAO_INTERFACE_CLASS_NAME, PO_CLASS_SIMPLE_NAME) +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }

    private static String getDAOImplContent() {
        return "@Repository" +
                LINE_SPLIT +
                String.format("public class %s extends BaseDAO<%s> implements %s{", DAO_IMPL_CLASS_NAME,
                        PO_CLASS_SIMPLE_NAME, DAO_INTERFACE_CLASS_NAME) +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }


    private static String getSearchSoContent() {
        return "@Repository" +
                LINE_SPLIT +
                String.format("public class %s extends SearchObject {", SO_CLASS_SIMPLE_NAME) +
                LINE_SPLIT +
                fillBlankForString("private static final long serialVersionUID = 1L;") +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }


    private static String getServiceInterfaceContent() {
        return String.format("public interface %s {", SERVICE_INTERFACE_CLASS_NAME) +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        CREATE_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME, VO_CLASS_SIMPLE_NAME, "vo") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        GET_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME, "Long", "primaryKey") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        UPDATE_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME, VO_CLASS_SIMPLE_NAME, "vo") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        DELETE_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME, "Long", "primaryKey") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        SEARCH_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME, SO_CLASS_SIMPLE_NAME, "so") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                fillBlankForString(generateMethodHeader(MethodAccess.DEFAULT, PACK_VO_CLASS_SIMPLE_NAME,
                        SEARCH_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME + "Count", SO_CLASS_SIMPLE_NAME, "so") + ";") +
                LINE_SPLIT +
                LINE_SPLIT +
                "}" +
                LINE_SPLIT;
    }

    private static String getServiceImplContent() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(String.format("@Service(\"%sService\")", convertFirstLetterToLower(PO_CLASS_SIMPLE_NAME)));
        buffer.append(LINE_SPLIT);

        // start
        buffer.append(String.format("public class %s implements %s {", SERVICE_IMPL_CLASS_NAME, SERVICE_INTERFACE_CLASS_NAME));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // autowire some class
        fillAutowireClassForServiceImpl(buffer);

        // create
        buffer.append(LINE_SPLIT);
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String createMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, CREATE_METHOD_NAME_PREFIX
                + PO_CLASS_SIMPLE_NAME, VO_CLASS_SIMPLE_NAME, "vo");
        String createMethodBody = getCreateMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(createMethodHeader, createMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // get
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String getMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, GET_METHOD_NAME_PREFIX + PO_CLASS_SIMPLE_NAME,
                "Long", "primaryKey");
        String getMethodBody = getGetMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(getMethodHeader, getMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // update
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String updateMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, UPDATE_METHOD_NAME_PREFIX
                + PO_CLASS_SIMPLE_NAME, VO_CLASS_SIMPLE_NAME, "vo");
        String updateMethodBody = getUpdateMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(updateMethodHeader, updateMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // delete
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String deleteMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, DELETE_METHOD_NAME_PREFIX
                + PO_CLASS_SIMPLE_NAME, "Long", "primaryKey");
        String deleteMethodBody = getDeleteMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(deleteMethodHeader, deleteMethodBody));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // search
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String searchMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, SEARCH_METHOD_NAME_PREFIX
                + PO_CLASS_SIMPLE_NAME, SO_CLASS_SIMPLE_NAME, "so");
        String searchMethodBody = getSearchMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(searchMethodHeader, searchMethodBody));

        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // searchCount
        buffer.append(BLANK_FOR_METHOD + "@Override" + LINE_SPLIT);
        String searchCountMethodHeader = generateMethodHeader(MethodAccess.PUBLIC, PACK_VO_CLASS_SIMPLE_NAME, SEARCH_METHOD_NAME_PREFIX
                + PO_CLASS_SIMPLE_NAME + "Count", SO_CLASS_SIMPLE_NAME, "so");
        String searchCountMethodBody = getSearchCountMethodBodyForServiceImpl();
        buffer.append(generateEntireMethod(searchCountMethodHeader, searchCountMethodBody));

        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        // end
        buffer.append("}");
        buffer.append(LINE_SPLIT);

        return buffer.toString();
    }

    private static String getCreateMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s entity = dozerBeanUtil.convert(vo, %s.class);", PO_CLASS_SIMPLE_NAME,
                        PO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s returnEntity = %s.createPOReturnObj(entity);", PO_CLASS_SIMPLE_NAME,
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(dozerBeanUtil.convert(returnEntity, %s.class));",
                        VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getGetMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s returnEntity = %s.getPO(primaryKey);", PO_CLASS_SIMPLE_NAME,
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(dozerBeanUtil.convert(returnEntity, %s.class));",
                        VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getSearchMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("List<%s> voList = %s.searchVOs(so);", VO_CLASS_SIMPLE_NAME,
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody("packVo.setVoList(voList);") +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getSearchCountMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("Long count = %s.searchPOsCount(so);",
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody("packVo.setUdf1(count);") +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static String getUpdateMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s po = dozerBeanUtil.convert(vo, %s.class);", PO_CLASS_SIMPLE_NAME,
                        PO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s returnEntity = %s.updatePOReturnObj(po);", PO_CLASS_SIMPLE_NAME,
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("packVo.setVo(dozerBeanUtil.convert(returnEntity, %s.class));",
                        VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }

    private static void fillAutowireClassForServiceImpl(StringBuffer buffer) {
        buffer.append(fillBlankForString(String.format("private Logger logger = LoggerFactory.getLogger(%s.class);",
                SERVICE_IMPL_CLASS_NAME)));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        String daoFieldName = convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME);
        buffer.append(fillBlankForString(String.format("private %s %s;", DAO_INTERFACE_CLASS_NAME, daoFieldName)));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        buffer.append(fillBlankForString("private DozerBeanUtil dozerBeanUtil;"));
        buffer.append(LINE_SPLIT);
        buffer.append(LINE_SPLIT);

        buffer.append(fillBlankForString("@Autowired"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForString("public " + SERVICE_IMPL_CLASS_NAME + "(" +
                String.format("%s %s, ", DAO_INTERFACE_CLASS_NAME, daoFieldName) + "DozerBeanUtil dozerBeanUtil) {"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForMethodBody(String.format("this.%s = %s;", daoFieldName, daoFieldName)));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForMethodBody("this.dozerBeanUtil = dozerBeanUtil;"));
        buffer.append(LINE_SPLIT);
        buffer.append(fillBlankForString("}"));
        buffer.append(LINE_SPLIT);
    }

    private static String getDeleteMethodBodyForServiceImpl() {
        return fillBlankForMethodBody(String.format("%s packVo = new %s();", PACK_VO_CLASS_SIMPLE_NAME,
                PACK_VO_CLASS_SIMPLE_NAME)) +
                LINE_SPLIT +
                fillBlankForMethodBody(String.format("%s.deletePOById(primaryKey);",
                        convertFirstLetterToLower(DAO_INTERFACE_CLASS_NAME))) +
                LINE_SPLIT +
                fillBlankForMethodBody("return packVo;");
    }


    private static String getPackVoContent(Class clazz) {
        String className = clazz.getSimpleName();
        return String.format("public class Pack%sVo extends AbstractPackVo{", className) +
                LINE_SPLIT +
                String.format("    private static final long serialVersionUID = 1L;", className) +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    private %sVo vo;", className) +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    private List<%sVo> voList;", className) +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    public %sVo getVo() {", className) +
                LINE_SPLIT +
                "        return vo;" +
                LINE_SPLIT +
                "    }" +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    public void setVo(%sVo vo) {", className) +
                LINE_SPLIT +
                "        this.vo = vo;" +
                LINE_SPLIT +
                "    }" +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    public List<%sVo> getVoList() {", className) +
                LINE_SPLIT +
                "        return voList;" +
                LINE_SPLIT +
                "    }" +
                LINE_SPLIT +
                LINE_SPLIT +
                String.format("    public void setVoList(List<%sVo> voList) {", className) +
                LINE_SPLIT +
                "        this.voList = voList;" +
                LINE_SPLIT +
                "    }" +
                LINE_SPLIT +
                LINE_SPLIT +
                "}";
    }


    private static String generateEntireMethod(String methodHeader, String methodBody) {
        return fillBlankForString(methodHeader) + "{" +
                LINE_SPLIT +
                methodBody +
                LINE_SPLIT +
                fillBlankForString("}");
    }

    private static String  fillBlankForMethodBody(String s) {
        return BLANK_FOR_METHOD_BODY + s;
    }

    private static String fillBlankForString(String s) {
        return BLANK_FOR_METHOD + s;
    }

    private static String convertFirstLetterToLower(String className) {
        String lowerString;
        String firstLetter = className.substring(0, 1);
        String leftLetters = className.substring(1);

        lowerString = firstLetter.toLowerCase() + leftLetters;
        return lowerString;
    }

    private static String generateMethodHeader(MethodAccess methodAccess, String returnName, String methodName, String paramType, String paramName) {
        String firstPart = (methodAccess == MethodAccess.DEFAULT ? "" : methodAccess.name + BLANK_FOR_PARAM )
                + returnName + BLANK_FOR_PARAM + methodName + "(";
        if (paramType != null && paramName != null) {
            firstPart += paramType + BLANK_FOR_PARAM + paramName;
        }
        firstPart += ")";
        return firstPart;
    }


    private static void outputContent2File(String outputDir, String fileName, String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            String outputPath = outputDir + DIR_SPLIT + fileName;
            fw = new FileWriter(new File(outputPath));
            bw = new BufferedWriter(fw);
            bw.write(content);
            System.out.println("====successFull write the file:" + fileName + " to path:" + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getInputPath(String info) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(info);
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = br.read()) != '\n') {
            sb.append((char) c);
        }
        return sb.toString();
    }

    private CodeGenerator() {
        throw new UnsupportedOperationException("You can't initialize me!");
    }

    /**
     * 方法的访问权限
     */
    private enum MethodAccess {

        /**
         * public
         */
        PUBLIC("public"),

        /**
         * protected
         */
        PROTECTED("protected"),

        /**
         * default
         */
        DEFAULT(""),

        /**
         * private
         */
        PRIVATE("private");

        String name;

        MethodAccess(String name) {
            this.name = name;
        }
    }
}
