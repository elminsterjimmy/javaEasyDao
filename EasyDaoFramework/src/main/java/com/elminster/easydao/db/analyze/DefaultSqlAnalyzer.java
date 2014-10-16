package com.elminster.easydao.db.analyze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.elminster.common.constants.Constants.CharacterConstants;
import com.elminster.common.constants.Constants.StringConstants;
import com.elminster.common.constants.RegexConstants;
import com.elminster.common.util.CloseUtil;
import com.elminster.common.util.ReflectUtil;
import com.elminster.common.util.StringUtil;
import com.elminster.easydao.db.analyze.expression.evaluate.Engine;
import com.elminster.easydao.db.analyze.expression.exception.ParserException;
import com.elminster.easydao.db.annotation.Sql;
import com.elminster.easydao.db.annotation.SqlFile;
import com.elminster.easydao.db.annotation.SqlParam;
import com.elminster.easydao.db.annotation.util.AnnotationUtil;
import com.elminster.easydao.db.manager.DAOSupportSession;

/**
 * A simple SQL analyzer implement.
 * 
 * @author Gu
 * @version 1.0
 * 
 */
public class DefaultSqlAnalyzer extends BaseSqlAnalyzer implements ISqlAnalyzer {

  private static final char DOT = CharacterConstants.DOT;
  private static final String SQL_REPLACEMENT = StringConstants.QUESTION;
  private static final String PARAM_KEY = StringConstants.DOLLAR;
  private static final String PARAM_KEY_REGEX = RegexConstants.REGEX_DOLLAR;

  /**
   * Constructor
   */
  public DefaultSqlAnalyzer(DAOSupportSession session) {
    super(session);
  }

  /**
   * Parser a method invocation to get the SQL statement and the SQL statement's
   * parameter(s).
   * 
   * @param method
   *          the method which original SQL statement is invoked
   * @param methodArguments
   *          the method's argument(s)
   * @throws Exception 
   */
  public void analyzeSql(Method invokedMethod, Object... methodArguments) throws AnalyzeException {
    String originalSql = getOriginalSql(invokedMethod);
    // get all SqlParam annotation(s)
    Annotation[][] parametersAnnos = invokedMethod.getParameterAnnotations();
    Map<String, Object> paramAnnoMap = new HashMap<String, Object>();
    for (int i = 0; i < parametersAnnos.length; i++) {
      Annotation[] parameterAnnos = parametersAnnos[i];
      if (null != parameterAnnos) {
        for (Annotation anno : parameterAnnos) {
          if (anno.annotationType().equals(SqlParam.class)) {
            SqlParam sqlParamAnno = (SqlParam) anno;
            paramAnnoMap.put(sqlParamAnno.value(), methodArguments[i]);
          }
        }
      }
    }
    // parser SQL
    analyzedSql = analyzeOriginalSql(originalSql, paramAnnoMap);
    int idx = -1;
    while ((idx = analyzedSql.indexOf(PARAM_KEY)) > 0) {
      boolean isSimpleData = true;
      StringBuilder builder = new StringBuilder();
      for (int i = idx + PARAM_KEY.length(); i < analyzedSql.length(); i++) {
        char ch = analyzedSql.charAt(i);
        if (invalidChar(ch)) {
          break;
        } else {
          if (DOT == ch) {
            isSimpleData = false;
          }
          builder.append(ch);
        }
      }
      String replaceKey = builder.toString();
      if (isSimpleData) {
        Object replaceValue = paramAnnoMap.get(replaceKey);
        if (null == replaceValue) {
          throw new AnalyzeException(
              "SqlParam Annotation's value and argurment are NOT matched.");
        }
        analyzedSqlParameters.add(replaceValue);
      } else {
        String[] splited = replaceKey.split(RegexConstants.REGEX_DOT);
        if (2 != splited.length) {
          throw new AnalyzeException("");
        }
        Object obj = paramAnnoMap.get(splited[0]);
        if (null == obj) {
          throw new AnalyzeException(
              "SqlParam Annotation's value and argurment are NOT matched.");
        }
        try {
          Field field = ReflectUtil.getDeclaredField(obj.getClass(), splited[1]);
          Object fieldValue = ReflectUtil.getFieldValue(obj, field);
          fieldValue = AnnotationUtil.getCustomerDBValue(field, fieldValue);
          analyzedSqlParameters.add(fieldValue);
        } catch (IllegalArgumentException e) {
          throw new AnalyzeException(e);
        } catch (IllegalAccessException e) {
          throw new AnalyzeException(e);
        }
      }
      analyzedSql = analyzedSql.replaceFirst(PARAM_KEY_REGEX + replaceKey,
          SQL_REPLACEMENT);
    }
  }

  /**
   * Only alphabetic, digit, ".", "_", "-" is valid.
   * @param ch the character
   * @return is invalid
   */
  private boolean invalidChar(char ch) {
    return !(Character.isAlphabetic(ch) || Character.isDigit(ch) || DOT == ch || 
        CharacterConstants.UNDER_LINE == ch || CharacterConstants.HYPHEN == ch);
  }

  /**
   * Analyze the original SQL statement.
   * @param originalSql the original SQL statement
   * @param paramaterMap the parameters
   * @return analyzed SQL statement
   * @throws ParseException 
   */
  private String analyzeOriginalSql(String originalSql, Map<String, Object> parameterMap) throws AnalyzeException {
    Engine engine = new Engine();
    engine.addVariables(parameterMap);
    Object rst = null;
    try {
      rst = engine.execute(originalSql);
    } catch (ParserException e) {
      throw new AnalyzeException(e);
    }
    return (String) rst;
  }

  /**
   * Get the original SQL statement which to analyze.
   * 
   * @param method
   *          the method which original SQL statement is invoked
   * @return the original SQL statement which to analyze
   */
  private String getOriginalSql(Method method) throws AnalyzeException {
    String originalSql = null;
    // Get SQL from SQL annotation
    Sql sqlAnno = method.getAnnotation(Sql.class);
    SqlFile sqlFileAnno = method.getAnnotation(SqlFile.class);
    if (null == sqlAnno && null == sqlFileAnno) {
      throw new AnalyzeException("SQL statement or SQL file is missing.");
    } else {
      String annoValue = null == sqlAnno ? null : sqlAnno.value();
      String annoFile = null == sqlFileAnno ? null : sqlFileAnno.value();
      if (!StringUtil.isEmpty(annoValue)) {
        originalSql = annoValue;
      } else if (!StringUtil.isEmpty(annoFile)) {
        originalSql = loadSqlFile(annoFile);
      } else {
        throw new AnalyzeException("SQL statement or SQL file is missing.");
      }
    }
    return originalSql;
  }

  /**
   * Load the SQL statement from a specified SQL file
   * 
   * @param fileName
   *          specified SQL file
   * @return SQL statement
   */
  private String loadSqlFile(String fileName) throws AnalyzeException {
    InputStreamReader isReader = null;
    InputStream is = null;
    BufferedReader bReader = null;
    StringBuilder sb = new StringBuilder();
    try {
      is = originalClass.getResourceAsStream(fileName);
      isReader = new InputStreamReader(is);
      bReader = new BufferedReader(isReader);
      String line = null;
      while (null != (line = bReader.readLine())) {
        sb.append(line);
        sb.append(StringUtil.newline());
      }
    } catch (FileNotFoundException fnfe) {
      throw new AnalyzeException("SQL file is not found.");
    } catch (IOException ioe) {
      throw new AnalyzeException("SQL file read error.");
    } finally {
      CloseUtil.closeReaderQuiet(bReader);
      CloseUtil.closeReaderQuiet(isReader);
      CloseUtil.closeInputStreamQuiet(is);
    }
    return sb.toString();
  }
}
