/* Generated By:JavaCC: Do not edit this line. WebMotionParser.java */
package org.debux.webmotion.netbeans.javacc.parser;

import java.util.*;

public class WebMotionParser implements WebMotionParserConstants {

    public List<ParseException> syntaxErrors = new ArrayList<ParseException>();

    void recover(ParseException ex, int ... recoveryPoints) {
        syntaxErrors.add(ex);
        Token t;
        do {
            t = getNextToken();
        } while(t.kind != EOF && testRecoveryPoint(t.kind, recoveryPoints));
    }

    boolean testRecoveryPoint(int recoveryPoint, int ... recoveryPoints) {
        for (int test : recoveryPoints) {
            if (test == recoveryPoint) {
                return true;
            }
        }
        return false;
    }

  final public void Mapping() throws ParseException {
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMENT:
        case SECTION_CONFIG_NAME:
        case SECTION_ACTIONS_NAME:
        case SECTION_ERRORS_NAME:
        case SECTION_FILTERS_NAME:
        case SECTION_EXTENSIONS_NAME:
        case SECTION_PROPERTIES_NAME:
        case SECTION_CONFIG_NEXT_CONFIG:
        case SECTION_CONFIG_NEXT_ACTIONS:
        case SECTION_CONFIG_NEXT_ERRORS:
        case SECTION_CONFIG_NEXT_FILTERS:
        case SECTION_CONFIG_NEXT_EXTENSIONS:
        case SECTION_CONFIG_NEXT_PROPERTIES:
        case SECTION_ACTIONS_NEXT_CONFIG:
        case SECTION_ACTIONS_NEXT_ACTIONS:
        case SECTION_ACTIONS_NEXT_ERRORS:
        case SECTION_ACTIONS_NEXT_FILTERS:
        case SECTION_ACTIONS_NEXT_EXTENSIONS:
        case SECTION_ACTIONS_NEXT_PROPERTIES:
        case SECTION_ERRORS_NEXT_CONFIG:
        case SECTION_ERRORS_NEXT_ACTIONS:
        case SECTION_ERRORS_NEXT_ERRORS:
        case SECTION_ERRORS_NEXT_FILTERS:
        case SECTION_ERRORS_NEXT_EXTENSIONS:
        case SECTION_ERRORS_NEXT_PROPERTIES:
        case SECTION_FILTERS_NEXT_CONFIG:
        case SECTION_FILTERS_NEXT_ACTIONS:
        case SECTION_FILTERS_NEXT_ERRORS:
        case SECTION_FILTERS_NEXT_FILTERS:
        case SECTION_FILTERS_NEXT_EXTENSIONS:
        case SECTION_FILTERS_NEXT_PROPERTIES:
        case SECTION_EXTENSIONS_NEXT_CONFIG:
        case SECTION_EXTENSIONS_NEXT_ACTIONS:
        case SECTION_EXTENSIONS_NEXT_ERRORS:
        case SECTION_EXTENSIONS_NEXT_FILTERS:
        case SECTION_EXTENSIONS_NEXT_EXTENSIONS:
        case SECTION_EXTENSIONS_NEXT_PROPERTIES:
        case SECTION_PROPERTIES_NEXT_CONFIG:
        case SECTION_PROPERTIES_NEXT_ACTIONS:
        case SECTION_PROPERTIES_NEXT_ERRORS:
        case SECTION_PROPERTIES_NEXT_FILTERS:
        case SECTION_PROPERTIES_NEXT_EXTENSIONS:
        case SECTION_PROPERTIES_NEXT_PROPERTIES:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SECTION_CONFIG_NAME:
        case SECTION_CONFIG_NEXT_CONFIG:
        case SECTION_ACTIONS_NEXT_CONFIG:
        case SECTION_ERRORS_NEXT_CONFIG:
        case SECTION_FILTERS_NEXT_CONFIG:
        case SECTION_EXTENSIONS_NEXT_CONFIG:
        case SECTION_PROPERTIES_NEXT_CONFIG:
          SectionConfig();
          break;
        case SECTION_ACTIONS_NAME:
        case SECTION_CONFIG_NEXT_ACTIONS:
        case SECTION_ACTIONS_NEXT_ACTIONS:
        case SECTION_ERRORS_NEXT_ACTIONS:
        case SECTION_FILTERS_NEXT_ACTIONS:
        case SECTION_EXTENSIONS_NEXT_ACTIONS:
        case SECTION_PROPERTIES_NEXT_ACTIONS:
          SectionActions();
          break;
        case SECTION_ERRORS_NAME:
        case SECTION_CONFIG_NEXT_ERRORS:
        case SECTION_ACTIONS_NEXT_ERRORS:
        case SECTION_ERRORS_NEXT_ERRORS:
        case SECTION_FILTERS_NEXT_ERRORS:
        case SECTION_EXTENSIONS_NEXT_ERRORS:
        case SECTION_PROPERTIES_NEXT_ERRORS:
          SectionErrors();
          break;
        case SECTION_FILTERS_NAME:
        case SECTION_CONFIG_NEXT_FILTERS:
        case SECTION_ACTIONS_NEXT_FILTERS:
        case SECTION_ERRORS_NEXT_FILTERS:
        case SECTION_FILTERS_NEXT_FILTERS:
        case SECTION_EXTENSIONS_NEXT_FILTERS:
        case SECTION_PROPERTIES_NEXT_FILTERS:
          SectionFilters();
          break;
        case SECTION_EXTENSIONS_NAME:
        case SECTION_CONFIG_NEXT_EXTENSIONS:
        case SECTION_ACTIONS_NEXT_EXTENSIONS:
        case SECTION_ERRORS_NEXT_EXTENSIONS:
        case SECTION_FILTERS_NEXT_EXTENSIONS:
        case SECTION_EXTENSIONS_NEXT_EXTENSIONS:
        case SECTION_PROPERTIES_NEXT_EXTENSIONS:
          SectionExtensions();
          break;
        case SECTION_PROPERTIES_NAME:
        case SECTION_CONFIG_NEXT_PROPERTIES:
        case SECTION_ACTIONS_NEXT_PROPERTIES:
        case SECTION_ERRORS_NEXT_PROPERTIES:
        case SECTION_FILTERS_NEXT_PROPERTIES:
        case SECTION_EXTENSIONS_NEXT_PROPERTIES:
        case SECTION_PROPERTIES_NEXT_PROPERTIES:
          SectionProperties();
          break;
        case COMMENT:
          jj_consume_token(COMMENT);
          break;
        default:
          jj_la1[1] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      jj_consume_token(0);
    } catch (ParseException ex) {
        recover(ex, EOF);
    }
  }

  final public void SectionConfig() throws ParseException {
    SectionConfigName();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_CONFIG:
      case CONFIG_KEY:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONFIG_KEY:
        SectionConfigLine();
        break;
      case COMMENT_IN_CONFIG:
        jj_consume_token(COMMENT_IN_CONFIG);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionConfigName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_CONFIG_NAME:
      jj_consume_token(SECTION_CONFIG_NAME);
      break;
    case SECTION_CONFIG_NEXT_CONFIG:
      jj_consume_token(SECTION_CONFIG_NEXT_CONFIG);
      break;
    case SECTION_ACTIONS_NEXT_CONFIG:
      jj_consume_token(SECTION_ACTIONS_NEXT_CONFIG);
      break;
    case SECTION_ERRORS_NEXT_CONFIG:
      jj_consume_token(SECTION_ERRORS_NEXT_CONFIG);
      break;
    case SECTION_FILTERS_NEXT_CONFIG:
      jj_consume_token(SECTION_FILTERS_NEXT_CONFIG);
      break;
    case SECTION_EXTENSIONS_NEXT_CONFIG:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_CONFIG);
      break;
    case SECTION_PROPERTIES_NEXT_CONFIG:
      jj_consume_token(SECTION_PROPERTIES_NEXT_CONFIG);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionConfigLine() throws ParseException {
    try {
      jj_consume_token(CONFIG_KEY);
      jj_consume_token(CONFIG_EQUALS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONFIG_VALUE:
        jj_consume_token(CONFIG_VALUE);
        break;
      default:
        jj_la1[5] = jj_gen;
        ;
      }
      jj_consume_token(CONFIG_END);
    } catch (ParseException ex) {
        recover(ex, CONFIG_END);
    }
  }

  final public void SectionActions() throws ParseException {
    SectionActionsName();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_ACTIONS:
      case ACTION_METHOD:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_METHOD:
        SectionActionsLine();
        break;
      case COMMENT_IN_ACTIONS:
        jj_consume_token(COMMENT_IN_ACTIONS);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionActionsName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_ACTIONS_NAME:
      jj_consume_token(SECTION_ACTIONS_NAME);
      break;
    case SECTION_CONFIG_NEXT_ACTIONS:
      jj_consume_token(SECTION_CONFIG_NEXT_ACTIONS);
      break;
    case SECTION_ACTIONS_NEXT_ACTIONS:
      jj_consume_token(SECTION_ACTIONS_NEXT_ACTIONS);
      break;
    case SECTION_ERRORS_NEXT_ACTIONS:
      jj_consume_token(SECTION_ERRORS_NEXT_ACTIONS);
      break;
    case SECTION_FILTERS_NEXT_ACTIONS:
      jj_consume_token(SECTION_FILTERS_NEXT_ACTIONS);
      break;
    case SECTION_EXTENSIONS_NEXT_ACTIONS:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_ACTIONS);
      break;
    case SECTION_PROPERTIES_NEXT_ACTIONS:
      jj_consume_token(SECTION_PROPERTIES_NEXT_ACTIONS);
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionActionsLine() throws ParseException {
    try {
      jj_consume_token(ACTION_METHOD);
      jj_consume_token(ACTION_SEPARATOR);
      jj_consume_token(ACTION_PATH);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_PATH_VALUE:
        case ACTION_PATH_VARIABLE:
          ;
          break;
        default:
          jj_la1[9] = jj_gen;
          break label_4;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_PATH_VALUE:
          jj_consume_token(ACTION_PATH_VALUE);
          break;
        case ACTION_PATH_VARIABLE:
          jj_consume_token(ACTION_PATH_VARIABLE);
          break;
        default:
          jj_la1[10] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_PATH:
          jj_consume_token(ACTION_PATH);
          break;
        default:
          jj_la1[11] = jj_gen;
          ;
        }
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_PARAMETERS_BEGIN:
        jj_consume_token(ACTION_PARAMETERS_BEGIN);
        SectionActionsPathParameter();
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_PATH_PARAMETER_OTHER:
          case ACTION_PATH_PARAMETER_VALUE_OTHER:
            ;
            break;
          default:
            jj_la1[12] = jj_gen;
            break label_5;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_PATH_PARAMETER_OTHER:
            jj_consume_token(ACTION_PATH_PARAMETER_OTHER);
            break;
          case ACTION_PATH_PARAMETER_VALUE_OTHER:
            jj_consume_token(ACTION_PATH_PARAMETER_VALUE_OTHER);
            break;
          default:
            jj_la1[13] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          SectionActionsPathParameter();
        }
        break;
      default:
        jj_la1[14] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_PATH_END:
        jj_consume_token(ACTION_PATH_END);
        break;
      case ACTION_PATH_PARAMETER_SEPARATOR:
        jj_consume_token(ACTION_PATH_PARAMETER_SEPARATOR);
        break;
      case ACTION_PATH_PARAMETER_VALUE_SEPARATOR:
        jj_consume_token(ACTION_PATH_PARAMETER_VALUE_SEPARATOR);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_ACTION_JAVA_BEGIN:
      case ACTION_ACTION_VARIABLE:
      case ACTION_ACTION_IDENTIFIER:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_ACTION_JAVA_BEGIN:
          jj_consume_token(ACTION_ACTION_JAVA_BEGIN);
          break;
        default:
          jj_la1[16] = jj_gen;
          ;
        }
        label_6:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_VARIABLE:
            jj_consume_token(ACTION_ACTION_VARIABLE);
            break;
          case ACTION_ACTION_IDENTIFIER:
            jj_consume_token(ACTION_ACTION_IDENTIFIER);
            break;
          default:
            jj_la1[17] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_VARIABLE:
          case ACTION_ACTION_IDENTIFIER:
            ;
            break;
          default:
            jj_la1[18] = jj_gen;
            break label_6;
          }
        }
        label_7:
        while (true) {
          jj_consume_token(ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER);
          label_8:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case ACTION_ACTION_JAVA_VARIABLE:
              jj_consume_token(ACTION_ACTION_JAVA_VARIABLE);
              break;
            case ACTION_ACTION_JAVA_IDENTIFIER:
              jj_consume_token(ACTION_ACTION_JAVA_IDENTIFIER);
              break;
            default:
              jj_la1[19] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case ACTION_ACTION_JAVA_IDENTIFIER:
            case ACTION_ACTION_JAVA_VARIABLE:
              ;
              break;
            default:
              jj_la1[20] = jj_gen;
              break label_8;
            }
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER:
            ;
            break;
          default:
            jj_la1[21] = jj_gen;
            break label_7;
          }
        }
        break;
      case ACTION_ACTION_VIEW:
        jj_consume_token(ACTION_ACTION_VIEW);
        label_9:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_VIEW_VALUE:
            jj_consume_token(ACTION_ACTION_VIEW_VALUE);
            break;
          case ACTION_ACTION_VIEW_VARIABLE:
            jj_consume_token(ACTION_ACTION_VIEW_VARIABLE);
            break;
          default:
            jj_la1[22] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_VIEW_VALUE:
          case ACTION_ACTION_VIEW_VARIABLE:
            ;
            break;
          default:
            jj_la1[23] = jj_gen;
            break label_9;
          }
        }
        break;
      case ACTION_ACTION_LINK:
        jj_consume_token(ACTION_ACTION_LINK);
        label_10:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_LINK_VALUE:
            jj_consume_token(ACTION_ACTION_LINK_VALUE);
            break;
          case ACTION_ACTION_LINK_VARIABLE:
            jj_consume_token(ACTION_ACTION_LINK_VARIABLE);
            break;
          default:
            jj_la1[24] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_ACTION_LINK_VALUE:
          case ACTION_ACTION_LINK_VARIABLE:
            ;
            break;
          default:
            jj_la1[25] = jj_gen;
            break label_10;
          }
        }
        break;
      default:
        jj_la1[26] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_ACTION_JAVA_SEPARATOR:
      case ACTION_ACTION_VIEW_SEPARATOR:
      case ACTION_ACTION_LINK_SEPARATOR:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_ACTION_JAVA_SEPARATOR:
          jj_consume_token(ACTION_ACTION_JAVA_SEPARATOR);
          break;
        case ACTION_ACTION_VIEW_SEPARATOR:
          jj_consume_token(ACTION_ACTION_VIEW_SEPARATOR);
          break;
        case ACTION_ACTION_LINK_SEPARATOR:
          jj_consume_token(ACTION_ACTION_LINK_SEPARATOR);
          break;
        default:
          jj_la1[27] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        SectionActionsParameter();
        label_11:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_PARAMETER_SEPARATOR:
          case ACTION_PARAMETER_VALUE_SEPARATOR:
            ;
            break;
          default:
            jj_la1[28] = jj_gen;
            break label_11;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case ACTION_PARAMETER_SEPARATOR:
            jj_consume_token(ACTION_PARAMETER_SEPARATOR);
            break;
          case ACTION_PARAMETER_VALUE_SEPARATOR:
            jj_consume_token(ACTION_PARAMETER_VALUE_SEPARATOR);
            break;
          default:
            jj_la1[29] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          SectionActionsParameter();
        }
        break;
      default:
        jj_la1[30] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_ACTION_JAVA_END:
        jj_consume_token(ACTION_ACTION_JAVA_END);
        break;
      case ACTION_ACTION_VIEW_END:
        jj_consume_token(ACTION_ACTION_VIEW_END);
        break;
      case ACTION_ACTION_LINK_END:
        jj_consume_token(ACTION_ACTION_LINK_END);
        break;
      case ACTION_PARAMETER_VALUE_END:
        jj_consume_token(ACTION_PARAMETER_VALUE_END);
        break;
      case ACTION_END:
        jj_consume_token(ACTION_END);
        break;
      default:
        jj_la1[31] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException ex) {
        recover(ex, ACTION_ACTION_JAVA_END, ACTION_ACTION_VIEW_END, ACTION_ACTION_LINK_END, ACTION_PARAMETER_VALUE_END, ACTION_END);
    }
  }

  final public void SectionActionsPathParameter() throws ParseException {
    jj_consume_token(ACTION_PATH_PARAMETER_NAME);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ACTION_PATH_PARAMETER_EQUALS:
      jj_consume_token(ACTION_PATH_PARAMETER_EQUALS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION_PATH_PARAMETER_VALUE:
      case ACTION_PATH_PARAMETER_VALUE_VARIABLE:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ACTION_PATH_PARAMETER_VALUE:
          jj_consume_token(ACTION_PATH_PARAMETER_VALUE);
          break;
        case ACTION_PATH_PARAMETER_VALUE_VARIABLE:
          jj_consume_token(ACTION_PATH_PARAMETER_VALUE_VARIABLE);
          break;
        default:
          jj_la1[32] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[33] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[34] = jj_gen;
      ;
    }
  }

  final public void SectionActionsParameter() throws ParseException {
    jj_consume_token(ACTION_PARAMETER_NAME);
    jj_consume_token(ACTION_PARAMETER_EQUALS);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ACTION_PARAMETER_VALUE:
      jj_consume_token(ACTION_PARAMETER_VALUE);
      break;
    default:
      jj_la1[35] = jj_gen;
      ;
    }
  }

  final public void SectionErrors() throws ParseException {
    SectionErrorsName();
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_ERRORS:
      case ERROR_CODE:
      case ALL:
      case EXCEPTION:
        ;
        break;
      default:
        jj_la1[36] = jj_gen;
        break label_12;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ERROR_CODE:
      case ALL:
      case EXCEPTION:
        SectionErrorsLine();
        break;
      case COMMENT_IN_ERRORS:
        jj_consume_token(COMMENT_IN_ERRORS);
        break;
      default:
        jj_la1[37] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionErrorsName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_ERRORS_NAME:
      jj_consume_token(SECTION_ERRORS_NAME);
      break;
    case SECTION_CONFIG_NEXT_ERRORS:
      jj_consume_token(SECTION_CONFIG_NEXT_ERRORS);
      break;
    case SECTION_ACTIONS_NEXT_ERRORS:
      jj_consume_token(SECTION_ACTIONS_NEXT_ERRORS);
      break;
    case SECTION_ERRORS_NEXT_ERRORS:
      jj_consume_token(SECTION_ERRORS_NEXT_ERRORS);
      break;
    case SECTION_FILTERS_NEXT_ERRORS:
      jj_consume_token(SECTION_FILTERS_NEXT_ERRORS);
      break;
    case SECTION_EXTENSIONS_NEXT_ERRORS:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_ERRORS);
      break;
    case SECTION_PROPERTIES_NEXT_ERRORS:
      jj_consume_token(SECTION_PROPERTIES_NEXT_ERRORS);
      break;
    default:
      jj_la1[38] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionErrorsLine() throws ParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ALL:
        jj_consume_token(ALL);
        break;
      case ERROR_CODE:
        jj_consume_token(ERROR_CODE);
        jj_consume_token(ERROR_CODE_VALUE);
        break;
      case EXCEPTION:
        jj_consume_token(EXCEPTION);
        break;
      default:
        jj_la1[39] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(ERROR_SEPARATOR);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ERROR_ACTION_JAVA_BEGIN:
      case ERROR_ACTION_JAVA:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ERROR_ACTION_JAVA_BEGIN:
          jj_consume_token(ERROR_ACTION_JAVA_BEGIN);
          break;
        default:
          jj_la1[40] = jj_gen;
          ;
        }
        jj_consume_token(ERROR_ACTION_JAVA);
        break;
      case ERROR_ACTION_VIEW_BEGIN:
        jj_consume_token(ERROR_ACTION_VIEW_BEGIN);
        jj_consume_token(ERROR_ACTION_VALUE);
        break;
      case ERROR_ACTION_LINK_BEGIN:
        jj_consume_token(ERROR_ACTION_LINK_BEGIN);
        jj_consume_token(ERROR_ACTION_VALUE);
        break;
      default:
        jj_la1[41] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ERROR_END:
        jj_consume_token(ERROR_END);
        break;
      case ERROR_VALUE_END:
        jj_consume_token(ERROR_VALUE_END);
        break;
      default:
        jj_la1[42] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException ex) {
        recover(ex, ERROR_END);
    }
  }

  final public void SectionFilters() throws ParseException {
    SectionFiltersName();
    label_13:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_FILTERS:
      case FILTER_METHOD:
        ;
        break;
      default:
        jj_la1[43] = jj_gen;
        break label_13;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FILTER_METHOD:
        SectionFiltersLine();
        break;
      case COMMENT_IN_FILTERS:
        jj_consume_token(COMMENT_IN_FILTERS);
        break;
      default:
        jj_la1[44] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionFiltersName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_FILTERS_NAME:
      jj_consume_token(SECTION_FILTERS_NAME);
      break;
    case SECTION_CONFIG_NEXT_FILTERS:
      jj_consume_token(SECTION_CONFIG_NEXT_FILTERS);
      break;
    case SECTION_ACTIONS_NEXT_FILTERS:
      jj_consume_token(SECTION_ACTIONS_NEXT_FILTERS);
      break;
    case SECTION_ERRORS_NEXT_FILTERS:
      jj_consume_token(SECTION_ERRORS_NEXT_FILTERS);
      break;
    case SECTION_FILTERS_NEXT_FILTERS:
      jj_consume_token(SECTION_FILTERS_NEXT_FILTERS);
      break;
    case SECTION_EXTENSIONS_NEXT_FILTERS:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_FILTERS);
      break;
    case SECTION_PROPERTIES_NEXT_FILTERS:
      jj_consume_token(SECTION_PROPERTIES_NEXT_FILTERS);
      break;
    default:
      jj_la1[45] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionFiltersLine() throws ParseException {
    try {
      jj_consume_token(FILTER_METHOD);
      jj_consume_token(FILTER_SEPARATOR);
      label_14:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case FILTER_PATH:
          jj_consume_token(FILTER_PATH);
          break;
        case FILTER_PATH_ALL:
          jj_consume_token(FILTER_PATH_ALL);
          break;
        default:
          jj_la1[46] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case FILTER_PATH:
        case FILTER_PATH_ALL:
          ;
          break;
        default:
          jj_la1[47] = jj_gen;
          break label_14;
        }
      }
      jj_consume_token(FILTER_SEPARATOR);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FILTER_ACTION_BEGIN:
        jj_consume_token(FILTER_ACTION_BEGIN);
        break;
      default:
        jj_la1[48] = jj_gen;
        ;
      }
      jj_consume_token(FILTER_ACTION);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FILTER_PARAMETERS_SEPARATOR:
        jj_consume_token(FILTER_PARAMETERS_SEPARATOR);
        SectionFiltersParameter();
        label_15:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case FILTER_PARAMETER_SEPARATOR:
          case FILTER_PARAMETER_VALUE_SEPARATOR:
            ;
            break;
          default:
            jj_la1[49] = jj_gen;
            break label_15;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case FILTER_PARAMETER_SEPARATOR:
            jj_consume_token(FILTER_PARAMETER_SEPARATOR);
            break;
          case FILTER_PARAMETER_VALUE_SEPARATOR:
            jj_consume_token(FILTER_PARAMETER_VALUE_SEPARATOR);
            break;
          default:
            jj_la1[50] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          SectionFiltersParameter();
        }
        break;
      default:
        jj_la1[51] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FILTER_END:
        jj_consume_token(FILTER_END);
        break;
      case FILTER_PARAMETER_VALUE_END:
        jj_consume_token(FILTER_PARAMETER_VALUE_END);
        break;
      default:
        jj_la1[52] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } catch (ParseException ex) {
        recover(ex, FILTER_END, FILTER_PARAMETER_VALUE_END);
    }
  }

  final public void SectionFiltersParameter() throws ParseException {
    jj_consume_token(FILTER_PARAMETER_NAME);
    jj_consume_token(FILTER_PARAMETER_EQUALS);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FILTER_PARAMETER_VALUE:
      jj_consume_token(FILTER_PARAMETER_VALUE);
      break;
    default:
      jj_la1[53] = jj_gen;
      ;
    }
  }

  final public void SectionExtensions() throws ParseException {
    SectionExtensionsName();
    label_16:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_EXTENSIONS:
      case EXTENSION_PATH:
        ;
        break;
      default:
        jj_la1[54] = jj_gen;
        break label_16;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EXTENSION_PATH:
        SectionExtensionsLine();
        break;
      case COMMENT_IN_EXTENSIONS:
        jj_consume_token(COMMENT_IN_EXTENSIONS);
        break;
      default:
        jj_la1[55] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionExtensionsName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_EXTENSIONS_NAME:
      jj_consume_token(SECTION_EXTENSIONS_NAME);
      break;
    case SECTION_CONFIG_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_CONFIG_NEXT_EXTENSIONS);
      break;
    case SECTION_ACTIONS_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_ACTIONS_NEXT_EXTENSIONS);
      break;
    case SECTION_ERRORS_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_ERRORS_NEXT_EXTENSIONS);
      break;
    case SECTION_FILTERS_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_FILTERS_NEXT_EXTENSIONS);
      break;
    case SECTION_EXTENSIONS_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_EXTENSIONS);
      break;
    case SECTION_PROPERTIES_NEXT_EXTENSIONS:
      jj_consume_token(SECTION_PROPERTIES_NEXT_EXTENSIONS);
      break;
    default:
      jj_la1[56] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionExtensionsLine() throws ParseException {
    try {
      label_17:
      while (true) {
        jj_consume_token(EXTENSION_PATH);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EXTENSION_PATH:
          ;
          break;
        default:
          jj_la1[57] = jj_gen;
          break label_17;
        }
      }
      jj_consume_token(EXTENSION_SEPARATOR);
      jj_consume_token(EXTENSION_FILE);
      jj_consume_token(EXTENSION_END);
    } catch (ParseException ex) {
        recover(ex, EXTENSION_END);
    }
  }

  final public void SectionProperties() throws ParseException {
    SectionPropertiesName();
    label_18:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMENT_IN_PROPERTIES:
      case PROPERTIE_NAME:
        ;
        break;
      default:
        jj_la1[58] = jj_gen;
        break label_18;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTIE_NAME:
        SectionPropertiesLine();
        break;
      case COMMENT_IN_PROPERTIES:
        jj_consume_token(COMMENT_IN_PROPERTIES);
        break;
      default:
        jj_la1[59] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void SectionPropertiesName() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SECTION_PROPERTIES_NAME:
      jj_consume_token(SECTION_PROPERTIES_NAME);
      break;
    case SECTION_CONFIG_NEXT_PROPERTIES:
      jj_consume_token(SECTION_CONFIG_NEXT_PROPERTIES);
      break;
    case SECTION_ACTIONS_NEXT_PROPERTIES:
      jj_consume_token(SECTION_ACTIONS_NEXT_PROPERTIES);
      break;
    case SECTION_ERRORS_NEXT_PROPERTIES:
      jj_consume_token(SECTION_ERRORS_NEXT_PROPERTIES);
      break;
    case SECTION_FILTERS_NEXT_PROPERTIES:
      jj_consume_token(SECTION_FILTERS_NEXT_PROPERTIES);
      break;
    case SECTION_EXTENSIONS_NEXT_PROPERTIES:
      jj_consume_token(SECTION_EXTENSIONS_NEXT_PROPERTIES);
      break;
    case SECTION_PROPERTIES_NEXT_PROPERTIES:
      jj_consume_token(SECTION_PROPERTIES_NEXT_PROPERTIES);
      break;
    default:
      jj_la1[60] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SectionPropertiesLine() throws ParseException {
    try {
      jj_consume_token(PROPERTIE_NAME);
      jj_consume_token(PROPERTIE_EQUALS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PROPERTIE_VALUE:
        jj_consume_token(PROPERTIE_VALUE);
        break;
      default:
        jj_la1[61] = jj_gen;
        ;
      }
      jj_consume_token(PROPERTIE_END);
    } catch (ParseException ex) {
        recover(ex, PROPERTIE_END);
    }
  }

  /** Generated Token Manager. */
  public WebMotionParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[62];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static private int[] jj_la1_3;
  static private int[] jj_la1_4;
  static private int[] jj_la1_5;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
      jj_la1_init_3();
      jj_la1_init_4();
      jj_la1_init_5();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xf0003f02,0xf0003f02,0x4,0x4,0x10000100,0x0,0x8,0x8,0x20000200,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x10,0x10,0x40000400,0x0,0x0,0x0,0x0,0x20,0x20,0x80000800,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x40,0x40,0x1000,0x0,0x80,0x80,0x2000,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0xfc3,0xfc3,0x4,0x4,0x40,0x10,0x1000,0x1000,0x80,0x300000,0x300000,0x80000,0x44000000,0x44000000,0x400000,0x88800000,0x4000,0x18000,0x18000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x7c000,0x0,0x0,0x0,0x0,0x0,0x30000000,0x30000000,0x2000000,0x0,0x0,0x0,0x100,0x0,0x0,0x0,0x0,0x0,0x0,0x200,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x401,0x0,0x0,0x0,0x802,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x7e00000,0x7e00000,0x0,0x0,0x200000,0x0,0x0,0x0,0x400000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x6,0x6,0x1,0x60,0x60,0x600,0x600,0x0,0x888,0x90000,0x90000,0x888,0x121110,0x0,0x0,0x0,0x40000,0x68000000,0x68000000,0x800000,0x68000000,0x0,0x0,0x0,0x0,0x0,0x1000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2000000,0x0,0x0,0x0,0x4000000,0x0,};
   }
   private static void jj_la1_init_3() {
      jj_la1_3 = new int[] {0xf8001f80,0xf8001f80,0x0,0x0,0x8000080,0x0,0x0,0x0,0x10000100,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x20000200,0x0,0x1,0xf,0x50,0x2000,0x2000,0x40000400,0xc000,0xc000,0x20000,0x2400000,0x2400000,0x80000,0x4800000,0x1000000,0x0,0x0,0x80000800,0x0,0x0,0x0,0x1000,0x0,};
   }
   private static void jj_la1_init_4() {
      jj_la1_4 = new int[] {0x7e1,0x7e1,0x0,0x0,0x20,0x0,0x0,0x0,0x40,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x80,0x0,0x0,0x0,0x0,0x0,0x0,0x100,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2,0x2,0x200,0x2,0x800,0x800,0x401,0x2000,};
   }
   private static void jj_la1_init_5() {
      jj_la1_5 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }

  /** Constructor with InputStream. */
  public WebMotionParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public WebMotionParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new WebMotionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public WebMotionParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new WebMotionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public WebMotionParser(WebMotionParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(WebMotionParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 62; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[165];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 62; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
          if ((jj_la1_3[i] & (1<<j)) != 0) {
            la1tokens[96+j] = true;
          }
          if ((jj_la1_4[i] & (1<<j)) != 0) {
            la1tokens[128+j] = true;
          }
          if ((jj_la1_5[i] & (1<<j)) != 0) {
            la1tokens[160+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 165; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
